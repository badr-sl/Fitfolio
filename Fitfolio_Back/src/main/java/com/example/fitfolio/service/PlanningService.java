package com.example.fitfolio.service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.fitfolio.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fitfolio.dto.PlanningDto;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.ActiviteRepository;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.repo.UserRepository;


@Service
@Transactional
public class PlanningService {

    @Autowired
    private PlanningRepository PlanningRepository;
    @Autowired
    private UserRepository UserRepository;
    @Autowired
    private ActiviteRepository ActiviteRepository;


    public ResponseEntity<PlanningDto> addNewPlanning(Planning planning) {
        Planning existingPlanning = PlanningRepository.findByJour(planning.getJour());
        PlanningDto PlanningDto = new PlanningDto();
        if (existingPlanning != null) {

            planning.setId(existingPlanning.getId());
            PlanningDto.setPlanning(PlanningRepository.save(planning));

            PlanningDto.setMessage("Modification effectuer");
            PlanningDto.setPlanning(planning);

            return new ResponseEntity<>(PlanningDto, HttpStatus.OK);
        }

        PlanningDto.setPlanning(PlanningRepository.save(planning));
        PlanningDto.setMessage("Creation Effectuer ");
        return new ResponseEntity<>(PlanningDto, HttpStatus.CREATED);
    }

    public Planning FindPlanningByJour(String jour) {
        return PlanningRepository.findByJour(jour);
    }


    public List<Planning> listPlanning() {
        return PlanningRepository.findAll();
    }


