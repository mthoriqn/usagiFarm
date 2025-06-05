package com.example.rabbitfarm.repository;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.RabbitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RabbitRepository extends JpaRepository<Rabbit, Long> {
    List<Rabbit> findByStatus(RabbitStatus status);
    List<Rabbit> findByBreed(String breed);
    List<Rabbit> findByCageId(Long cageId);
}
