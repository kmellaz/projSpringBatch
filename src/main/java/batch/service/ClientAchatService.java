package batch.service;

import batch.dao.ClientAchatRepository;
import batch.dto.LigneCommande;
import batch.model.ecommerce.ClientAchat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientAchatService {

    private final ClientAchatRepository clientAchatRepository;

    @Autowired
    public ClientAchatService(ClientAchatRepository clientAchatRepository) {
        this.clientAchatRepository = clientAchatRepository;
    }

    public List<LigneCommande> findAll(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        List<ClientAchat> all = this.clientAchatRepository.findAll();
        List<LigneCommande> allLignesCommande = all.stream()
                .map(clientAchat -> new LigneCommande(clientAchat.getCommande().getClient().getNom(),
                clientAchat.getCommande().getClient().getEmail(),
                clientAchat.getProduit().getNom(),
                clientAchat.getQuantite(),
                clientAchat.getProduit().getPrix(),
                null,
                clientAchat.getCommande().getDateCommande().format(dateTimeFormatter))).collect(Collectors.toList());
        return allLignesCommande;
    }
}
