package DtoTest;

import com.example.fitfolio.dto.ActiviteDTO1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteDTO1Test {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ActiviteDTO1 activiteDTO1 = new ActiviteDTO1();

        // Act
        activiteDTO1.setId(1L);
        activiteDTO1.setTitre("Running");
        activiteDTO1.setDescription("Morning run in the park");
        activiteDTO1.setCategorie("Cardio");
        activiteDTO1.setNbrcalories(300.0);
        activiteDTO1.setTypeactivite("Outdoor");
        activiteDTO1.setObjectif("Lose weight");
        activiteDTO1.setImage("running.jpg");
        activiteDTO1.setPointcardio(5.0);
        activiteDTO1.setSpeed("10 km/h");

        // Assert
        assertEquals(1L, activiteDTO1.getId());
        assertEquals("Running", activiteDTO1.getTitre());
        assertEquals("Morning run in the park", activiteDTO1.getDescription());
        assertEquals("Cardio", activiteDTO1.getCategorie());
        assertEquals(300.0, activiteDTO1.getNbrcalories());
        assertEquals("Outdoor", activiteDTO1.getTypeactivite());
        assertEquals("Lose weight", activiteDTO1.getObjectif());
        assertEquals("running.jpg", activiteDTO1.getImage());
        assertEquals(5.0, activiteDTO1.getPointcardio());
        assertEquals("10 km/h", activiteDTO1.getSpeed());
    }

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long id = 1L;
        String titre = "Swimming";
        String description = "Afternoon swim at the pool";
        String categorie = "Cardio";
        Double nbrcalories = 500.0;
        String typeactivite = "Indoor";
        String objectif = "Fitness";
        String image = "swimming.jpg";
        Double pointcardio = 4.5;
        String speed = "5 km/h";

        // Act
        ActiviteDTO1 activiteDTO1 = new ActiviteDTO1(id, titre, description, categorie, nbrcalories, typeactivite, objectif, image, pointcardio, speed);

        // Assert
        assertEquals(id, activiteDTO1.getId());
        assertEquals(titre, activiteDTO1.getTitre());
        assertEquals(description, activiteDTO1.getDescription());
        assertEquals(categorie, activiteDTO1.getCategorie());
        assertEquals(nbrcalories, activiteDTO1.getNbrcalories());
        assertEquals(typeactivite, activiteDTO1.getTypeactivite());
        assertEquals(objectif, activiteDTO1.getObjectif());
        assertEquals(image, activiteDTO1.getImage());
        assertEquals(pointcardio, activiteDTO1.getPointcardio());
        assertEquals(speed, activiteDTO1.getSpeed());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        ActiviteDTO1 activiteDTO1 = new ActiviteDTO1();

        // Assert
        assertNotNull(activiteDTO1);
        assertNull(activiteDTO1.getId());
        assertNull(activiteDTO1.getTitre());
        assertNull(activiteDTO1.getDescription());
        assertNull(activiteDTO1.getCategorie());
        assertNull(activiteDTO1.getNbrcalories());
        assertNull(activiteDTO1.getTypeactivite());
        assertNull(activiteDTO1.getObjectif());
        assertNull(activiteDTO1.getImage());
        assertNull(activiteDTO1.getPointcardio());
        assertNull(activiteDTO1.getSpeed());
    }
}
