package com.example.fitfolio.service;

import java.util.List;
import java.util.Optional;

import com.example.fitfolio.dto.ActiviteDto;
import com.example.fitfolio.entities.Planning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.repo.ActiviteRepository;
import com.example.fitfolio.repo.PlanningRepository;
@Service
@Transactional
public class ActiviteService {

    @Autowired
    private ActiviteRepository ActiviteRepository;

    @Autowired
    private PlanningRepository planningRepository;
    // Sauvegarde ou update Activite par titre
    public ResponseEntity<ActiviteDto> addNewActivite(Activite repas) {
    	System.out.println("entree addnewActivite one");
    System.out.println("entree addnewActivite"+repas);
        Activite existingActivite = ActiviteRepository.findByTitre(repas.getTitre());
        System.out.println("entree addnewActivite"+existingActivite);
        ActiviteDto ActiviteDto=new ActiviteDto();
        if (existingActivite != null) {
        	
        	repas.setId(existingActivite.getId());
        	ActiviteDto.setActivite(ActiviteRepository.save(repas) );
        	
        	ActiviteDto.setMessage("Modification effectuer");
        	ActiviteDto.setActivite(repas);
        	
            return new ResponseEntity<>(ActiviteDto, HttpStatus.OK); 
                                 }
        
        ActiviteDto.setActivite(ActiviteRepository.save(repas) );
    	ActiviteDto.setMessage("Creation Effectuer ");
        return new ResponseEntity<>(ActiviteDto, HttpStatus.CREATED);
    }


    public Activite chercheActivite(String titre) {
        return ActiviteRepository.findByTitre(titre); // Retourne l'utilisateur
    }


    public List<Activite> listAllActivites() {
        return ActiviteRepository.findAll();
    }

    // Fetch activities filtered by objectif
    public List<Activite> listActivitesByGoal(String objectif) {
        return ActiviteRepository.findByObjectif(objectif);
    }


    public ResponseEntity<String> deleteActivite(Long id) {
        // Vérifier si l'Activité existe
        Optional<Activite> activiteOptional = ActiviteRepository.findById(id);

        if (activiteOptional.isPresent()) {
            Activite activite = activiteOptional.get();

            // On récupère tous les Plannings qui contiennent cette Activité
            List<Planning> plannings = planningRepository.findAll();

            for (Planning planning : plannings) {
                // Si le Planning contient l'Activité, on la retire
                if (planning.getActivites().contains(activite)) {
                    planning.getActivites().remove(activite);
                    planningRepository.save(planning);
                }
            }

            // Supprimer l'Activité de la base de données
            ActiviteRepository.delete(activite);
            return new ResponseEntity<>("Activité supprimée avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Activité introuvable", HttpStatus.NOT_FOUND);
        }
    }

}