    public ResponseEntity<String> deletePlanning(Long id) {
        if (PlanningRepository.existsById(id)) {
            PlanningRepository.deleteById(id); //
            return new ResponseEntity<>("Planning supprimé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Planning introuvable", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<PlanningDto> addRepasToPlanning(String jour, Long utilisateurId, List<Repas> repasList) {
        // Trouver l'utilisateur
        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) {
            System.out.println("Utilisateur non trouvé");
            PlanningDto errorDto = new PlanningDto();
            errorDto.setMessage("Utilisateur non trouvé");
            return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
        }

        // Vérifier si un planning existe déjà pour cet utilisateur à cette date
        Optional<Planning> optionalPlanning = PlanningRepository.findByJourAndUtilisateur(jour, utilisateur);

        Planning planning;

        if (optionalPlanning.isEmpty()) {
            // Si aucun planning n'existe, créer un nouveau planning
            System.out.println("Planning non trouvé pour l'utilisateur à cette date, création d'un nouveau planning");

            planning = new Planning();
            planning.setJour(jour);
            planning.setUtilisateur(utilisateur);
            planning.setRepass(new ArrayList<>(repasList)); // Initialiser avec les repas ajoutés
            planning.setActivites(new ArrayList<>());
            planning.setCode("J" + jour + "U" + utilisateurId); // Génération d'un code unique

            // Sauvegarder le nouveau planning
            planning = PlanningRepository.save(planning);
        } else {
            // Si le planning existe déjà, remplacer les repas existants
            System.out.println("Planning trouvé, remplacement des repas pour la journée");

            planning = optionalPlanning.get();

            // Supprimer les anciens repas et remplacer par les nouveaux
            planning.getRepass().clear(); // Supprimer tous les anciens repas
            planning.setRepass(new ArrayList<>(repasList)); // Ajouter les nouveaux repas

            // Sauvegarder le planning mis à jour
            planning = PlanningRepository.save(planning);
        }

        // Retourner une réponse avec le planning
        PlanningDto planningDto = new PlanningDto();
        planningDto.setPlanning(planning);
        planningDto.setMessage("Repas remplacés dans le planning avec succès");

        return new ResponseEntity<>(planningDto, HttpStatus.OK);
    }

    public ResponseEntity<PlanningDto> addActivitesToPlanning(String jour, Long utilisateurId, List<Activite> activiteList) {

        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) {
            PlanningDto errorDto = new PlanningDto();
            errorDto.setMessage("Utilisateur non trouvé");
            return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
        }


        Optional<Planning> optionalPlanning = PlanningRepository.findByJourAndUtilisateur(jour, utilisateur);

        Planning planning;
        if (optionalPlanning.isEmpty()) {
            // Create a new planning if it doesn't exist
            planning = new Planning();
            planning.setJour(jour);
            planning.setUtilisateur(utilisateur);
            planning.setRepass(new ArrayList<>());
            planning.setActivites(new ArrayList<>());
            planning.setCode("J" + jour + "U" + utilisateurId); // You can customize the code generation

            // Save the new planning
            planning = PlanningRepository.save(planning);
        } else {
            // Retrieve the existing planning
            planning = optionalPlanning.get();
        }

        // Validate and retrieve each Activite in the activiteList
        List<Activite> validActiviteList = new ArrayList<>();
        for (Activite activite : activiteList) {
            Optional<Activite> optionalActivite = ActiviteRepository.findById(activite.getId());
            if (optionalActivite.isPresent()) {
                validActiviteList.add(optionalActivite.get());
            } else {
                PlanningDto errorDto = new PlanningDto();
                errorDto.setMessage("Activite with ID " + activite.getId() + " not found");
                return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
            }
        }

        // Add valid activities to the planning
        planning.getActivites().addAll(validActiviteList);

        // Save the updated planning
        PlanningRepository.save(planning);

        // Create and return the DTO with a success message
        PlanningDto planningDto = new PlanningDto();
        planningDto.setPlanning(planning);
        planningDto.setMessage("Activités ajoutées au planning avec succès");

        return new ResponseEntity<>(planningDto, HttpStatus.OK);
    }

    public ResponseEntity<List<Activite>> fetchActivitesByJourAndUtilisateur(String jour, Long utilisateurId) {
        // Find the user
        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Fetch the planning
        Optional<Planning> optionalPlanning = PlanningRepository.findByJourAndUtilisateur(jour, utilisateur);

        if (optionalPlanning.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Extract activities
        Planning planning = optionalPlanning.get();
        List<Activite> activites = new ArrayList<>(planning.getActivites());

        return new ResponseEntity<>(activites, HttpStatus.OK);
    }


    public ResponseEntity<List<Repas>> fetchRepasByJourAndUtilisateur(String jour, Long utilisateurId) {
        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Planning> optionalPlanning = PlanningRepository.findByJourAndUtilisateur(jour, utilisateur);
        if (optionalPlanning.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Planning planning = optionalPlanning.get();
        List<Repas> repas = new ArrayList<>(planning.getRepass());
        return new ResponseEntity<>(repas, HttpStatus.OK);
    }

    public ResponseEntity<List<Activite>> fetchWeeklyActivities(Long utilisateurId, String currentDate) {
        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LocalDate current = LocalDate.parse(currentDate);
        LocalDate startOfWeek = current.with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String startOfWeekStr = startOfWeek.toString();
        String endOfWeekStr = endOfWeek.toString();
        List<Planning> weeklyPlannings = PlanningRepository.findByUtilisateurAndJourBetween(utilisateur, startOfWeekStr, endOfWeekStr);
        List<Activite> weeklyActivities = new ArrayList<>();
        for (Planning planning : weeklyPlannings) {
            weeklyActivities.addAll(planning.getActivites());
        }
        return new ResponseEntity<>(weeklyActivities, HttpStatus.OK);
    }

    public ResponseEntity<Double> calculateDailyCalories(Long utilisateurId, String jour) {

        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) {
            System.out.println("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Planning> optionalPlanning = PlanningRepository.findByJourAndUtilisateur(jour, utilisateur);
        if (optionalPlanning.isEmpty()) {
            System.out.println("Planning not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Planning planning = optionalPlanning.get();
        System.out.println("Number of Repas: " + planning.getRepass().size());
        System.out.println("Number of Activites: " + planning.getActivites().size());
        double totalCaloriesFromRepas = planning.getRepass()
                .stream()
                .filter(repas -> repas.getNbrcalories() != null)
                .mapToDouble(Repas::getNbrcalories)
                .sum();


        double totalCaloriesBurned = planning.getActivites()
                .stream()
                .filter(activite -> activite.getNbrcalories() != null)
                .mapToDouble(Activite::getNbrcalories)
                .sum();
        double netCalories = totalCaloriesFromRepas - totalCaloriesBurned;
        System.out.println("Total calories from Repas: " + totalCaloriesFromRepas);
        System.out.println("Total calories burned by Activities: " + totalCaloriesBurned);
        System.out.println("Net Calories: " + netCalories);
        return new ResponseEntity<>(netCalories, HttpStatus.OK);
    }

    public ResponseEntity<List<Repas>> fetchWeeklyRepas(Long utilisateurId, String startDate, int weekNumber, Optional<String> day) {

        Utilisateur utilisateur = UserRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LocalDate registrationStartDate = LocalDate.parse(startDate);
        LocalDate weekStartDate = registrationStartDate.plusWeeks(weekNumber - 1);
        LocalDate weekEndDate = weekStartDate.plusDays(6);


        if (day.isPresent()) {
            try {
                DayOfWeek desiredDay = DayOfWeek.valueOf(day.get().toUpperCase());
                weekStartDate = weekStartDate.with(desiredDay);
                weekEndDate = weekStartDate;
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
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


    public List<Planning> findPlanningsByUserId(Long utilisateurId) {
        return PlanningRepository.findByUtilisateurId(utilisateurId);
    }

    public List<ActiviteDTO1> fetchActivitiesByUtilisateur(Long utilisateurId) {
        List<Object[]> results = PlanningRepository.findActivitiesByUtilisateurOrderedByPaIdDesc(utilisateurId);

        return results.stream()
                .map(result -> new ActiviteDTO1(
                        result[0] instanceof Number ? ((Number) result[0]).longValue() : null, // ID
                        result[1] != null ? result[1].toString() : null,                     // Titre
                        result[2] != null ? result[2].toString() : null,                     // Description
                        result[3] != null ? result[3].toString() : null,                     // Categorie
                        result[4] instanceof Number ? ((Number) result[4]).doubleValue() : null, // NbrCalories
                        result[5] != null ? result[5].toString() : null,                     // TypeActivite
                        result[6] != null ? result[6].toString() : null,                     // Objectif
                        result[7] != null ? result[7].toString() : null,                     // Image
                        result[8] instanceof Number ? ((Number) result[8]).doubleValue() : null, // PointCardio
                        result[9] != null ? result[9].toString() : null                      // Speed
                ))
                .collect(Collectors.toList());
    }

}
