package com.example.fitfolio;

import com.example.fitfolio.controllers.RepasController;
import com.example.fitfolio.dto.RepasDto;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.service.RepasService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepasControllerTest {

    @Mock
    private RepasService repasService;

    @InjectMocks
    private RepasController repasController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListRepas() {
        // Arrange
        List<Repas> mockRepasList = Arrays.asList(new Repas(), new Repas());
        when(repasService.listRepas()).thenReturn(mockRepasList);

        // Act
        List<Repas> repasList = repasController.repass();

        // Assert
        assertNotNull(repasList);
        assertEquals(2, repasList.size());
        verify(repasService, times(1)).listRepas();
    }

    @Test
    void testSaveRepas() throws JSONException {
        // Arrange
        Repas mockRepas = new Repas();
        RepasDto mockRepasDto = new RepasDto();
        ResponseEntity<RepasDto> mockResponse = ResponseEntity.ok(mockRepasDto);
        when(repasService.addNewRepas(mockRepas)).thenReturn(mockResponse);

        // Act
        ResponseEntity<RepasDto> response = repasController.saveRepas(mockRepas);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(repasService, times(1)).addNewRepas(mockRepas);
    }

    @Test
    void testDeleteRepas() {
        // Arrange
        Long id = 1L;
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Deleted");
        when(repasService.deleteRepas(id)).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = repasController.deleteRepas(id);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(repasService, times(1)).deleteRepas(id);
    }

    @Test
    void testGetMidiRepas() {
        // Arrange
        List<Repas> mockRepasList = Arrays.asList(new Repas());
        when(repasService.findRepasByTyperepas("Midi")).thenReturn(mockRepasList);

        // Act
        List<Repas> repasList = repasController.getMidiRepas();

        // Assert
        assertNotNull(repasList);
        assertEquals(1, repasList.size());
        verify(repasService, times(1)).findRepasByTyperepas("Midi");
    }

    @Test
    void testGetMatinRepas() {
        // Arrange
        List<Repas> mockRepasList = Arrays.asList(new Repas());
        when(repasService.findRepasByTyperepas("Matin")).thenReturn(mockRepasList);

        // Act
        List<Repas> repasList = repasController.getMatinRepas();

        // Assert
        assertNotNull(repasList);
        assertEquals(1, repasList.size());
        verify(repasService, times(1)).findRepasByTyperepas("Matin");
    }
    @Test
    void testGetRepasByGoalNoContent() {
        // Arrange
        String objectif = "Weight Loss";
        when(repasService.listRepasByGoal(objectif)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Repas>> response = repasController.getRepasByGoal(objectif);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(repasService, times(1)).listRepasByGoal(objectif);
    }

    @Test
    void testGetRepasByGoalException() {
        // Arrange
        String objectif = "Weight Loss";
        when(repasService.listRepasByGoal(objectif)).thenThrow(new RuntimeException("Test Exception"));

        // Act
        ResponseEntity<List<Repas>> response = repasController.getRepasByGoal(objectif);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(repasService, times(1)).listRepasByGoal(objectif);
    }


    @Test
    void testGetNuitRepas() {
        // Arrange
        List<Repas> mockRepasList = Arrays.asList(new Repas());
        when(repasService.findRepasByTyperepas("Nuit")).thenReturn(mockRepasList);

        // Act
        List<Repas> repasList = repasController.getnuitRepas();

        // Assert
        assertNotNull(repasList);
        assertEquals(1, repasList.size());
        verify(repasService, times(1)).findRepasByTyperepas("Nuit");
    }

    @Test
    void testGetRepasByGoal() {
        // Arrange
        String objectif = "Weight Loss";
        List<Repas> mockRepasList = Arrays.asList(new Repas());
        when(repasService.listRepasByGoal(objectif)).thenReturn(mockRepasList);

        // Act
        ResponseEntity<List<Repas>> response = repasController.getRepasByGoal(objectif);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(repasService, times(1)).listRepasByGoal(objectif);
    }
}
