package com.example.johnspetitions;

import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class JohnspetitionsApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private PetitionRepository petitionRepository;

    @Test
    void petitionRepositoryLoads() {
        assertNotNull(petitionRepository);
    }

    @Test
    void testPetitionSaveAndFind() {
        Petition petition = new Petition();
        petition.setTitle("Save the Park");
        petition.setDescription("Protect local green space");

        Petition saved = petitionRepository.save(petition);

        assertTrue(petitionRepository.findById(saved.getPetitionId()).isPresent());
    }
}
