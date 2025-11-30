package com.example.johnspetitions.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signatureId;

    @ManyToOne
    @JoinColumn(name = "petition_id", nullable = false)
    private Petition petition;

    private String signerName;
    private String signerEmail;
    private LocalDateTime signDate = LocalDateTime.now();

    public Long getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Long signatureId) {
        this.signatureId = signatureId;
    }

    public Petition getPetition() {
        return petition;
    }

    public void setPetitionId(Petition petition) {
        this.petition = petition;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerEmail() {
        return signerEmail;
    }

    public void setSignerEmail(String signerEmail) {
        this.signerEmail = signerEmail;
    }

    public LocalDateTime getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDateTime signDate) {
        this.signDate = signDate;
    }
}
