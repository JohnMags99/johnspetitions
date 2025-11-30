package com.example.johnspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchPetitions {

    @GetMapping("/searchPetition")
    public String home() {
        return "search_petitions";
    }
}