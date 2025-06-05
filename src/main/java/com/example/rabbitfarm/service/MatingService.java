package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Mating;
import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.Gender;
import com.example.rabbitfarm.repository.MatingRepository;
import com.example.rabbitfarm.repository.RabbitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatingService {

    private final MatingRepository matingRepository;
    private final RabbitRepository rabbitRepository;

    @Autowired
    public MatingService(MatingRepository matingRepository, RabbitRepository rabbitRepository) {
        this.matingRepository = matingRepository;
        this.rabbitRepository = rabbitRepository;
    }

    public Mating recordMating(Mating mating) {
        // Validate male rabbit
        Rabbit maleRabbit = rabbitRepository.findById(mating.getMaleRabbit().getId())
                .orElseThrow(() -> new RuntimeException("Male rabbit not found with id: " + mating.getMaleRabbit().getId()));
        if (maleRabbit.getGender() != Gender.MALE) {
            throw new RuntimeException("Rabbit with id " + maleRabbit.getId() + " is not male.");
        }

        // Validate female rabbit
        Rabbit femaleRabbit = rabbitRepository.findById(mating.getFemaleRabbit().getId())
                .orElseThrow(() -> new RuntimeException("Female rabbit not found with id: " + mating.getFemaleRabbit().getId()));
        if (femaleRabbit.getGender() != Gender.FEMALE) {
            throw new RuntimeException("Rabbit with id " + femaleRabbit.getId() + " is not female.");
        }

        mating.setMaleRabbit(maleRabbit);
        mating.setFemaleRabbit(femaleRabbit);
        // Potentially set expected birth date based on mating date (e.g., +31 days)
        // mating.setExpectedBirthDate(mating.getMatingDate().plusDays(31));
        return matingRepository.save(mating);
    }

    public List<Mating> getAllMatings() {
        return matingRepository.findAll();
    }

    public Optional<Mating> getMatingById(Long id) {
        return matingRepository.findById(id);
    }

    public Mating updateMating(Long id, Mating matingDetails) {
        Mating mating = matingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mating record not found with id: " + id));

        // Re-validate rabbits if they are changed
        if (matingDetails.getMaleRabbit() != null && matingDetails.getMaleRabbit().getId() != null) {
            Rabbit maleRabbit = rabbitRepository.findById(matingDetails.getMaleRabbit().getId())
                .orElseThrow(() -> new RuntimeException("Male rabbit not found with id: " + matingDetails.getMaleRabbit().getId()));
            if (maleRabbit.getGender() != Gender.MALE) {
                 throw new RuntimeException("Rabbit with id " + maleRabbit.getId() + " is not male.");
            }
            mating.setMaleRabbit(maleRabbit);
        }

        if (matingDetails.getFemaleRabbit() != null && matingDetails.getFemaleRabbit().getId() != null) {
            Rabbit femaleRabbit = rabbitRepository.findById(matingDetails.getFemaleRabbit().getId())
                .orElseThrow(() -> new RuntimeException("Female rabbit not found with id: " + matingDetails.getFemaleRabbit().getId()));
            if (femaleRabbit.getGender() != Gender.FEMALE) {
                 throw new RuntimeException("Rabbit with id " + femaleRabbit.getId() + " is not female.");
            }
            mating.setFemaleRabbit(femaleRabbit);
        }

        mating.setMatingDate(matingDetails.getMatingDate());
        mating.setExpectedBirthDate(matingDetails.getExpectedBirthDate());
        mating.setActualBirthDate(matingDetails.getActualBirthDate());
        mating.setNumberOfKits(matingDetails.getNumberOfKits());
        mating.setNotes(matingDetails.getNotes());
        return matingRepository.save(mating);
    }

    public void deleteMating(Long id) {
        if (!matingRepository.existsById(id)) {
            throw new RuntimeException("Mating record not found with id: " + id);
        }
        matingRepository.deleteById(id);
    }
}
