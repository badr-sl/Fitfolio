package com.example.fitfolio.entities;

import jakarta.persistence.*;
@Entity
@SqlResultSetMapping(
        name = "ActiviteMapping",
        entities = @EntityResult(entityClass = Activite.class)
)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 
public class Activite  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;

    private String description;

    private String categorie;

    private Double nbrcalories;

    private String typeactivite;

    private String objectif;

    private String image;
    private Double pointcardio ;
    private String speed ;

    
  


    public Activite(Long id, String titre, String description, String categorie, Double nbrcalories,
            String typeactivite, String objectif, String image, Double pointcardio, String speed) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.nbrcalories = nbrcalories;
        this.typeactivite = typeactivite;
        this.objectif = objectif;
        this.image = image;
        this.pointcardio = pointcardio;
        this.speed = speed;
    }



    public Double getPointcardio() {
        return pointcardio;
    }



    public void setPointcardio(Double pointcardio) {
        this.pointcardio = pointcardio;
    }



    public String getSpeed() {
        return speed;
    }



    public void setSpeed(String speed) {
        this.speed = speed;
    }



    public Activite() {}

  

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

    public String getTypeactivite() {
        return typeactivite;
    }

    public void setTypeactivite(String typeactivite) {
        this.typeactivite = typeactivite;
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

