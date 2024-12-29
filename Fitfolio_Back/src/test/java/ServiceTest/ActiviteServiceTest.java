package ServiceTest;

import com.example.fitfolio.dto.ActiviteDto;
import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.repo.ActiviteRepository;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.service.ActiviteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActiviteServiceTest {

    @InjectMocks
    private ActiviteService activiteService;

    @Mock
    private ActiviteRepository activiteRepository;

    @Mock
    private PlanningRepository planningRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewActivite_Create() {
        Activite activite = new Activite();
        activite.setTitre("Running");
        when(activiteRepository.findByTitre("Running")).thenReturn(null);
        when(activiteRepository.save(any(Activite.class))).thenReturn(activite);

        ResponseEntity<ActiviteDto> response = activiteService.addNewActivite(activite);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Creation Effectuer ", response.getBody().getMessage());
    }

    @Test
    void testAddNewActivite_Update() {
        Activite existingActivite = new Activite();
        existingActivite.setId(1L);
        existingActivite.setTitre("Running");

        Activite updatedActivite = new Activite();
        updatedActivite.setTitre("Running");
        updatedActivite.setId(1L);

        when(activiteRepository.findByTitre("Running")).thenReturn(existingActivite);
        when(activiteRepository.save(any(Activite.class))).thenReturn(updatedActivite);

        ResponseEntity<ActiviteDto> response = activiteService.addNewActivite(updatedActivite);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Modification effectuer", response.getBody().getMessage());
    }

    @Test
    void testChercheActivite() {
        Activite activite = new Activite();
        activite.setTitre("Running");
        when(activiteRepository.findByTitre("Running")).thenReturn(activite);

        Activite result = activiteService.chercheActivite("Running");

        assertNotNull(result);
        assertEquals("Running", result.getTitre());
    }

    @Test
    void testListAllActivites() {
        List<Activite> activites = new ArrayList<>();
        activites.add(new Activite());
        when(activiteRepository.findAll()).thenReturn(activites);

        List<Activite> result = activiteService.listAllActivites();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListActivitesByGoal() {
        List<Activite> activites = new ArrayList<>();
        activites.add(new Activite());
        when(activiteRepository.findByObjectif("Weight Loss")).thenReturn(activites);

        List<Activite> result = activiteService.listActivitesByGoal("Weight Loss");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteActivite_Success() {
        Activite activite = new Activite();
        activite.setId(1L);

        List<Planning> plannings = new ArrayList<>();
        Planning planning = new Planning();
        planning.getActivites().add(activite);
        plannings.add(planning);

        when(activiteRepository.findById(1L)).thenReturn(Optional.of(activite));
        when(planningRepository.findAll()).thenReturn(plannings);

        ResponseEntity<String> response = activiteService.deleteActivite(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Activité supprimée avec succès", response.getBody());
        verify(activiteRepository, times(1)).delete(activite);
        verify(planningRepository, times(1)).save(planning);
    }

    @Test
    void testDeleteActivite_NotFound() {
        when(activiteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = activiteService.deleteActivite(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Activité introuvable", response.getBody());
        verify(activiteRepository, never()).delete(any(Activite.class));
    }
}