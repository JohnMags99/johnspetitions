package com.example.johnspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.entity.Signature;
import com.example.johnspetitions.repository.PetitionRepository;
import com.example.johnspetitions.repository.SignatureRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PetitionController {
    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;

    public PetitionController(PetitionRepository petitionRepository, SignatureRepository signatureRepository) {
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
    }

    @GetMapping("/search")
    public String searchPetitions(@RequestParam(value = "q", required = false) String query,
                                  Model model) {
        List<Petition> results = new ArrayList<>();
        Map<Integer, List<Signature>> signaturesByPetition = new HashMap<>();

        if (query != null && !query.isBlank()) {
            results = petitionRepository.findByTitleContainingIgnoreCase(query);

            for (Petition petition : results) {
                List<Signature> sigs = signatureRepository
                        .findTop5ByPetition_PetitionIdOrderBySignDateDesc(petition.getPetitionId());
                signaturesByPetition.put(petition.getPetitionId(), sigs);
            }
        }

        model.addAttribute("results", results);
        model.addAttribute("signaturesByPetition", signaturesByPetition);
        model.addAttribute("query", query);
        return "search_petitions";
    }

    @GetMapping("/petitions/{id}")
    public String viewPetition(@PathVariable("id") Integer id, org.springframework.ui.Model model) {
        Petition petition = petitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid petition Id:" + id));

        List<Signature> signatures = signatureRepository.findByPetition_PetitionId(id);

        model.addAttribute("petition", petition);
        model.addAttribute("signatures", signatures);
        return "view_petition"; // template name
    }
}
