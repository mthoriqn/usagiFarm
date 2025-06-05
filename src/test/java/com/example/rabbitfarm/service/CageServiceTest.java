package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Cage;
import com.example.rabbitfarm.repository.CageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CageServiceTest {

    @Mock
    private CageRepository cageRepository;

    @InjectMocks
    private CageService cageService;

    private Cage cage1;
    private Cage cage2;

    @BeforeEach
    void setUp() {
        cage1 = new Cage("C001", 10, "Shed A");
        cage1.setId(1L);
        cage2 = new Cage("C002", 5, "Shed B");
        cage2.setId(2L);
    }

    @Test
    void saveCage_shouldSaveAndReturnCage() {
        when(cageRepository.save(any(Cage.class))).thenReturn(cage1);
        Cage savedCage = cageService.saveCage(cage1);
        assertThat(savedCage).isNotNull();
        assertThat(savedCage.getCageNumber()).isEqualTo("C001");
        verify(cageRepository, times(1)).save(cage1);
    }

    @Test
    void getAllCages_shouldReturnListOfCages() {
        when(cageRepository.findAll()).thenReturn(Arrays.asList(cage1, cage2));
        List<Cage> cages = cageService.getAllCages();
        assertThat(cages).hasSize(2);
        assertThat(cages).containsExactly(cage1, cage2);
        verify(cageRepository, times(1)).findAll();
    }

    @Test
    void getCageById_whenCageExists_shouldReturnCage() {
        when(cageRepository.findById(1L)).thenReturn(Optional.of(cage1));
        Optional<Cage> foundCage = cageService.getCageById(1L);
        assertThat(foundCage).isPresent();
        assertThat(foundCage.get()).isEqualTo(cage1);
        verify(cageRepository, times(1)).findById(1L);
    }

    @Test
    void getCageById_whenCageDoesNotExist_shouldReturnEmpty() {
        when(cageRepository.findById(3L)).thenReturn(Optional.empty());
        Optional<Cage> foundCage = cageService.getCageById(3L);
        assertThat(foundCage).isNotPresent();
        verify(cageRepository, times(1)).findById(3L);
    }

    @Test
    void getCageByCageNumber_whenCageExists_shouldReturnCage() {
        when(cageRepository.findByCageNumber("C001")).thenReturn(Optional.of(cage1));
        Optional<Cage> foundCage = cageService.getCageByCageNumber("C001");
        assertThat(foundCage).isPresent();
        assertThat(foundCage.get().getCageNumber()).isEqualTo("C001");
        verify(cageRepository, times(1)).findByCageNumber("C001");
    }

    @Test
    void updateCage_whenCageExists_shouldUpdateAndReturnCage() {
        Cage cageDetails = new Cage("C001-Updated", 12, "Shed A-Prime");
        when(cageRepository.findById(1L)).thenReturn(Optional.of(cage1));
        when(cageRepository.save(any(Cage.class))).thenAnswer(invocation -> invocation.getArgument(0)); // return the saved entity

        Cage updatedCage = cageService.updateCage(1L, cageDetails);

        assertThat(updatedCage).isNotNull();
        assertThat(updatedCage.getId()).isEqualTo(1L);
        assertThat(updatedCage.getCageNumber()).isEqualTo("C001-Updated");
        assertThat(updatedCage.getCapacity()).isEqualTo(12);
        assertThat(updatedCage.getLocation()).isEqualTo("Shed A-Prime");
        verify(cageRepository, times(1)).findById(1L);
        verify(cageRepository, times(1)).save(any(Cage.class));
    }

    @Test
    void updateCage_whenCageDoesNotExist_shouldThrowException() {
        Cage cageDetails = new Cage("C003", 10, "Shed C");
        when(cageRepository.findById(3L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cageService.updateCage(3L, cageDetails);
        });
        assertThat(exception.getMessage()).isEqualTo("Cage not found with id: 3");
        verify(cageRepository, times(1)).findById(3L);
        verify(cageRepository, never()).save(any(Cage.class));
    }

    @Test
    void deleteCage_whenCageExists_shouldDeleteCage() {
        when(cageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cageRepository).deleteById(1L);
        cageService.deleteCage(1L);
        verify(cageRepository, times(1)).existsById(1L);
        verify(cageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCage_whenCageDoesNotExist_shouldThrowException() {
        when(cageRepository.existsById(3L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cageService.deleteCage(3L);
        });
        assertThat(exception.getMessage()).isEqualTo("Cage not found with id: 3");
        verify(cageRepository, times(1)).existsById(3L);
        verify(cageRepository, never()).deleteById(anyLong());
    }
}
