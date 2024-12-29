package com.example.fitfolio.repo;

import java.util.List;

import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Repas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepasRepository extends JpaRepository<Repas, Long> {
    Repas findByTitre(String title);
    List<Repas> findByTyperepas(String typerepas); // New method for typerepas
    List<Repas> findByObjectif(String objectif);
}
