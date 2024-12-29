package com.example.fitfolio;

import com.example.fitfolio.controllers.ActiviteController;
import com.example.fitfolio.dto.ActiviteDto;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.service.ActiviteService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActiviteControllerTest {

    @Mock
    private ActiviteService activiteService;

    @InjectMocks
    private ActiviteController activiteController;

    public ActiviteControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllActivites() {
        // Arrange
        List<Activite> mockActivites = Arrays.asList(new Activite(), new Activite());
        when(activiteService.listAllActivites()).thenReturn(mockActivites);

        // Act
        List<Activite> activites = activiteController.activites(null);

        // Assert
        assertNotNull(activites);
        assertEquals(2, activites.size());
        verify(activiteService, times(1)).listAllActivites();
    }

    @Test
    void testListActivitesByGoal() {
        // Arrange
        String objectif = "weight loss";
        List<Activite> mockActivites = Arrays.asList(new Activite());
        when(activiteService.listActivitesByGoal(objectif)).thenReturn(mockActivites);

        // Act
        List<Activite> activites = activiteController.activites(objectif);

        // Assert
        assertNotNull(activites);
        assertEquals(1, activites.size());
        verify(activiteService, times(1)).listActivitesByGoal(objectif);
    }

    @Test
    void testSaveActivite() throws JSONException {
        // Arrange
        Activite mockActivite = new Activite();
        ActiviteDto mockActiviteDto = new ActiviteDto();
        ResponseEntity<ActiviteDto> mockResponse = ResponseEntity.ok(mockActiviteDto);
        when(activiteService.addNewActivite(mockActivite)).thenReturn(mockResponse);

        // Act
        ResponseEntity<ActiviteDto> response = activiteController.saveActivite(mockActivite);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(activiteService, times(1)).addNewActivite(mockActivite);
    }

    @Test
    void testDeleteActivite() {
        // Arrange
        Long id = 1L;
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Deleted");
        when(activiteService.deleteActivite(id)).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = activiteController.deleteActivite(id);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(activiteService, times(1)).deleteActivite(id);
    }

    @Test
    void testSaveActiviteLogs() throws JSONException {
        // Arrange
        Activite mockActivite = new Activite();
        when(activiteService.addNewActivite(mockActivite)).thenReturn(ResponseEntity.ok(new ActiviteDto()));

        // Act
        ResponseEntity<ActiviteDto> response = activiteController.saveActivite(mockActivite);

        // Assert
        assertNotNull(response);
        verify(activiteService, times(1)).addNewActivite(mockActivite);
    }

    @Test
    void testDeleteActiviteLogs() {
        // Arrange
        Long id = 1L;
        when(activiteService.deleteActivite(id)).thenReturn(ResponseEntity.ok("Deleted"));

        // Act
        ResponseEntity<String> response = activiteController.deleteActivite(id);

        // Assert
        assertNotNull(response);
        verify(activiteService, times(1)).deleteActivite(id);
    }

    @Test
    void testListAllActivitesLogs() {
        // Arrange
        when(activiteService.listAllActivites()).thenReturn(Arrays.asList(new Activite(), new Activite()));

        // Act
        List<Activite> activites = activiteController.activites(null);

        // Assert
        assertNotNull(activites);
        verify(activiteService, times(1)).listAllActivites();
    }

    @Test
    void testListActivitesByGoalLogs() {
        // Arrange
        String objectif = "goal";
        when(activiteService.listActivitesByGoal(objectif)).thenReturn(Arrays.asList(new Activite()));

        // Act
        List<Activite> activites = activiteController.activites(objectif);

        // Assert
        assertNotNull(activites);
        verify(activiteService, times(1)).listActivitesByGoal(objectif);
    }
}
