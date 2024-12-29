package com.example.fitfolio.controllers;

import java.util.List;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fitfolio.dto.RepasDto;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.service.RepasService;

@RestController
@RequestMapping("/fitfolio")
public class RepasController {
	
    public RepasService repasService;
    
    public RepasController(RepasService repasService) {
		this.repasService = repasService;
	}
   
	@GetMapping(path = "/repas")
  
    public List<Repas> repass() {
		
        return repasService.listRepas(); 
    }

    @PostMapping(path = "/addRepas")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public  ResponseEntity<RepasDto> saveRepas(@RequestBody Repas repas) throws JSONException { 
    	System.out.println("entree yyyyyyyyyyyyyyyyyy");
    	return  repasService.addNewRepas(repas); 
    }

    @DeleteMapping(path = "/deleteRepas/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteRepas(@PathVariable Long id) {
        System.out.println("Entrée dans deleteRepas avec id: " + id);
        return repasService.deleteRepas(id);
    }

    @GetMapping(path = "/repas/midi")
    public List<Repas> getMidiRepas() {
        return repasService.findRepasByTyperepas("Midi");
    }

    @GetMapping(path = "/repas/matin")
    public List<Repas> getMatinRepas() {
        return repasService.findRepasByTyperepas("Matin");
    }


  @GetMapping(path = "/repas/nuit")
public List<Repas> getnuitRepas() {
    return repasService.findRepasByTyperepas("Nuit");
}


    @GetMapping("/repas/objectif")
    public ResponseEntity<List<Repas>> getRepasByGoal(@RequestParam String objectif) {
        try {
            List<Repas> repasList = repasService.listRepasByGoal(objectif);
            if (repasList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(repasList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


