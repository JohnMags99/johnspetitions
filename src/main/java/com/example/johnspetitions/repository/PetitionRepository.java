package com.example.johnspetitions.repository;

import com.example.johnspetitions.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}

