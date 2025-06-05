package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Cage;
import com.example.rabbitfarm.repository.CageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CageService {

    private final CageRepository cageRepository;

    @Autowired
    public CageService(CageRepository cageRepository) {
        this.cageRepository = cageRepository;
    }

    public Cage saveCage(Cage cage) {
        // Add any validation or business logic here
        return cageRepository.save(cage);
    }

    public List<Cage> getAllCages() {
        return cageRepository.findAll();
    }

    public Optional<Cage> getCageById(Long id) {
        return cageRepository.findById(id);
    }

    public Optional<Cage> getCageByCageNumber(String cageNumber) {
        return cageRepository.findByCageNumber(cageNumber);
    }

    public Cage updateCage(Long id, Cage cageDetails) {
        Cage cage = cageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cage not found with id: " + id)); // Replace with custom exception

        cage.setCageNumber(cageDetails.getCageNumber());
        cage.setCapacity(cageDetails.getCapacity());
        cage.setLocation(cageDetails.getLocation());
        // Handle rabbits association if necessary
        return cageRepository.save(cage);
    }

    public void deleteCage(Long id) {
        if (!cageRepository.existsById(id)) {
            throw new RuntimeException("Cage not found with id: " + id); // Replace with custom exception
        }
        // Add logic to handle rabbits in the cage before deletion if needed
        cageRepository.deleteById(id);
    }
}
