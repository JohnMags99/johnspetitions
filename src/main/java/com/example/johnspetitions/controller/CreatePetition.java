package com.example.johnspetitions.controller;

import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.repository.PetitionRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CreatePetition {

    private final PetitionRepository petitionRepository;

    public CreatePetition(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    @GetMapping("/createPetition")
    public String showForm(Model model) {
        model.addAttribute("petition", new Petition());
        return "create_petition";
    }

    @PostMapping("/create")
    public String submitForm(@ModelAttribute Petition petition) {
        petitionRepository.save(petition);
        return "redirect:/allPetitions";
    }
}