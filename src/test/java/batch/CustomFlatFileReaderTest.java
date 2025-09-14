package batch;

import batch.component.CustomFlatFileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomFlatFileReaderTest {

    private CustomFlatFileReader customFlatFileReader;

    @BeforeEach
    public void setUp() throws Exception {
        customFlatFileReader = new CustomFlatFileReader();
        // Assurez-vous que le CustomReader accepte une Resource
        customFlatFileReader.setResource(new ClassPathResource("input-file-test.csv"));
        customFlatFileReader.open(new ExecutionContext()); // Nécessaire pour initialiser la lecture
    }

    @Test
    public void testReadAllItems() throws Exception {
        List<Object> items = new ArrayList<>();
        Object item;

        while ((item = customFlatFileReader.read()) != null) {
            assertNotNull(item, "Item lu ne doit pas être null");

            items.add(item);
        }

        // Le fichier a 5 lignes de données (sans l'en-tête)
        assertEquals(5, items.size(), "Le nombre de lignes lues doit être 5");
    }

    @Test
    public void testColumnCountPerLine()  {
        try {
            this.customFlatFileReader.getDefaultLineMapper().mapLine("client;email;produit;quantite;prixUnitaire;prixTotal;dateCommande",0);
            //this.customFlatFileReader.getDefaultLineMapper().mapLine("TOTO;toto@gmail.com;Tee shirt PUMA;7;15.00;105.00;2023-05-05",0);
            //assertTrue(true);
            //assertThrows();

        } catch (Exception e) {
            throw new RuntimeException(e);
            //assertTrue(false);
        }
        //assertEquals(2, fieldSet.getProperties().size(), "Nombre de colonnes incorrect");
    }


    @Test
    void testCasDerreur() {
        assertThrows(RuntimeException.class, () -> {
            this.customFlatFileReader.getDefaultLineMapper().mapLine("client;email;produit;quantite;prixUnitaire;prixTotal;dateCommande",0);
            //this.customFlatFileReader.getDefaultLineMapper().mapLine("TOTO;toto@gmail.com;Tee shirt PUMA;7;15.00;105.00;2023-05-05",0);
        });
    }



}

