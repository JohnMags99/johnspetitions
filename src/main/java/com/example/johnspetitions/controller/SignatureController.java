package com.example.johnspetitions.controller;

import com.example.johnspetitions.entity.Petition;
import com.example.johnspetitions.entity.Signature;
import com.example.johnspetitions.repository.PetitionRepository;
import com.example.johnspetitions.repository.SignatureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SignatureController {

    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;

    public SignatureController(PetitionRepository petitionRepository, SignatureRepository signatureRepository) {
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
    }

    @PostMapping("/sign")
    @ResponseBody
    public ResponseEntity<String> signPetition(@RequestParam Long petitionId,
                                               @RequestParam String signerName,
                                               @RequestParam String signerEmail) {
        try {
            Petition petition = petitionRepository.findById(petitionId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid petition ID"));

            Signature signature = new Signature();
            signature.setSignerName(signerName);
            signature.setSignerEmail(signerEmail);

            signature.setPetition(petition);

            signatureRepository.save(signature);

            return ResponseEntity.ok("Signature saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving signature");
        }
    }

}

