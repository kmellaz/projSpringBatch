package batch.model.ecommerce;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
//@IdClass(ClientAchatId.class) // Clé composite
public class ClientAchat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //@Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    //@Id
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClientAchat that)) return false;
        return Objects.equals(commande, that.commande) && Objects.equals(produit, that.produit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commande, produit);
    }
}

