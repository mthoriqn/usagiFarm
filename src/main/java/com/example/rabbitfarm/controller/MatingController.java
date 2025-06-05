package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Mating;
import com.example.rabbitfarm.service.MatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matings")
public class MatingController {

    private final MatingService matingService;

    @Autowired
    public MatingController(MatingService matingService) {
        this.matingService = matingService;
    }

    @PostMapping
    public ResponseEntity<?> createMating(@RequestBody Mating mating) {
        try {
            Mating recordedMating = matingService.recordMating(mating);
            return new ResponseEntity<>(recordedMating, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Mating>> getAllMatings() {
        List<Mating> matings = matingService.getAllMatings();
        return ResponseEntity.ok(matings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mating> getMatingById(@PathVariable Long id) {
        Optional<Mating> mating = matingService.getMatingById(id);
        return mating.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMating(@PathVariable Long id, @RequestBody Mating matingDetails) {
        try {
            Mating updatedMating = matingService.updateMating(id, matingDetails);
            return ResponseEntity.ok(updatedMating);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMating(@PathVariable Long id) {
        try {
            matingService.deleteMating(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
