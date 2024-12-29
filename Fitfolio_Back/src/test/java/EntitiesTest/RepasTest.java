package EntitiesTest;

import com.example.fitfolio.entities.Repas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepasTest {

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long id = 1L;
        String titre = "Petit déjeuner";
        String description = "Repas riche en protéines";
        String categorie = "Matin";
        Double nbrcalories = 350.0;
        String typerepas = "Breakfast";
        String objectif = "Weight loss";
        String image = "breakfast.jpg";

        // Act
        Repas repas = new Repas(id, titre, description, categorie, nbrcalories, typerepas, objectif, image);

        // Assert
        assertNotNull(repas);
        assertEquals(id, repas.getId());
        assertEquals(titre, repas.getTitre());
        assertEquals(description, repas.getDescription());
        assertEquals(categorie, repas.getCategorie());
        assertEquals(nbrcalories, repas.getNbrcalories());
        assertEquals(typerepas, repas.getTyperepas());
        assertEquals(objectif, repas.getObjectif());
        assertEquals(image, repas.getImage());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        Repas repas = new Repas();

        // Assert
        assertNotNull(repas);
        assertNull(repas.getId());
        assertNull(repas.getTitre());
        assertNull(repas.getDescription());
        assertNull(repas.getCategorie());
        assertNull(repas.getNbrcalories());
        assertNull(repas.getTyperepas());
        assertNull(repas.getObjectif());
        assertNull(repas.getImage());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Repas repas = new Repas();
        Long id = 2L;
        String titre = "Déjeuner";
        String description = "Repas équilibré";
        String categorie = "Midi";
        Double nbrcalories = 600.0;
        String typerepas = "Lunch";
        String objectif = "Muscle gain";
        String image = "lunch.jpg";

        // Act
        repas.setId(id);
        repas.setTitre(titre);
        repas.setDescription(description);
        repas.setCategorie(categorie);
        repas.setNbrcalories(nbrcalories);
        repas.setTyperepas(typerepas);
        repas.setObjectif(objectif);
        repas.setImage(image);

        // Assert
        assertEquals(id, repas.getId());
        assertEquals(titre, repas.getTitre());
        assertEquals(description, repas.getDescription());
        assertEquals(categorie, repas.getCategorie());
        assertEquals(nbrcalories, repas.getNbrcalories());
        assertEquals(typerepas, repas.getTyperepas());
        assertEquals(objectif, repas.getObjectif());
        assertEquals(image, repas.getImage());
    }
}

