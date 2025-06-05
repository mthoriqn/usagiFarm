package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.RabbitStatus;
import com.example.rabbitfarm.repository.RabbitRepository;
import com.example.rabbitfarm.repository.CageRepository; // For cage validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RabbitService {

    private final RabbitRepository rabbitRepository;
    private final CageRepository cageRepository; // Optional: for validating cage existence

    @Autowired
    public RabbitService(RabbitRepository rabbitRepository, CageRepository cageRepository) {
        this.rabbitRepository = rabbitRepository;
        this.cageRepository = cageRepository;
    }

    public Rabbit saveRabbit(Rabbit rabbit) {
        // Example: Validate cage exists if cage_id is provided
        if (rabbit.getCage() != null && rabbit.getCage().getId() != null) {
            cageRepository.findById(rabbit.getCage().getId())
                    .orElseThrow(() -> new RuntimeException("Cage not found with id: " + rabbit.getCage().getId()));
        }
        return rabbitRepository.save(rabbit);
    }

    public List<Rabbit> getAllRabbits() {
        return rabbitRepository.findAll();
    }

    public Optional<Rabbit> getRabbitById(Long id) {
        return rabbitRepository.findById(id);
    }

    public List<Rabbit> getRabbitsByStatus(RabbitStatus status) {
        return rabbitRepository.findByStatus(status);
    }

    public List<Rabbit> getRabbitsByCageId(Long cageId) {
        return rabbitRepository.findByCageId(cageId);
    }

    public Rabbit updateRabbit(Long id, Rabbit rabbitDetails) {
        Rabbit rabbit = rabbitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rabbit not found with id: " + id));

        rabbit.setName(rabbitDetails.getName());
        rabbit.setBreed(rabbitDetails.getBreed());
        rabbit.setDateOfBirth(rabbitDetails.getDateOfBirth());
        rabbit.setGender(rabbitDetails.getGender());
        rabbit.setStatus(rabbitDetails.getStatus());
        rabbit.setNotes(rabbitDetails.getNotes());

        if (rabbitDetails.getCage() != null && rabbitDetails.getCage().getId() != null) {
            cageRepository.findById(rabbitDetails.getCage().getId())
                    .orElseThrow(() -> new RuntimeException("Cage not found with id: " + rabbitDetails.getCage().getId()));
            rabbit.setCage(rabbitDetails.getCage());
        } else {
            rabbit.setCage(null);
        }
        return rabbitRepository.save(rabbit);
    }

    public void deleteRabbit(Long id) {
        if (!rabbitRepository.existsById(id)) {
            throw new RuntimeException("Rabbit not found with id: " + id);
        }
        // Add logic to handle related records (sales, matings) if needed
        rabbitRepository.deleteById(id);
    }
}
