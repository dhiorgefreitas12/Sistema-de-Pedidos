package Sistema.de.pedidos.sistemaDePedidos.dto;

import Sistema.de.pedidos.sistemaDePedidos.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderDto(
        Long id,
        Long clientId,
        LocalDate orderDate,
        BigDecimal total,
        OrderStatus status,
        List<OrderItemDto> items
) {
}
