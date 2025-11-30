package com.example.johnspetitions.controller;

import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.entity.Signature;
import com.example.johnspetitions.repository.PetitionRepository;
import com.example.johnspetitions.repository.SignatureRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewAllPetitions {

    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;

    public ViewAllPetitions(PetitionRepository petitionRepository, SignatureRepository signatureRepository) {
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
    }

    @GetMapping("/allPetitions")
    public String listAllPetitions(Model model) {
        List<Petition> petitions = petitionRepository.findAll();

        Map<Integer, List<Signature>> recentSignaturesByPetition = new HashMap<>();
        for (Petition petition : petitions) {
            List<Signature> recent = signatureRepository
                    .findTop5ByPetition_PetitionIdOrderBySignDateDesc(petition.getPetitionId());
            recentSignaturesByPetition.put(petition.getPetitionId(), recent);
        }

        model.addAttribute("petitions", petitions);
        model.addAttribute("recentSignaturesByPetition", recentSignaturesByPetition);
        return "all_petitions";
    }
}
