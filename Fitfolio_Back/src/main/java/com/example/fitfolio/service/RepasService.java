package com.example.fitfolio.service;

import java.util.List;
import java.util.Optional;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.repo.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.fitfolio.dto.RepasDto;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.repo.RepasRepository;

@Service
@Transactional
public class RepasService {

    @Autowired
    private RepasRepository RepasRepository;
    @Autowired
    private PlanningRepository planningRepository;

    public ResponseEntity<RepasDto> addNewRepas(Repas repas) {
    	System.out.println("entree addnewRepas one");
    System.out.println("entree addnewRepas"+repas);
        Repas existingRepas = RepasRepository.findByTitre(repas.getTitre());
        System.out.println("entree addnewRepas"+existingRepas);
        RepasDto RepasDto=new RepasDto();
        if (existingRepas != null) {
        	
        	repas.setId(existingRepas.getId());
        	RepasDto.setRepas(RepasRepository.save(repas) );
        	
        	RepasDto.setMessage("Modification effectuer");
        	RepasDto.setRepas(repas);
        	
            return new ResponseEntity<>(RepasDto, HttpStatus.OK); 
                                 }

        RepasDto.setRepas(RepasRepository.save(repas) );
    	RepasDto.setMessage("Creation Effectuer ");
        return new ResponseEntity<>(RepasDto, HttpStatus.CREATED);
    }
    
    public Repas findRepas(String titre) {
        return RepasRepository.findByTitre(titre); 
    }

   
    public List<Repas> listRepas() {
        return RepasRepository.findAll(); 
    }

    public ResponseEntity<String> deleteRepas(Long id) {
      
        Optional<Repas> repasOptional = RepasRepository.findById(id);

        if (repasOptional.isPresent()) {
            Repas repas = repasOptional.get();

           
            List<Planning> plannings = planningRepository.findAll(); 
            for (Planning planning : plannings) {
         
                if (planning.getRepass().contains(repas)) {
                    planning.getRepass().remove(repas);
                    planningRepository.save(planning); 
                }
            }

          
            RepasRepository.delete(repas);
            return new ResponseEntity<>("Repas supprimé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Repas introuvable", HttpStatus.NOT_FOUND);
        }
    }


    public List<Repas> findRepasByTyperepas(String typerepas) {
        return RepasRepository.findByTyperepas(typerepas);
    }

    public List<Repas> listRepasByGoal(String objectif) {
        return RepasRepository.findByObjectif(objectif);
    }

}
