package com.example.johnspetitions.repository;

import com.example.johnspetitions.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetitionRepository extends JpaRepository<Petition, Integer> {
    List<Petition> findByTitleContainingIgnoreCase(String title);
}

