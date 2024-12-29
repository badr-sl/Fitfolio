package EntitiesTest;

import com.example.fitfolio.entities.Activite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteTest {

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long id = 1L;
        String titre = "Running";
        String description = "Morning jogging session";
        String categorie = "Cardio";
        Double nbrcalories = 250.0;
        String typeactivite = "Outdoor";
        String objectif = "Weight Loss";
        String image = "image.jpg";
        Double pointcardio = 5.0;
        String speed = "10km/h";

        // Act
        Activite activite = new Activite(id, titre, description, categorie, nbrcalories, typeactivite, objectif, image, pointcardio, speed);

        // Assert
        assertNotNull(activite);
        assertEquals(id, activite.getId());
        assertEquals(titre, activite.getTitre());
        assertEquals(description, activite.getDescription());
        assertEquals(categorie, activite.getCategorie());
        assertEquals(nbrcalories, activite.getNbrcalories());
        assertEquals(typeactivite, activite.getTypeactivite());
        assertEquals(objectif, activite.getObjectif());
        assertEquals(image, activite.getImage());
        assertEquals(pointcardio, activite.getPointcardio());
        assertEquals(speed, activite.getSpeed());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        Activite activite = new Activite();

        // Assert
        assertNotNull(activite);
        assertNull(activite.getId());
        assertNull(activite.getTitre());
        assertNull(activite.getDescription());
        assertNull(activite.getCategorie());
        assertNull(activite.getNbrcalories());
        assertNull(activite.getTypeactivite());
        assertNull(activite.getObjectif());
        assertNull(activite.getImage());
        assertNull(activite.getPointcardio());
        assertNull(activite.getSpeed());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Activite activite = new Activite();

        // Act
        activite.setId(2L);
        activite.setTitre("Swimming");
        activite.setDescription("Evening swimming session");
        activite.setCategorie("Water Sports");
        activite.setNbrcalories(400.0);
        activite.setTypeactivite("Indoor");
        activite.setObjectif("Stamina Building");
        activite.setImage("swimming.jpg");
        activite.setPointcardio(8.0);
        activite.setSpeed("5km/h");

        // Assert
        assertEquals(2L, activite.getId());
        assertEquals("Swimming", activite.getTitre());
        assertEquals("Evening swimming session", activite.getDescription());
        assertEquals("Water Sports", activite.getCategorie());
        assertEquals(400.0, activite.getNbrcalories());
        assertEquals("Indoor", activite.getTypeactivite());
        assertEquals("Stamina Building", activite.getObjectif());
        assertEquals("swimming.jpg", activite.getImage());
        assertEquals(8.0, activite.getPointcardio());
        assertEquals("5km/h", activite.getSpeed());
    }

    @Test
    void testChainingSettersAndGetters() {
        // Arrange
        Activite activite = new Activite();

        // Act
        activite.setId(3L);
        activite.setTitre("Cycling");
        activite.setDescription("Morning cycling");
        activite.setCategorie("Cardio");
        activite.setNbrcalories(500.0);
        activite.setTypeactivite("Outdoor");
        activite.setObjectif("Fitness");
        activite.setImage("cycling.jpg");
        activite.setPointcardio(10.0);
        activite.setSpeed("20km/h");

        // Assert
        assertAll("Verify all fields",
                () -> assertEquals(3L, activite.getId()),
                () -> assertEquals("Cycling", activite.getTitre()),
                () -> assertEquals("Morning cycling", activite.getDescription()),
                () -> assertEquals("Cardio", activite.getCategorie()),
                () -> assertEquals(500.0, activite.getNbrcalories()),
                () -> assertEquals("Outdoor", activite.getTypeactivite()),
                () -> assertEquals("Fitness", activite.getObjectif()),
                () -> assertEquals("cycling.jpg", activite.getImage()),
                () -> assertEquals(10.0, activite.getPointcardio()),
                () -> assertEquals("20km/h", activite.getSpeed())
        );
    }
}

