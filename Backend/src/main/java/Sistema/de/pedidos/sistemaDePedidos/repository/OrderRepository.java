package Sistema.de.pedidos.sistemaDePedidos.repository;

import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientIdAndOrderDateAfter(Long clientId, LocalDate fromDate);
}
