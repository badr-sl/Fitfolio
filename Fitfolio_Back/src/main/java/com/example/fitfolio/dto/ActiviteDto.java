package com.example.fitfolio.dto;
import com.example.fitfolio.entities.*;

public class ActiviteDto {
    private String message ;
    private Activite activite;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Activite getActivite() {
        return activite;
    }
    public void setActivite(Activite activite) {
        this.activite = activite;
    }

}
