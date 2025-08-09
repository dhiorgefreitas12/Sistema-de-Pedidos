package Sistema.de.pedidos.sistemaDePedidos.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal subtotal
) {
}

