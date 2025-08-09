package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.ClientDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Client;
import Sistema.de.pedidos.sistemaDePedidos.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    public ClientDto save(ClientDto dto) {
        Client client = new Client(null, dto.name(), dto.creditLimit(), null);
        return toDto(clientRepository.save(client));
    }

    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ClientDto findById(Long id) {
        return clientRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    private ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getName(), client.getCreditLimit());
    }
}
