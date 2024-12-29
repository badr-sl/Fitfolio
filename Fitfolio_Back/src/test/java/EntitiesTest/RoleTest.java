package EntitiesTest;

import com.example.fitfolio.entities.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Role role = new Role();

        // Act
        role.setId(1L);
        role.setRoleName("ADMIN");

        // Assert
        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getRoleName());
    }

    @Test
    void testConstructor() {
        // Arrange
        Role role = new Role(1L, "USER");

        // Assert
        assertEquals(1L, role.getId());
        assertEquals("USER", role.getRoleName());
    }



    @Test
    void testToString() {
        // Arrange
        Role role = new Role(1L, "ADMIN");

        // Act
        String result = role.toString();

        // Assert
        assertTrue(result.contains("1"));
        assertTrue(result.contains("ADMIN"));
    }
}

