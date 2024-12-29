package DtoTest;

import com.example.fitfolio.dto.UserDto;
import com.example.fitfolio.entities.Utilisateur;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        UserDto userDto = new UserDto();
        Utilisateur utilisateur = new Utilisateur();

        // Act
        userDto.setMessage("User created successfully");
        userDto.setAppUser(utilisateur);

        // Assert
        assertEquals("User created successfully", userDto.getMessage());
        assertEquals(utilisateur, userDto.getAppUser());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        UserDto userDto = new UserDto();

        // Assert
        assertNotNull(userDto);
        assertNull(userDto.getMessage());
        assertNull(userDto.getAppUser());
    }
}