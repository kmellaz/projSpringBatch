package batch.model.ecommerce;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Commande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCommande;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<ClientAchat> lignes = new ArrayList<>();

    /**
     * ajoute une ligne d'achat à la commande
     * @param ligneAchat
     */
    public void addAchat(ClientAchat ligneAchat){
        this.lignes.add(ligneAchat);
    }

    public Long getId() {
        return id;
    }

    public List<ClientAchat> getLignes() {
        return lignes;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Commande commande)) return false;
        return Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
