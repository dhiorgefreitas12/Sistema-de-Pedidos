package Sistema.de.pedidos.sistemaDePedidos.repository;

import Sistema.de.pedidos.sistemaDePedidos.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}