package batch;


import batch.dto.LigneCommande;
import batch.service.ClientAchatService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

//(classes = BatchApplication.class)
@SpringBootTest
@Disabled
public class ServiceTest {

    @Autowired
    private ClientAchatService clientAchatService;

    @Test
    public void findAllTest(){
        List<LigneCommande> all = this.clientAchatService.findAll();
        System.out.println("Nombre de ligne commande trouvée : " + all.size());
    }


}
