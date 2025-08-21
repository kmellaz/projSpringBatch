package batch.service;

import batch.dao.ClientRepository;
import batch.model.ecommerce.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(final ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void enregistrerClient(final Client client){
        this.clientRepository.save(client);
    }

    public Client findClientBy(final String email){
        return this.clientRepository.findByEmail(email).orElse(null);
    }

    public void supprimerClient(final Client client){
        this.clientRepository.delete(client);
    }


}
