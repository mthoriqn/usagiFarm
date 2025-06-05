package com.example.rabbitfarm.repository;

import com.example.rabbitfarm.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
    List<Sale> findByRabbitId(Long rabbitId);
}
