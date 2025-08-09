package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderItemRepository;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderRepository;
import Sistema.de.pedidos.sistemaDePedidos.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public List<OrderItem> createOrderItems(List<OrderItemDto> itemDtos, Order order) {
        return itemDtos.stream().map(dto -> {
            Product product = productRepository.findById(dto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: ID " + dto.productId()));

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(dto.quantity()));

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(dto.quantity())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemDto(
                        item.getOrder().getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
    }

    public OrderItem createSingle(OrderItemDto dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(dto.quantity()));

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(dto.quantity())
                .subtotal(subtotal)
                .build();

        return orderItemRepository.save(item);
    }

}
