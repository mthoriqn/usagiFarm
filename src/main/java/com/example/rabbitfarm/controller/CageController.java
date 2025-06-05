package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Cage;
import com.example.rabbitfarm.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cages")
public class CageController {

    private final CageService cageService;

    @Autowired
    public CageController(CageService cageService) {
        this.cageService = cageService;
    }

    @PostMapping
    public ResponseEntity<Cage> createCage(@RequestBody Cage cage) {
        Cage savedCage = cageService.saveCage(cage);
        return new ResponseEntity<>(savedCage, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cage>> getAllCages() {
        List<Cage> cages = cageService.getAllCages();
        return ResponseEntity.ok(cages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cage> getCageById(@PathVariable Long id) {
        Optional<Cage> cage = cageService.getCageById(id);
        return cage.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{cageNumber}")
    public ResponseEntity<Cage> getCageByCageNumber(@PathVariable String cageNumber) {
        Optional<Cage> cage = cageService.getCageByCageNumber(cageNumber);
        return cage.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cage> updateCage(@PathVariable Long id, @RequestBody Cage cageDetails) {
        try {
            Cage updatedCage = cageService.updateCage(id, cageDetails);
            return ResponseEntity.ok(updatedCage);
        } catch (RuntimeException e) { // Replace with more specific exception handling
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCage(@PathVariable Long id) {
        try {
            cageService.deleteCage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) { // Replace with more specific exception handling
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
