package com.example.johnspetitions.repository;

import com.example.johnspetitions.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SignatureRepository extends JpaRepository<Signature, Long> {

    // Find all signatures for a given petition
    List<Signature> findByPetition_PetitionId(Long petitionId);

    List<Signature> findTop5ByPetition_PetitionIdOrderBySignDateDesc(Long petitionId);
}

