package com.example.fitfolio.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

   
    private String jour;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Repas> repass = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Activite> activites = new ArrayList<>();

    @ManyToOne
@JoinColumn(name = "utilisateur_id", nullable = false) // Foreign key column
private Utilisateur utilisateur;

    public Planning() {}

    public Planning(Long id, String code, String jour, Collection<Repas> repass, Collection<Activite> activites,
                    Utilisateur utilisateur) {
        this.id = id;
        this.code = code;
        this.jour = jour;
        this.repass = repass;
        this.activites = activites;
        this.utilisateur = utilisateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public Collection<Repas> getRepass() {
        return repass;
    }

    public void setRepass(Collection<Repas> repass) {
        this.repass = repass;
    }

    public Collection<Activite> getActivites() {
        return activites;
    }

    public void setActivites(Collection<Activite> activites) {
        this.activites = activites;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
