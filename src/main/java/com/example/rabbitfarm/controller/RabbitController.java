package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.RabbitStatus;
import com.example.rabbitfarm.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rabbits")
public class RabbitController {

    private final RabbitService rabbitService;

    @Autowired
    public RabbitController(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    @PostMapping
    public ResponseEntity<Rabbit> createRabbit(@RequestBody Rabbit rabbit) {
        try {
            Rabbit savedRabbit = rabbitService.saveRabbit(rabbit);
            return new ResponseEntity<>(savedRabbit, HttpStatus.CREATED);
        } catch (RuntimeException e) {
             return ResponseEntity.badRequest().body(null); // Or a custom error object
        }
    }

    @GetMapping
    public ResponseEntity<List<Rabbit>> getAllRabbits(
            @RequestParam(required = false) RabbitStatus status,
            @RequestParam(required = false) Long cageId) {
        List<Rabbit> rabbits;
        if (status != null) {
            rabbits = rabbitService.getRabbitsByStatus(status);
        } else if (cageId != null) {
            rabbits = rabbitService.getRabbitsByCageId(cageId);
        }
        else {
            rabbits = rabbitService.getAllRabbits();
        }
        return ResponseEntity.ok(rabbits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rabbit> getRabbitById(@PathVariable Long id) {
        Optional<Rabbit> rabbit = rabbitService.getRabbitById(id);
        return rabbit.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rabbit> updateRabbit(@PathVariable Long id, @RequestBody Rabbit rabbitDetails) {
        try {
            Rabbit updatedRabbit = rabbitService.updateRabbit(id, rabbitDetails);
            return ResponseEntity.ok(updatedRabbit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or a custom error object
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRabbit(@PathVariable Long id) {
        try {
            rabbitService.deleteRabbit(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
