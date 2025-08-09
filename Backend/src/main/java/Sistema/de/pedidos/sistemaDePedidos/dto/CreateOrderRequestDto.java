package Sistema.de.pedidos.sistemaDePedidos.dto;

import java.util.List;

public record CreateOrderRequestDto(
        Long clientId,
        List<OrderItemRequestDto> items
) {
}
