package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.ClientDto;
import Sistema.de.pedidos.sistemaDePedidos.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void testCreateClient() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "John Doe", BigDecimal.valueOf(5000));

        when(clientService.save(any(ClientDto.class))).thenReturn(clientDto);

        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content("{\"id\":1, \"name\":\"John Doe\", \"creditLimit\":5000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.creditLimit").value(5000));

        verify(clientService, times(1)).save(any(ClientDto.class));
    }

    @Test
    void testFindAllClients() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "John Doe", BigDecimal.valueOf(5000));

        when(clientService.findAll()).thenReturn(Collections.singletonList(clientDto));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].creditLimit").value(5000));

        verify(clientService, times(1)).findAll();
    }

    @Test
    void testFindClientById() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "John Doe", BigDecimal.valueOf(5000));

        when(clientService.findById(1L)).thenReturn(clientDto);

        mockMvc.perform(get("/clients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.creditLimit").value(5000))
                .andExpect(jsonPath("$.creditLimit").value(BigDecimal.valueOf(5000).toString()));

        verify(clientService, times(1)).findById(1L);
    }
    
    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientService).delete(1L);

        mockMvc.perform(delete("/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).delete(1L);
    }
}
