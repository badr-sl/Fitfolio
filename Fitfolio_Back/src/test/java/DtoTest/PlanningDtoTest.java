package DtoTest;

import com.example.fitfolio.dto.PlanningDto;
import com.example.fitfolio.entities.Planning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanningDtoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        PlanningDto planningDto = new PlanningDto();
        Planning planning = new Planning();

        // Act
        planningDto.setMessage("Success");
        planningDto.setPlanning(planning);

        // Assert
        assertEquals("Success", planningDto.getMessage());
        assertEquals(planning, planningDto.getPlanning());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        PlanningDto planningDto = new PlanningDto();

        // Assert
        assertNotNull(planningDto);
        assertNull(planningDto.getMessage());
        assertNull(planningDto.getPlanning());
    }
}