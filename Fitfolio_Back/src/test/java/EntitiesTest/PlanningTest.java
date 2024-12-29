package EntitiesTest;

import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.entities.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PlanningTest {

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long id = 1L;
        String code = "P123";
        String jour = "Monday";
        Collection<Repas> repass = new ArrayList<>();
        Collection<Activite> activites = new ArrayList<>();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        // Act
        Planning planning = new Planning(id, code, jour, repass, activites, utilisateur);

        // Assert
        assertNotNull(planning);
        assertEquals(id, planning.getId());
        assertEquals(code, planning.getCode());
        assertEquals(jour, planning.getJour());
        assertEquals(repass, planning.getRepass());
        assertEquals(activites, planning.getActivites());
        assertEquals(utilisateur, planning.getUtilisateur());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        Planning planning = new Planning();

        // Assert
        assertNotNull(planning);
        assertNull(planning.getId());
        assertNull(planning.getCode());
        assertNull(planning.getJour());
        assertNotNull(planning.getRepass());
        assertTrue(planning.getRepass().isEmpty());
        assertNotNull(planning.getActivites());
        assertTrue(planning.getActivites().isEmpty());
        assertNull(planning.getUtilisateur());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Planning planning = new Planning();
        Long id = 2L;
        String code = "P456";
        String jour = "Tuesday";
        Collection<Repas> repass = new ArrayList<>();
        Collection<Activite> activites = new ArrayList<>();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(2L);

        // Act
        planning.setId(id);
        planning.setCode(code);
        planning.setJour(jour);
        planning.setRepass(repass);
        planning.setActivites(activites);
        planning.setUtilisateur(utilisateur);

        // Assert
        assertEquals(id, planning.getId());
        assertEquals(code, planning.getCode());
        assertEquals(jour, planning.getJour());
        assertEquals(repass, planning.getRepass());
        assertEquals(activites, planning.getActivites());
        assertEquals(utilisateur, planning.getUtilisateur());
    }

    @Test
    void testAddRepas() {
        // Arrange
        Planning planning = new Planning();
        Repas repas = new Repas();
        repas.setId(1L);

        // Act
        planning.getRepass().add(repas);

        // Assert
        assertNotNull(planning.getRepass());
        assertEquals(1, planning.getRepass().size());
        assertTrue(planning.getRepass().contains(repas));
    }

    @Test
    void testAddActivite() {
        // Arrange
        Planning planning = new Planning();
        Activite activite = new Activite();
        activite.setId(1L);

        // Act
        planning.getActivites().add(activite);

        // Assert
        assertNotNull(planning.getActivites());
        assertEquals(1, planning.getActivites().size());
        assertTrue(planning.getActivites().contains(activite));
    }

    @Test
    void testSetUtilisateur() {
        // Arrange
        Planning planning = new Planning();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        // Act
        planning.setUtilisateur(utilisateur);

        // Assert
        assertNotNull(planning.getUtilisateur());
        assertEquals(utilisateur, planning.getUtilisateur());
    }
}
