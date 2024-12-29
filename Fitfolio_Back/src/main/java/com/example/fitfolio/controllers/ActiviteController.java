package com.example.fitfolio.controllers;

import java.util.List;

import com.example.fitfolio.dto.ActiviteDto;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.service.ActiviteService;

@RestController
@RequestMapping("/fitfolio")
public class ActiviteController {
	
    //@Autowired  remplacer par constructeur
    public ActiviteService activiteService;    
    
    public ActiviteController(ActiviteService activiteService) {
		this.activiteService = activiteService;
	}

    @GetMapping(path = "/activites")
    @PostAuthorize("hasAuthority('USER')")
    public List<Activite> activites(@RequestParam(required = false) String objectif) {
        System.out.println("Fetching activities...");
        if (objectif != null && !objectif.isEmpty()) {
            System.out.println("Filtering by objectif: " + objectif);
            return activiteService.listActivitesByGoal(objectif);
        } else {
            return activiteService.listAllActivites();
        }
    }

    @PostMapping(path = "/addActivite")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public  ResponseEntity<ActiviteDto> saveActivite(@RequestBody Activite activite) throws JSONException {
    	System.out.println("entree yyyyyyyyyyyyyyyyyy");
    	return  activiteService.addNewActivite(activite); 
    }

    @DeleteMapping(path = "/deleteActivite/{id}")
    public ResponseEntity<String> deleteActivite(@PathVariable Long id) {
        System.out.println("Entrée dans deleteRepas avec id: " + id);
        return activiteService.deleteActivite(id);
    }
    
}