package batch.dao;

import batch.model.ecommerce.ClientAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAchatRepository extends JpaRepository<ClientAchat, Long> {


}
