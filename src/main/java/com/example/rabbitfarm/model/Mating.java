package com.example.rabbitfarm.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matings")
public class Mating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "male_rabbit_id", nullable = false)
    private Rabbit maleRabbit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "female_rabbit_id", nullable = false)
    private Rabbit femaleRabbit;

    @Column(name = "mating_date", nullable = false)
    private LocalDate matingDate;

    @Column(name = "expected_birth_date")
    private LocalDate expectedBirthDate;

    @Column(name = "actual_birth_date")
    private LocalDate actualBirthDate;

    @Column(name = "number_of_kits")
    private Integer numberOfKits;

    @Lob
    private String notes;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rabbit getMaleRabbit() {
        return maleRabbit;
    }

    public void setMaleRabbit(Rabbit maleRabbit) {
        this.maleRabbit = maleRabbit;
    }

    public Rabbit getFemaleRabbit() {
        return femaleRabbit;
    }

    public void setFemaleRabbit(Rabbit femaleRabbit) {
        this.femaleRabbit = femaleRabbit;
    }

    public LocalDate getMatingDate() {
        return matingDate;
    }

    public void setMatingDate(LocalDate matingDate) {
        this.matingDate = matingDate;
    }

    public LocalDate getExpectedBirthDate() {
        return expectedBirthDate;
    }

    public void setExpectedBirthDate(LocalDate expectedBirthDate) {
        this.expectedBirthDate = expectedBirthDate;
    }

    public LocalDate getActualBirthDate() {
        return actualBirthDate;
    }

    public void setActualBirthDate(LocalDate actualBirthDate) {
        this.actualBirthDate = actualBirthDate;
    }

    public Integer getNumberOfKits() {
        return numberOfKits;
    }

    public void setNumberOfKits(Integer numberOfKits) {
        this.numberOfKits = numberOfKits;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
