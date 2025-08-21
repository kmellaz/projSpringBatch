package batch.model.ecommerce;

import java.io.Serializable;
import java.util.Objects;

public class ClientAchatId implements Serializable {
    private Long commande;
    private Long produit;

    public Long getCommande() {
        return commande;
    }

    public void setCommande(Long commande) {
        this.commande = commande;
    }

    public Long getProduit() {
        return produit;
    }

    public void setProduit(Long produit) {
        this.produit = produit;
    }

    // equals() and hashCode() requis

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClientAchatId that)) return false;
        return Objects.equals(commande, that.commande) && Objects.equals(produit, that.produit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commande, produit);
    }
}

