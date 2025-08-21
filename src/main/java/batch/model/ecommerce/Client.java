package batch.model.ecommerce;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Commande> commandes = new ArrayList<>();

    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCommande(Commande commande){
        this.commandes.add(commande);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client client)) return false;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

