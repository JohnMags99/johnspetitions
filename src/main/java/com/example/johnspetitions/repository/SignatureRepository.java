package com.example.johnspetitions.repository;

import com.example.johnspetitions.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignatureRepository extends JpaRepository<Signature, Integer> {

    // Find all signatures for a given petition
    List<Signature> findByPetition_PetitionId(Integer petitionId);

    List<Signature> findTop5ByPetition_PetitionIdOrderBySignDateDesc(Integer petitionId);
}

