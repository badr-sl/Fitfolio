package com.example.fitfolio;

import com.example.fitfolio.controllers.PlanningController;
import com.example.fitfolio.dto.ActiviteDTO1;
import com.example.fitfolio.dto.PlanningDto;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.PlanningService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanningControllerTest {

    @Mock
    private PlanningService planningService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlanningRepository planningRepository;

    @InjectMocks
    private PlanningController planningController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Plannings List Tests
    @Test
    void plannings_ReturnsListSuccessfully() {
        List<Planning> mockPlannings = Arrays.asList(new Planning());
        when(planningService.listPlanning()).thenReturn(mockPlannings);

        List<Planning> result = planningController.plannings();

        assertNotNull(result);
        assertEquals(mockPlannings.size(), result.size());
    }

    @Test
    void plannings_ReturnsEmptyList() {
        when(planningService.listPlanning()).thenReturn(Collections.emptyList());

        List<Planning> result = planningController.plannings();

        assertTrue(result.isEmpty());
    }

    // Save Planning Tests
    @Test
    void savePlanning_Success() throws JSONException {
        Planning planning = new Planning();
        PlanningDto planningDto = new PlanningDto();
        when(planningService.addNewPlanning(any())).thenReturn(ResponseEntity.ok(planningDto));

        ResponseEntity<PlanningDto> response = planningController.savePlanning(planning);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void savePlanning_BadRequest() throws JSONException {
        Planning planning = new Planning();
        when(planningService.addNewPlanning(any())).thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<PlanningDto> response = planningController.savePlanning(planning);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Delete Planning Tests
    @Test
    void deletePlanning_Success() {
        when(planningService.deletePlanning(1L)).thenReturn(ResponseEntity.ok("Deleted"));

        ResponseEntity<String> response = planningController.deleteRepas(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted", response.getBody());
    }

    @Test
    void deletePlanning_NotFound() {
        when(planningService.deletePlanning(1L)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<String> response = planningController.deleteRepas(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Get Plannings By User Tests
    @Test
    void getPlanningsByUserId_Success() {
        List<Planning> plannings = Arrays.asList(new Planning());
        when(planningService.findPlanningsByUserId(1L)).thenReturn(plannings);

        ResponseEntity<List<Planning>> response = planningController.getPlanningsByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getPlanningsByUserId_NoContent() {
        when(planningService.findPlanningsByUserId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Planning>> response = planningController.getPlanningsByUserId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Add Repas To Planning Tests
    @Test
    void addRepasToPlanning_Success() {
        List<Repas> repasList = Arrays.asList(new Repas());
        PlanningDto planningDto = new PlanningDto();
        when(planningService.addRepasToPlanning(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(planningDto));

        ResponseEntity<PlanningDto> response = planningController
                .addRepasToPlanning("2024-01-01", 1L, repasList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void addRepasToPlanning_BadRequest() {
        List<Repas> repasList = Arrays.asList(new Repas());
        when(planningService.addRepasToPlanning(any(), any(), any()))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<PlanningDto> response = planningController
                .addRepasToPlanning("invalid-date", 1L, repasList);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Add Activities To Planning Tests
    @Test
    void addActivitesToPlanning_Success() {
        List<Activite> activiteList = Arrays.asList(new Activite());
        PlanningDto planningDto = new PlanningDto();
        when(planningService.addActivitesToPlanning(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(planningDto));

        ResponseEntity<PlanningDto> response = planningController
                .addActivitesToPlanning("2024-01-01", 1L, activiteList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Get Activities Tests
    @Test
    void getActivitesByJourAndUtilisateur_Success() {
        List<Activite> activities = Arrays.asList(new Activite());
        when(planningService.fetchActivitesByJourAndUtilisateur(any(), any()))
                .thenReturn(ResponseEntity.ok(activities));

        ResponseEntity<List<Activite>> response = planningController
                .getActivitesByJourAndUtilisateur("2024-01-01", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    // Weekly Repas Tests
    @Test
    void fetchWeeklyRepas_Success() {
        Utilisateur user = new Utilisateur();
        List<Planning> plannings = Arrays.asList(new Planning());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planningRepository.findByUtilisateurAndJourBetween(any(), any(), any()))
                .thenReturn(plannings);

        ResponseEntity<List<Repas>> response = planningController
                .fetchWeeklyRepas(1L, "2024-01-01", 1, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void fetchWeeklyRepas_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<List<Repas>> response = planningController
                .fetchWeeklyRepas(1L, "2024-01-01", 1, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    // Calculate Calories Tests
    @Test
    void calculateDailyCalories_Success() {
        when(planningService.calculateDailyCalories(1L, "2024-01-01"))
                .thenReturn(ResponseEntity.ok(2000.0));

        ResponseEntity<Double> response = planningController
                .calculateDailyCalories(1L, "2024-01-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2000.0, response.getBody());
    }

    // Weekly Activities Tests
    @Test
    void getWeeklyActivities_Success() {
        List<Activite> activities = Arrays.asList(new Activite());
        when(planningService.fetchWeeklyActivities(any(), any()))
                .thenReturn(ResponseEntity.ok(activities));

        ResponseEntity<List<Activite>> response = planningController
                .getWeeklyActivities(1L, "2024-01-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    // Get All Activities Tests
    @Test
    void getAllActivities_Success() {
        List<ActiviteDTO1> activities = Arrays.asList(new ActiviteDTO1());
        when(planningService.fetchActivitiesByUtilisateur(1L))
                .thenReturn(activities);

        ResponseEntity<List<ActiviteDTO1>> response = planningController
                .getAllActivities(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAllActivities_NoContent() {
        when(planningService.fetchActivitiesByUtilisateur(1L))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<ActiviteDTO1>> response = planningController
                .getAllActivities(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Exception Tests
    @Test
    void fetchActivitiesByUtilisateur_HandlesException() {
        when(planningService.fetchActivitiesByUtilisateur(1L))
                .thenThrow(new RuntimeException());

        ResponseEntity<List<ActiviteDTO1>> response = planningController
                .fetchActivitiesByUtilisateur(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}