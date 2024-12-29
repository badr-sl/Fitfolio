package ServiceTest;

import com.example.fitfolio.dto.RepasDto;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.repo.RepasRepository;
import com.example.fitfolio.service.RepasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepasServiceTest {

    @InjectMocks
    private RepasService repasService;

    @Mock
    private RepasRepository repasRepository;

    @Mock
    private PlanningRepository planningRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewRepas_Creation() {
        Repas repas = new Repas();
        repas.setTitre("Breakfast");

        when(repasRepository.findByTitre("Breakfast")).thenReturn(null);
        when(repasRepository.save(repas)).thenReturn(repas);

        ResponseEntity<RepasDto> response = repasService.addNewRepas(repas);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Creation Effectuer ", response.getBody().getMessage());
        assertEquals(repas, response.getBody().getRepas());
    }



    @Test
    void testFindRepas() {
        Repas repas = new Repas();
        repas.setTitre("Lunch");

        when(repasRepository.findByTitre("Lunch")).thenReturn(repas);

        Repas result = repasService.findRepas("Lunch");

        assertNotNull(result);
        assertEquals("Lunch", result.getTitre());
    }

    @Test
    void testListRepas() {
        List<Repas> repasList = Collections.singletonList(new Repas());
        when(repasRepository.findAll()).thenReturn(repasList);

        List<Repas> result = repasService.listRepas();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteRepas_Success() {
        Repas repas = new Repas();
        repas.setId(1L);

        Planning planning = new Planning();
        planning.setRepass(new ArrayList<>(Collections.singletonList(repas)));

        when(repasRepository.findById(1L)).thenReturn(Optional.of(repas));
        when(planningRepository.findAll()).thenReturn(Collections.singletonList(planning));

        ResponseEntity<String> response = repasService.deleteRepas(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Repas supprimé avec succès", response.getBody());
        verify(planningRepository, times(1)).save(planning);
        verify(repasRepository, times(1)).delete(repas);
    }

    @Test
    void testDeleteRepas_NotFound() {
        when(repasRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = repasService.deleteRepas(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Repas introuvable", response.getBody());
    }

    @Test
    void testFindRepasByTyperepas() {
        List<Repas> repasList = Collections.singletonList(new Repas());
        when(repasRepository.findByTyperepas("Dinner")).thenReturn(repasList);

        List<Repas> result = repasService.findRepasByTyperepas("Dinner");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListRepasByGoal() {
        List<Repas> repasList = Collections.singletonList(new Repas());
        when(repasRepository.findByObjectif("Weight Loss")).thenReturn(repasList);

        List<Repas> result = repasService.listRepasByGoal("Weight Loss");

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}