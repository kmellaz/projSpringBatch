package batch.component;

import batch.dto.LigneCommande;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("ligneCommandeItemProcessor")
@StepScope
public class LigneCommandeItemProcessor implements ItemProcessor<LigneCommande, LigneCommande> {
    @Override
    public LigneCommande process(LigneCommande item) throws Exception {
        LigneCommande resultat = new LigneCommande(item.client(), item.email(),
                item.produit(), item.quantite(), item.prixUnitaire(),
                item.prixUnitaire().multiply(new BigDecimal(item.quantite())),
                item.dateCommande());
        return resultat;
    }
}
