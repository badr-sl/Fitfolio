package com.example.fitfolio.repo;

import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Utilisateur;


import java.util.List;

import java.util.Optional;

import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanningRepository extends JpaRepository<Planning, Long> {

    Planning findByJour(String jour);
    List<Planning> findByUtilisateurId(Long utilisateurId);

    Optional<Planning> findByJourAndUtilisateur(String jour, Utilisateur utilisateur);

    @Query("SELECT p FROM Planning p WHERE p.utilisateur = :utilisateur AND p.jour BETWEEN :startDate AND :endDate")
    List<Planning> findByUtilisateurAndJourBetween(
            @Param("utilisateur") Utilisateur utilisateur,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);



    @Query(value = """
SELECT 
    a.id, 
    a.titre, 
    a.description, 
    a.categorie, 
    a.nbrcalories, 
    a.typeactivite, 
    a.objectif, 
    a.image, 
    a.pointcardio, 
    a.speed
FROM planning_activites pa
JOIN activite a ON pa.activites_id = a.id
JOIN planning p ON pa.planning_id = p.id
WHERE p.utilisateur_id = :utilisateurId
ORDER BY pa.id DESC
""", nativeQuery = true)
    List<Object[]> findActivitiesByUtilisateurOrderedByPaIdDesc(@Param("utilisateurId") Long utilisateurId);

}
