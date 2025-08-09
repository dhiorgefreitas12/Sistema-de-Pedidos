package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderDto;
import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.enums.OrderStatus;
import Sistema.de.pedidos.sistemaDePedidos.model.Client;
import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.ClientRepository;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Client client;
    private Product product;

    @BeforeEach
    void setUp() {
        client = new Client(1L, "Client 1", BigDecimal.valueOf(2000), null);
        product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(BigDecimal.valueOf(50));
    }

    @Test
    void testSaveOrder() {
        OrderDto orderDto = new OrderDto(
                null, 1L, LocalDate.now(), BigDecimal.valueOf(200),
                OrderStatus.APPROVED, Arrays.asList(
                new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100))
        )
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setClient(client);
        savedOrder.setOrderDate(LocalDate.now());
        savedOrder.setTotal(BigDecimal.valueOf(200));
        savedOrder.setStatus(OrderStatus.APPROVED);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDto result = orderService.save(orderDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1L, result.clientId());
        assertEquals(BigDecimal.valueOf(200), result.total());

        verify(clientRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testSaveOrderClientNotFound() {
        OrderDto orderDto = new OrderDto(
                null, 1L, LocalDate.now(), BigDecimal.valueOf(200),
                OrderStatus.APPROVED, Arrays.asList(
                new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100))
        )
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.save(orderDto)
        );

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findById(1L);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void testSaveOrderProductNotFound() {
        OrderDto orderDto = new OrderDto(
                null, 1L, LocalDate.now(), BigDecimal.valueOf(200),
                OrderStatus.APPROVED, Arrays.asList(
                new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100))
        )
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.save(orderDto)
        );

        assertEquals("Product not found: 1", exception.getMessage());
        verify(clientRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(0)).save(any(Order.class));
    }


    @Test
    void testFindOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.findById(1L)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.delete(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.delete(1L)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, times(1)).existsById(1L);
    }
}
