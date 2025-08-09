package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.ClientDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Client;
import Sistema.de.pedidos.sistemaDePedidos.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSaveClient() {
        ClientDto clientDto = new ClientDto(null, "Teste1", BigDecimal.valueOf(1500));

        Client client = new Client(null, "Teste1", BigDecimal.valueOf(1500), null);
        Client savedClient = new Client(1L, "Teste1", BigDecimal.valueOf(1500), null);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientDto result = clientService.save(clientDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Teste1", result.name());
        assertEquals(BigDecimal.valueOf(1500), result.creditLimit());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testFindAllClients() {
        Client client1 = new Client(1L, "Teste1", BigDecimal.valueOf(1500), null);
        Client client2 = new Client(2L, "Teste2", BigDecimal.valueOf(2500), null);
        List<Client> clientList = Arrays.asList(client1, client2);

        when(clientRepository.findAll()).thenReturn(clientList);

        List<ClientDto> result = clientService.findAll();

        assertEquals(2, result.size());
        assertEquals("Teste1", result.get(0).name());
        assertEquals("Teste2", result.get(1).name());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindClientById() {
        Client client = new Client(1L, "Teste1", BigDecimal.valueOf(1500), null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDto result = clientService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Teste1", result.name());

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testFindClientByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> clientService.findById(1L));
        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.existsById(1L)).thenReturn(true);

        clientService.delete(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteClientNotFound() {
        when(clientRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> clientService.delete(1L));
        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(1)).existsById(1L);
    }
}
