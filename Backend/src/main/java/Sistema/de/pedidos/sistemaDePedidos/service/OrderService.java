package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderDto;
import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Client;
import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.ClientRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public OrderDto save(OrderDto dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Order newOrder = Order.builder()
                .client(client)
                .orderDate(dto.orderDate())
                .status(dto.status())
                .total(dto.total())
                .build();


        newOrder = orderRepository.save(newOrder);

        final Order savedOrder = newOrder;

        List<OrderItem> items = dto.items().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.productId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemDto.productId()));

                    BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));

                    return OrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .quantity(itemDto.quantity())
                            .subtotal(subtotal)
                            .build();
                })
                .collect(Collectors.toList());


        savedOrder.setItems(items);
        orderRepository.save(savedOrder);

        return toDto(savedOrder);
    }


    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toDto(order);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    private OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getOrder().getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .toList();

        return new OrderDto(
                order.getId(),
                order.getClient().getId(),
                order.getOrderDate(),
                order.getTotal(),
                order.getStatus(),
                items
        );
    }

}
