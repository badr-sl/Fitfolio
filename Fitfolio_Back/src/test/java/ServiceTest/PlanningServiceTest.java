package ServiceTest;

import com.example.fitfolio.dto.PlanningDto;
import com.example.fitfolio.dto.ActiviteDTO1;
import com.example.fitfolio.entities.*;
import com.example.fitfolio.repo.ActiviteRepository;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.PlanningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanningServiceTest {

    @InjectMocks
    private PlanningService planningService;

    @Mock
    private PlanningRepository planningRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActiviteRepository activiteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewPlanning_Creation() {
        Planning planning = new Planning();
        planning.setJour("2024-12-24");

        when(planningRepository.findByJour("2024-12-24")).thenReturn(null);
        when(planningRepository.save(planning)).thenReturn(planning);

        ResponseEntity<PlanningDto> response = planningService.addNewPlanning(planning);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Creation Effectuer ", response.getBody().getMessage());
    }

    @Test
    void testAddNewPlanning_Update() {
        Planning existingPlanning = new Planning();
        existingPlanning.setId(1L);
        existingPlanning.setJour("2024-12-24");

        Planning planning = new Planning();
        planning.setJour("2024-12-24");

        when(planningRepository.findByJour("2024-12-24")).thenReturn(existingPlanning);
        when(planningRepository.save(planning)).thenReturn(existingPlanning);

        ResponseEntity<PlanningDto> response = planningService.addNewPlanning(planning);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Modification effectuer", response.getBody().getMessage());
    }

    @Test
    void testFindPlanningByJour() {
        Planning planning = new Planning();
        when(planningRepository.findByJour("2024-12-24")).thenReturn(planning);

        Planning result = planningService.FindPlanningByJour("2024-12-24");

        assertNotNull(result);
    }

    @Test
    void testListPlanning() {
        List<Planning> plannings = Collections.singletonList(new Planning());
        when(planningRepository.findAll()).thenReturn(plannings);

        List<Planning> result = planningService.listPlanning();

        assertEquals(1, result.size());
    }

    @Test
    void testDeletePlanning_Success() {
        when(planningRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<String> response = planningService.deletePlanning(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Planning supprimé avec succès", response.getBody());
        verify(planningRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePlanning_NotFound() {
        when(planningRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<String> response = planningService.deletePlanning(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Planning introuvable", response.getBody());
    }

    @Test
    void testAddRepasToPlanning_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<PlanningDto> response = planningService.addRepasToPlanning("2024-12-24", 1L, new ArrayList<>());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Utilisateur non trouvé", response.getBody().getMessage());
    }

    @Test
    void testAddRepasToPlanning_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        Planning planning = new Planning();
        planning.setJour("2024-12-24");
        planning.setUtilisateur(utilisateur);

        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur)).thenReturn(Optional.empty());
        when(planningRepository.save(any(Planning.class))).thenReturn(planning);

        ResponseEntity<PlanningDto> response = planningService.addRepasToPlanning("2024-12-24", 1L, new ArrayList<>());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Repas remplacés dans le planning avec succès", response.getBody().getMessage());
    }

    @Test
    void testAddActivitesToPlanning_ActivityNotFound() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        Activite activite = new Activite();
        activite.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(activiteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<PlanningDto> response = planningService.addActivitesToPlanning("2024-12-24", 1L, Collections.singletonList(activite));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Activite with ID 1 not found"));
    }

    @Test
    void testFetchWeeklyActivities_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        Planning planning = new Planning();
        Activite activite = new Activite();
        planning.setActivites(Collections.singletonList(activite));

        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(planningRepository.findByUtilisateurAndJourBetween(any(), any(), any())).thenReturn(Collections.singletonList(planning));

        ResponseEntity<List<Activite>> response = planningService.fetchWeeklyActivities(1L, "2024-12-24");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCalculateDailyCalories_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        Planning planning = new Planning();
        Repas repas = new Repas();
        repas.setNbrcalories(500.0);
        Activite activite = new Activite();
        activite.setNbrcalories(200.0);
        planning.setRepass(Collections.singletonList(repas));
        planning.setActivites(Collections.singletonList(activite));

        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur)).thenReturn(Optional.of(planning));

        ResponseEntity<Double> response = planningService.calculateDailyCalories(1L, "2024-12-24");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(300.0, response.getBody());
    }
    @Test
    void testFetchRepasByJourAndUtilisateur_UserNotFound() {
        // Mock the repository to return null when looking for the user
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<List<Repas>> response = planningService.fetchRepasByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has a NOT_FOUND status and no body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFetchRepasByJourAndUtilisateur_PlanningNotFound() {
        // Mock the user repository to return a valid user
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // Mock the planning repository to return an empty optional
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur))
                .thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<List<Repas>> response = planningService.fetchRepasByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has a NOT_FOUND status and no body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFetchRepasByJourAndUtilisateur_Success() {
        // Mock the user repository to return a valid user
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // Mock the planning repository to return a planning with repas
        Planning planning = new Planning();
        Repas repas1 = new Repas();
        Repas repas2 = new Repas();
        planning.setRepass(Arrays.asList(repas1, repas2));
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur))
                .thenReturn(Optional.of(planning));

        // Call the method
        ResponseEntity<List<Repas>> response = planningService.fetchRepasByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has an OK status and the correct body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
    @Test
    void testFetchActivitesByJourAndUtilisateur_UserNotFound() {
        // Mock the repository to return null when looking for the user
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<List<Activite>> response = planningService.fetchActivitesByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has a NOT_FOUND status and no body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFetchActivitesByJourAndUtilisateur_PlanningNotFound() {
        // Mock the user repository to return a valid user
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // Mock the planning repository to return an empty optional
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur))
                .thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<List<Activite>> response = planningService.fetchActivitesByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has a NOT_FOUND status and no body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFetchActivitesByJourAndUtilisateur_Success() {
        // Mock the user repository to return a valid user
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // Mock the planning repository to return a planning with activities
        Planning planning = new Planning();
        Activite activite1 = new Activite();
        Activite activite2 = new Activite();
        planning.setActivites(Arrays.asList(activite1, activite2));
        when(planningRepository.findByJourAndUtilisateur("2024-12-24", utilisateur))
                .thenReturn(Optional.of(planning));

        // Call the method
        ResponseEntity<List<Activite>> response = planningService.fetchActivitesByJourAndUtilisateur("2024-12-24", 1L);

        // Assert that the response has an OK status and the correct body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
    @Test
    void testFindPlanningsByUserId_Success() {
        Planning planning = new Planning();
        when(planningRepository.findByUtilisateurId(1L)).thenReturn(Collections.singletonList(planning));

        List<Planning> plannings = planningService.findPlanningsByUserId(1L);

        assertNotNull(plannings);
        assertEquals(1, plannings.size());
    }
    @Test
    void testFetchActivitiesByUtilisateur_Success() {
        Object[] activityData = {1L, "Titre", "Description", "Categorie", 100.0, "Type", "Objectif", "Image", 50.0, "Speed"};
        when(planningRepository.findActivitiesByUtilisateurOrderedByPaIdDesc(1L)).thenReturn(Collections.singletonList(activityData));

        List<ActiviteDTO1> activities = planningService.fetchActivitiesByUtilisateur(1L);

        assertNotNull(activities);
        assertEquals(1, activities.size());
        assertEquals("Titre", activities.get(0).getTitre());
        assertEquals(100.0, activities.get(0).getNbrcalories());
    }

    @Test
    void testFetchWeeklyRepas_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List<Repas>> response = planningService.fetchWeeklyRepas(1L, "2024-01-01", 1, Optional.empty());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFetchWeeklyRepas_InvalidDay() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        when(userRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // Act
        ResponseEntity<List<Repas>> response = planningService.fetchWeeklyRepas(1L, "2024-01-01", 1, Optional.of("INVALID_DAY"));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }






}
