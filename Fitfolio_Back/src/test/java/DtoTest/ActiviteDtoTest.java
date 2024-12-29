package DtoTest;

import com.example.fitfolio.dto.ActiviteDto;
import com.example.fitfolio.entities.Activite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteDtoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ActiviteDto activiteDto = new ActiviteDto();
        Activite activite = new Activite();
        activite.setTitre("Running");
        activite.setDescription("Morning run in the park");
        activite.setNbrcalories(300.0);

        String message = "Activity created successfully";

        // Act
        activiteDto.setMessage(message);
        activiteDto.setActivite(activite);

        // Assert
        assertEquals(message, activiteDto.getMessage());
        assertEquals(activite, activiteDto.getActivite());
        assertEquals("Running", activiteDto.getActivite().getTitre());
        assertEquals("Morning run in the park", activiteDto.getActivite().getDescription());
        assertEquals(300.0, activiteDto.getActivite().getNbrcalories());
    }

    @Test
    void testConstructor() {
        // Arrange
        Activite activite = new Activite();
        activite.setTitre("Swimming");
        activite.setDescription("Afternoon swim at the pool");
        activite.setNbrcalories(500.0);

        String message = "Activity updated successfully";

        // Act
        ActiviteDto activiteDto = new ActiviteDto();
        activiteDto.setMessage(message);
        activiteDto.setActivite(activite);

        // Assert
        assertNotNull(activiteDto);
        assertEquals(message, activiteDto.getMessage());
        assertEquals(activite, activiteDto.getActivite());
        assertEquals("Swimming", activiteDto.getActivite().getTitre());
        assertEquals("Afternoon swim at the pool", activiteDto.getActivite().getDescription());
        assertEquals(500.0, activiteDto.getActivite().getNbrcalories());
    }
}