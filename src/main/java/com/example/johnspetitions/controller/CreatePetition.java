package com.example.johnspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreatePetition {

    @GetMapping("/createPetition")
    public String home() {
        return "create_petition";
    }
}