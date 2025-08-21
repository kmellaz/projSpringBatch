package batch.model.ecommerce;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    private BigDecimal prix;

    @OneToMany(mappedBy = "produit")
    private List<ClientAchat> achats = new ArrayList<>();


    public void addClientAchat(ClientAchat clientAchat){
        this.achats.add(clientAchat);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Long getId() {
        return id;
    }

    public List<ClientAchat> getAchats() {
        return achats;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Produit produit)) return false;
        return Objects.equals(id, produit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
