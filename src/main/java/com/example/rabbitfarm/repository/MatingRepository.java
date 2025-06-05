package com.example.rabbitfarm.repository;

import com.example.rabbitfarm.model.Mating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatingRepository extends JpaRepository<Mating, Long> {
    List<Mating> findByMaleRabbitId(Long maleRabbitId);
    List<Mating> findByFemaleRabbitId(Long femaleRabbitId);
    List<Mating> findByExpectedBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
