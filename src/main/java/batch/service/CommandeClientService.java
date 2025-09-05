package batch.service;

import batch.dao.ClientRepository;
import batch.dao.CommandeRepository;
import batch.dao.ProduitRepository;
import batch.model.ecommerce.Client;
import batch.model.ecommerce.ClientAchat;
import batch.model.ecommerce.Commande;
import batch.model.ecommerce.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CommandeClientService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;
    private static final int NOMBRE_COMMANDE_MAX = 100_000;

    @Autowired
    public CommandeClientService(CommandeRepository commandeRepository,
                                 ClientRepository clientRepository,
                                 ProduitRepository produitRepository) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.produitRepository = produitRepository;
    }

    @Transactional
    public void commandesClient(){
        List<Client> allClient = this.clientRepository.findAll();
        List<Produit> allProduit = this.produitRepository.findAll();

        for (int indexClient = 0; indexClient < allClient.size(); indexClient++){
            // client
            Client client = getRandomFromList(allClient);
            // commandes
            for(int indexCommande=0 ; indexCommande < NOMBRE_COMMANDE_MAX ; indexCommande++){
                Commande commande = new Commande();
                commande.setDateCommande(LocalDateTime.now());

                // lignes achat

                for (int i=0 ; i<100; i++){
                    ClientAchat ligneAchat = new ClientAchat();
                    Produit produit = getRandomFromList(allProduit);
                    ClientAchat ca = commande.getLignes().stream().filter(ligne->ligne.getProduit().getNom().equals(produit.getNom())).findFirst().orElse(null);
                    if(ca != null){
                        ligneAchat.setQuantite(new Random().nextInt(1,10) + ca.getQuantite());
                        continue;
                    }
                    ligneAchat.setProduit(produit);
                    produit.addClientAchat(ligneAchat);
                    ligneAchat.setQuantite(new Random().nextInt(1,10));
                    ligneAchat.setCommande(commande);
                    commande.addAchat(ligneAchat);
                }
                commande.setClient(client);
                client.addCommande(commande);
            }
            this.clientRepository.save(client);
        }
    }


    private <E> E getRandomFromList(List<E> list){
        // Tirage au sort
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        E selectedObject = list.get(randomIndex);
        return selectedObject;
    }

}
