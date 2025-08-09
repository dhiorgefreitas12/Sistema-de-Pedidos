package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.ClientDto;
import Sistema.de.pedidos.sistemaDePedidos.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService service;


    @PostMapping
    public ResponseEntity<ClientDto> create(@RequestBody ClientDto client) {
        return ResponseEntity.ok(service.save(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
