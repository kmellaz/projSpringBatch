package batch.component;

import batch.dto.LigneCommande;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import batch.service.ClientAchatService;
import java.util.Iterator;
import java.util.List;

@Component("ligneCommandeItemReader")
@StepScope
public class LigneCommandeItemReader implements ItemReader<LigneCommande> {

    private final ClientAchatService clientAchatService;
    private final Iterator<LigneCommande> iterator;

    @Autowired
    public LigneCommandeItemReader(ClientAchatService clientAchatService) {
        this.clientAchatService = clientAchatService;
        List<LigneCommande> allLignes = this.clientAchatService.findAll();
        this.iterator = allLignes.iterator();
    }

    @Override
    public LigneCommande read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return (this.iterator != null && iterator.hasNext())?this.iterator.next() : null;
    }
}
