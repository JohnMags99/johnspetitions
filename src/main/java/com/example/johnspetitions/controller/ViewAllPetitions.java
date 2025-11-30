package com.example.johnspetitions.controller;

import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.repository.PetitionRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewAllPetitions {

    private final PetitionRepository petitionRepository;

    public ViewAllPetitions(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    @GetMapping("/allPetitions")
    public String listAllPetitions(Model model) {
        List<Petition> petitions = petitionRepository.findAll();
        model.addAttribute("petitions", petitions);
        return "all_petitions";
    }
}
