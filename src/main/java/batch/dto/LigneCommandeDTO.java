package batch.dto;

import java.math.BigDecimal;

public class LigneCommandeDTO {
    String client;
    String email;
    String produit;
    int quantite;
    BigDecimal prixUnitaire;
    BigDecimal prixTotal;
    String dateCommande;


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    @Override
    public String toString() {
        return "LigneCommandeDTO{" +
                "client='" + client + '\'' +
                ", email='" + email + '\'' +
                ", produit='" + produit + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", prixTotal=" + prixTotal +
                ", dateCommande='" + dateCommande + '\'' +
                '}';
    }
}
