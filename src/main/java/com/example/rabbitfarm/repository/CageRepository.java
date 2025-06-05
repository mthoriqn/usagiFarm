package com.example.rabbitfarm.repository;

import com.example.rabbitfarm.model.Cage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CageRepository extends JpaRepository<Cage, Long> {
    Optional<Cage> findByCageNumber(String cageNumber);
}
