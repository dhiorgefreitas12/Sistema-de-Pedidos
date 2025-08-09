package Sistema.de.pedidos.sistemaDePedidos.repository;

import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
