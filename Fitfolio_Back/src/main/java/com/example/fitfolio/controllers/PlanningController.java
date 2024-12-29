package com.example.fitfolio.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.fitfolio.dto.ActiviteDTO1;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitfolio.dto.PlanningDto;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.*;
import com.example.fitfolio.service.PlanningService;
import com.example.fitfolio.repo.*;

@RestController
@RequestMapping("/fitfolio")
public class PlanningController {

    @Autowired
    public PlanningService planningService;
    @Autowired
    public UserRepository UserRepository;

    @Autowired
    public PlanningRepository  PlanningRepository;

    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    @GetMapping(path = "/plannings")
    @PostAuthorize("hasAuthority('USER')")
    public List<Planning> plannings() {

        return planningService.listPlanning();
    }

    @PostMapping(path = "/addPlanning")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public  ResponseEntity<PlanningDto> savePlanning(@RequestBody Planning planning) throws JSONException {

        return  planningService.addNewPlanning(planning);
    }

    @DeleteMapping(path = "/deletePlanning/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteRepas(@PathVariable Long id) {
        System.out.println("Entrée dans deleteRepas avec id: " + id);
        return planningService.deletePlanning(id);
    }
    @GetMapping("/allplanning/{id}")
    public ResponseEntity<List<Planning>> getPlanningsByUserId(@PathVariable("id") Long utilisateurId) {
        List<Planning> plannings = planningService.findPlanningsByUserId(utilisateurId);

        if (plannings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(plannings, HttpStatus.OK);
    }



    @PostMapping(path = "/planning/addRepasToPlanning")
    public ResponseEntity<PlanningDto> addRepasToPlanning(
            @RequestParam("jour") String jour,
            @RequestParam("utilisateurId") Long utilisateurId,
            @RequestBody List<Repas> repasList) {


        return planningService.addRepasToPlanning(jour, utilisateurId, repasList);
    }

    @PostMapping(path = "/planning/addActivitesToPlanning")
    public ResponseEntity<PlanningDto> addActivitesToPlanning(
            @RequestParam("jour") String jour,
            @RequestParam("utilisateurId") Long utilisateurId,
            @RequestBody List<Activite> activiteList) {

        return planningService.addActivitesToPlanning(jour, utilisateurId, activiteList);
    }

    @GetMapping("/planning/activites")
    public ResponseEntity<List<Activite>> getActivitesByJourAndUtilisateur(
            @RequestParam("jour") String jour,
            @RequestParam("utilisateurId") Long utilisateurId) {

        return planningService.fetchActivitesByJourAndUtilisateur(jour, utilisateurId);
    }


    @GetMapping("/planning/repas")
    public ResponseEntity<List<Repas>> getRepasByJourAndUtilisateur(
            @RequestParam("jour") String jour,
            @RequestParam("utilisateurId") Long utilisateurId) {

        return planningService.fetchRepasByJourAndUtilisateur(jour, utilisateurId);
    }

    @GetMapping("/planning/weeklyActivities")
    public ResponseEntity<List<Activite>> getWeeklyActivities(
            @RequestParam("utilisateurId") Long utilisateurId,
            @RequestParam("currentDate") String currentDate) {

        return planningService.fetchWeeklyActivities(utilisateurId, currentDate);
    }

    @GetMapping("/planning/calculateCalories")
    public ResponseEntity<Double> calculateDailyCalories(
            @RequestParam("utilisateurId") Long utilisateurId,
            @RequestParam("jour") String jour) {
        return planningService.calculateDailyCalories(utilisateurId, jour);
    }


    @GetMapping("/weekly-repas")
    public ResponseEntity<List<Repas>> fetchWeeklyRepas(
            @RequestParam Long utilisateurId,    // User ID
            @RequestParam String startDate,      // Start date of registration (yyyy-MM-dd)
            @RequestParam int weekNumber,        // Week number to fetch (e.g., 2 for week 2)
            @RequestParam(required = false) String day) { // Optional day like 'lundi'

        // Find the user by ID
        Optional<Utilisateur> optionalUtilisateur = UserRepository.findById(utilisateurId);
        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Utilisateur utilisateur = optionalUtilisateur.get();

        // Parse the registration start date and calculate the date range
        LocalDate registrationStartDate;

        registrationStartDate = LocalDate.parse(startDate);


        LocalDate weekStartDate = registrationStartDate.plusWeeks(weekNumber - 1);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // Adjust for a specific day if provided
        if (day != null) {
            try {
                DayOfWeek desiredDay = DayOfWeek.valueOf(day.toUpperCase());
                weekStartDate = weekStartDate.with(desiredDay);
                weekEndDate = weekStartDate; // Same date for start and end
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid day input
            }
        }

        // Fetch plannings within the calculated date range
        List<Planning> weeklyPlannings = PlanningRepository.findByUtilisateurAndJourBetween(
                utilisateur,
                weekStartDate.toString(),
                weekEndDate.toString()
        );


        List<Repas> weeklyRepas = weeklyPlannings.stream()
                .flatMap(planning -> planning.getRepass().stream())
                .collect(Collectors.toList());

        return new ResponseEntity<>(weeklyRepas, HttpStatus.OK);
    }


    @GetMapping("/{utilisateurId}")
    public ResponseEntity<List<ActiviteDTO1>> fetchActivitiesByUtilisateur(@PathVariable Long utilisateurId) {
        try {
            List<ActiviteDTO1> activities = planningService.fetchActivitiesByUtilisateur(utilisateurId);

            if (activities.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/AllActivities/{utilisateurId}")
    public ResponseEntity<List<ActiviteDTO1>> getAllActivities(
            @PathVariable Long utilisateurId) {
        List<ActiviteDTO1> activities = planningService.fetchActivitiesByUtilisateur(utilisateurId);

        if (activities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no content
        }

        return new ResponseEntity<>(activities, HttpStatus.OK); // Return 200 with data
    }
}