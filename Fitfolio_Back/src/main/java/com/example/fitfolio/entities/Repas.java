package com.example.fitfolio.entities;

import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 
public class Repas  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;

    private String description;

    private String categorie;

    private Double nbrcalories;

    private String typerepas;

    private String objectif;

    private String image;


    public Repas() {}

    public Repas(Long id,String titre, String description, String categorie, Double nbrcalories, String typerepas, String objectif, String image) {
        this.id=id;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.nbrcalories = nbrcalories;
        this.typerepas = typerepas;
        this.objectif = objectif;
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getNbrcalories() {
        return nbrcalories;
    }

    public void setNbrcalories(Double nbrcalories) {
        this.nbrcalories = nbrcalories;
    }

    public String getTyperepas() {
        return typerepas;
    }

    public void setTyperepas(String typerepas) {
        this.typerepas = typerepas;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

