package EntitiesTest;

import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();

        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "ADMIN"));

        Collection<Planning> plannings = new ArrayList<>();

        // Act
        utilisateur.setId(1L);
        utilisateur.setUsername("john_doe");
        utilisateur.setPassword("password123");
        utilisateur.setPrenom("John");
        utilisateur.setNom("Doe");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setTelephone("123456789");
        utilisateur.setAge(30);
        utilisateur.setPoids(75.5);
        utilisateur.setTaille(1.8);
        utilisateur.setSexe("Male");
        utilisateur.setObjectif("Weight Loss");
        utilisateur.setRoles(roles);
        utilisateur.setPlannings(plannings);
        utilisateur.setDate("2024-01-01");

        // Assert
        assertEquals(1L, utilisateur.getId());
        assertEquals("john_doe", utilisateur.getUsername());
        assertEquals("password123", utilisateur.getPassword());
        assertEquals("John", utilisateur.getPrenom());
        assertEquals("Doe", utilisateur.getNom());
        assertEquals("john.doe@example.com", utilisateur.getEmail());
        assertEquals("123456789", utilisateur.getTelephone());
        assertEquals(30, utilisateur.getAge());
        assertEquals(75.5, utilisateur.getPoids());
        assertEquals(1.8, utilisateur.getTaille());
        assertEquals("Male", utilisateur.getSexe());
        assertEquals("Weight Loss", utilisateur.getObjectif());
        assertEquals(roles, utilisateur.getRoles());
        assertEquals(plannings, utilisateur.getPlannings());
        assertEquals("2024-01-01", utilisateur.getDate());
    }

    @Test
    void testConstructorWithAllFields() {
        // Arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "ADMIN"));

        Collection<Planning> plannings = new ArrayList<>();

        // Act
        Utilisateur utilisateur = new Utilisateur(
                "2024-01-01", 1L, "john_doe", "password123",
                roles, "John", "john.doe@example.com", "Doe",
                "123456789", 30, 75.5, 1.8,
                "Male", "Weight Loss", plannings
        );

        // Assert
        assertEquals(1L, utilisateur.getId());
        assertEquals("john_doe", utilisateur.getUsername());
        assertEquals("password123", utilisateur.getPassword());
        assertEquals("John", utilisateur.getPrenom());
        assertEquals("Doe", utilisateur.getNom());
        assertEquals("john.doe@example.com", utilisateur.getEmail());
        assertEquals("123456789", utilisateur.getTelephone());
        assertEquals(30, utilisateur.getAge());
        assertEquals(75.5, utilisateur.getPoids());
        assertEquals(1.8, utilisateur.getTaille());
        assertEquals("Male", utilisateur.getSexe());
        assertEquals("Weight Loss", utilisateur.getObjectif());
        assertEquals(roles, utilisateur.getRoles());
        assertEquals(plannings, utilisateur.getPlannings());
        assertEquals("2024-01-01", utilisateur.getDate());
    }

    @Test
    void testConstructorWithMinimalFields() {
        // Arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "USER"));

        // Act
        Utilisateur utilisateur = new Utilisateur(1L, "jane_doe", "password456", roles, 25);

        // Assert
        assertEquals(1L, utilisateur.getId());
        assertEquals("jane_doe", utilisateur.getUsername());
        assertEquals("password456", utilisateur.getPassword());
        assertEquals(roles, utilisateur.getRoles());
        assertEquals(25, utilisateur.getAge());
    }

    @Test
    void testRolesManagement() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        Role adminRole = new Role(1L, "ADMIN");
        Role userRole = new Role(2L, "USER");

        // Act
        utilisateur.getRoles().add(adminRole);
        utilisateur.getRoles().add(userRole);

        // Assert
        assertEquals(2, utilisateur.getRoles().size());
        assertTrue(utilisateur.getRoles().contains(adminRole));
        assertTrue(utilisateur.getRoles().contains(userRole));
    }

    @Test
    void testPlanningsManagement() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        Planning planning1 = new Planning();
        Planning planning2 = new Planning();

        // Act
        utilisateur.getPlannings().add(planning1);
        utilisateur.getPlannings().add(planning2);

        // Assert
        assertEquals(2, utilisateur.getPlannings().size());
        assertTrue(utilisateur.getPlannings().contains(planning1));
        assertTrue(utilisateur.getPlannings().contains(planning2));
    }

    @Test
    void testToString() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setUsername("john_doe");
        utilisateur.setPrenom("John");
        utilisateur.setNom("Doe");

        // Act
        String toStringResult = utilisateur.toString();

        // Assert
        assertTrue(toStringResult.contains("john_doe"));
        assertTrue(toStringResult.contains("John"));
        assertTrue(toStringResult.contains("Doe"));
    }


}

