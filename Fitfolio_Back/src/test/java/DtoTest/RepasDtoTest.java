package DtoTest;

import com.example.fitfolio.dto.RepasDto;
import com.example.fitfolio.entities.Repas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepasDtoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        RepasDto repasDto = new RepasDto();
        Repas repas = new Repas();

        // Act
        repasDto.setMessage("Success");
        repasDto.setRepas(repas);

        // Assert
        assertEquals("Success", repasDto.getMessage());
        assertEquals(repas, repasDto.getRepas());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        RepasDto repasDto = new RepasDto();

        // Assert
        assertNotNull(repasDto);
        assertNull(repasDto.getMessage());
        assertNull(repasDto.getRepas());
    }
}
