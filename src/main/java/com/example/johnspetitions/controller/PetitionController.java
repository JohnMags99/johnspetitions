package com.example.johnspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.entity.Signature;
import com.example.johnspetitions.repository.PetitionRepository;
import com.example.johnspetitions.repository.SignatureRepository;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/createPetition")
    public String createForm(Model model) {
        model.addAttribute("petition", new Petition());
        return "create_petition";
    }

    @PostMapping("/create")
    public String submitForm(@ModelAttribute Petition petition) {
        try {
            petitionRepository.save(petition);
            return "redirect:/petitions/"+petition.getPetitionId();
        } catch (Exception e) {
            return "redirect:/createPetition?error=failed_to_save";
        }
    }

    @GetMapping("/searchPetition")
    public String searchForm() {
        return "search_petitions";
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
