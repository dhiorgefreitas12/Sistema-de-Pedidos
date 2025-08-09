package Sistema.de.pedidos.sistemaDePedidos.dto;

public record OrderItemRequestDto(
        Long productId,
        Integer quantity
) {
}
