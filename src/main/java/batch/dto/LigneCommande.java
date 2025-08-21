package batch.dto;

import java.math.BigDecimal;

public record LigneCommande(String client, String email, String produit,
                            int quantite, BigDecimal prixUnitaire,
                            BigDecimal prixTotal ,String dateCommande) {
}
