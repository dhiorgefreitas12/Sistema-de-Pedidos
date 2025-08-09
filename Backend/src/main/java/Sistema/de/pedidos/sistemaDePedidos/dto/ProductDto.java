package Sistema.de.pedidos.sistemaDePedidos.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        BigDecimal price
) {
}
