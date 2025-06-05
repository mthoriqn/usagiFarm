package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.Cage;
import com.example.rabbitfarm.model.Gender;
import com.example.rabbitfarm.model.RabbitStatus;
import com.example.rabbitfarm.repository.RabbitRepository;
import com.example.rabbitfarm.repository.CageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitServiceTest {

    @Mock
    private RabbitRepository rabbitRepository;

    @Mock
    private CageRepository cageRepository; // RabbitService depends on this for validation

    @InjectMocks
    private RabbitService rabbitService;

    private Rabbit rabbit1;
    private Rabbit rabbit2;
    private Cage cage1;

    @BeforeEach
    void setUp() {
        cage1 = new Cage("C001", 10, "Shed A");
        cage1.setId(1L);

        rabbit1 = new Rabbit();
        rabbit1.setId(1L);
        rabbit1.setName("Bunny");
        rabbit1.setBreed("Dutch");
        rabbit1.setDateOfBirth(LocalDate.now().minusMonths(6));
        rabbit1.setGender(Gender.MALE);
        rabbit1.setStatus(RabbitStatus.AVAILABLE);
        rabbit1.setCage(cage1);

        rabbit2 = new Rabbit();
        rabbit2.setId(2L);
        rabbit2.setName("Floppy");
        // ... other properties
    }

    @Test
    void saveRabbit_withValidCage_shouldSaveAndReturnRabbit() {
        when(cageRepository.findById(1L)).thenReturn(Optional.of(cage1)); // Cage validation
        when(rabbitRepository.save(any(Rabbit.class))).thenReturn(rabbit1);

        Rabbit savedRabbit = rabbitService.saveRabbit(rabbit1);

        assertThat(savedRabbit).isNotNull();
        assertThat(savedRabbit.getName()).isEqualTo("Bunny");
        verify(cageRepository, times(1)).findById(1L);
        verify(rabbitRepository, times(1)).save(rabbit1);
    }

    @Test
    void saveRabbit_withNoCage_shouldSaveAndReturnRabbit() {
        rabbit1.setCage(null); // No cage assigned
        when(rabbitRepository.save(any(Rabbit.class))).thenReturn(rabbit1);

        Rabbit savedRabbit = rabbitService.saveRabbit(rabbit1);

        assertThat(savedRabbit).isNotNull();
        assertThat(savedRabbit.getName()).isEqualTo("Bunny");
        assertThat(savedRabbit.getCage()).isNull();
        verify(cageRepository, never()).findById(anyLong()); // Should not try to find a null cage
        verify(rabbitRepository, times(1)).save(rabbit1);
    }

    @Test
    void saveRabbit_withInvalidCage_shouldThrowException() {
        rabbit1.setCage(new Cage()); // Set cage but no ID, or an ID that doesn't exist
        rabbit1.getCage().setId(99L);
        when(cageRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            rabbitService.saveRabbit(rabbit1);
        });

        assertThat(exception.getMessage()).isEqualTo("Cage not found with id: 99");
        verify(cageRepository, times(1)).findById(99L);
        verify(rabbitRepository, never()).save(any(Rabbit.class));
    }

    @Test
    void getAllRabbits_shouldReturnListOfRabbits() {
        when(rabbitRepository.findAll()).thenReturn(Arrays.asList(rabbit1, rabbit2));
        List<Rabbit> rabbits = rabbitService.getAllRabbits();
        assertThat(rabbits).hasSize(2).containsExactly(rabbit1, rabbit2);
        verify(rabbitRepository, times(1)).findAll();
    }

    @Test
    void getRabbitById_whenRabbitExists_shouldReturnRabbit() {
        when(rabbitRepository.findById(1L)).thenReturn(Optional.of(rabbit1));
        Optional<Rabbit> found = rabbitService.getRabbitById(1L);
        assertThat(found).isPresent().contains(rabbit1);
        verify(rabbitRepository, times(1)).findById(1L);
    }

    @Test
    void getRabbitsByStatus_shouldReturnMatchingRabbits() {
        when(rabbitRepository.findByStatus(RabbitStatus.AVAILABLE)).thenReturn(List.of(rabbit1));
        List<Rabbit> availableRabbits = rabbitService.getRabbitsByStatus(RabbitStatus.AVAILABLE);
        assertThat(availableRabbits).hasSize(1).contains(rabbit1);
        verify(rabbitRepository, times(1)).findByStatus(RabbitStatus.AVAILABLE);
    }

    @Test
    void updateRabbit_whenRabbitExistsAndCageValid_shouldUpdateAndReturnRabbit() {
        Rabbit details = new Rabbit();
        details.setName("Bunny Updated");
        details.setBreed("Mini Rex");
        details.setCage(cage1); // Valid cage

        when(rabbitRepository.findById(1L)).thenReturn(Optional.of(rabbit1));
        when(cageRepository.findById(cage1.getId())).thenReturn(Optional.of(cage1)); // Cage validation for update
        when(rabbitRepository.save(any(Rabbit.class))).thenAnswer(inv -> inv.getArgument(0));

        Rabbit updatedRabbit = rabbitService.updateRabbit(1L, details);

        assertThat(updatedRabbit.getName()).isEqualTo("Bunny Updated");
        assertThat(updatedRabbit.getBreed()).isEqualTo("Mini Rex");
        assertThat(updatedRabbit.getCage().getId()).isEqualTo(cage1.getId());
        verify(rabbitRepository, times(1)).findById(1L);
        verify(rabbitRepository, times(1)).save(any(Rabbit.class));
        verify(cageRepository, times(1)).findById(cage1.getId());
    }

    @Test
    void updateRabbit_whenRabbitExistsAndCageSetToNull_shouldUpdateAndReturnRabbit() {
        Rabbit details = new Rabbit();
        details.setName("Bunny Updated");
        details.setCage(null); // Setting cage to null

        when(rabbitRepository.findById(1L)).thenReturn(Optional.of(rabbit1)); // rabbit1 initially has a cage
        when(rabbitRepository.save(any(Rabbit.class))).thenAnswer(inv -> inv.getArgument(0));

        Rabbit updatedRabbit = rabbitService.updateRabbit(1L, details);

        assertThat(updatedRabbit.getName()).isEqualTo("Bunny Updated");
        assertThat(updatedRabbit.getCage()).isNull();
        verify(rabbitRepository, times(1)).findById(1L);
        verify(rabbitRepository, times(1)).save(any(Rabbit.class));
        verify(cageRepository, never()).findById(anyLong()); // No cage ID to find
    }


    @Test
    void updateRabbit_whenRabbitDoesNotExist_shouldThrowException() {
        Rabbit details = new Rabbit();
        when(rabbitRepository.findById(3L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            rabbitService.updateRabbit(3L, details);
        });
        assertThat(exception.getMessage()).isEqualTo("Rabbit not found with id: 3");
        verify(rabbitRepository, times(1)).findById(3L);
        verify(rabbitRepository, never()).save(any(Rabbit.class));
    }

    @Test
    void deleteRabbit_whenRabbitExists_shouldDeleteRabbit() {
        when(rabbitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(rabbitRepository).deleteById(1L);
        rabbitService.deleteRabbit(1L);
        verify(rabbitRepository, times(1)).existsById(1L);
        verify(rabbitRepository, times(1)).deleteById(1L);
    }
}
