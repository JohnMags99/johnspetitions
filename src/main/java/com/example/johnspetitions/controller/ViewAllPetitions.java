package com.example.johnspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewAllPetitions {

    @GetMapping("/allPetitions")
    public String home() {
        return "all_petitions";
    }
}
