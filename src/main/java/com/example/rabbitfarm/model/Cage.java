package com.example.rabbitfarm.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cages")
public class Cage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cage_number", nullable = false, unique = true, length = 50)
    private String cageNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 100)
    private String location;

    @OneToMany(mappedBy = "cage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rabbit> rabbits;

    // Constructors
    public Cage() {
    }

    public Cage(String cageNumber, Integer capacity, String location) {
        this.cageNumber = cageNumber;
        this.capacity = capacity;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCageNumber() {
        return cageNumber;
    }

    public void setCageNumber(String cageNumber) {
        this.cageNumber = cageNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Rabbit> getRabbits() {
        return rabbits;
    }

    public void setRabbits(Set<Rabbit> rabbits) {
        this.rabbits = rabbits;
    }
}
