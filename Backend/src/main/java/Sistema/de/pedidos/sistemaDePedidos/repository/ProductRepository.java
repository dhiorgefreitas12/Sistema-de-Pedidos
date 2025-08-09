package Sistema.de.pedidos.sistemaDePedidos.repository;

import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}