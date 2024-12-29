package com.example.fitfolio.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fitfolio.entities.Activite;

import java.util.List;

public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    Activite findByTitre(String title); // Existing method

    // New method to find activities by objectif
    List<Activite> findByObjectif(String objectif);
}