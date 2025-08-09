package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderItemRepository;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderRepository;
import Sistema.de.pedidos.sistemaDePedidos.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        order = new Order(1L, null, null, BigDecimal.valueOf(1000), null, null);
        product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(BigDecimal.valueOf(50));
    }


    @Test
    void testCreateSingleOrderItem() {
        OrderItemDto itemDto = new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderItem savedItem = new OrderItem(1L, order, product, 2, BigDecimal.valueOf(100));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(savedItem);

        OrderItem result = orderItemService.createSingle(itemDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.valueOf(100), result.getSubtotal());

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }


    @Test
    void testCreateSingleOrderItemProductNotFound() {
        OrderItemDto itemDto = new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderItemService.createSingle(itemDto)
        );

        assertEquals("Product not found", exception.getMessage());
    }
}
