package com.example.rabbitfarm.repository;

import com.example.rabbitfarm.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByType(String type);
    List<Feed> findByExpiryDateBefore(LocalDate date);
}
