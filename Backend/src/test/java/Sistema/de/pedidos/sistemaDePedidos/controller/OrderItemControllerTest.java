package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Order;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderItemRepository;
import Sistema.de.pedidos.sistemaDePedidos.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    }

    @Test
    void testFindAllOrderItems() throws Exception {
        OrderItemDto orderItemDto1 = new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100));
        OrderItemDto orderItemDto2 = new OrderItemDto(1L, 2L, 1, BigDecimal.valueOf(100));

        when(orderItemService.toDtoList(any())).thenReturn(Arrays.asList(orderItemDto1, orderItemDto2));
        when(orderItemRepository.findAll()).thenReturn(Arrays.asList(new OrderItem(), new OrderItem()));

        mockMvc.perform(get("/order-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].subtotal").value(100))
                .andExpect(jsonPath("$[1].orderId").value(1))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(1))
                .andExpect(jsonPath("$[1].subtotal").value(100));

        verify(orderItemRepository, times(1)).findAll();
        verify(orderItemService, times(1)).toDtoList(any());
    }

    @Test
    void testCreateOrderItem() throws Exception {
        OrderItemDto orderItemDto = new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100));

        // Mock da entidade OrderItem
        OrderItem orderItem = mock(OrderItem.class);

        // Mockando a relação entre OrderItem e Order
        Order mockOrder = mock(Order.class);
        when(orderItem.getOrder()).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn(1L);
        when(orderItem.getProduct()).thenReturn(mock(Product.class));
        when(orderItem.getProduct().getId()).thenReturn(1L);
        when(orderItem.getQuantity()).thenReturn(2);
        when(orderItem.getSubtotal()).thenReturn(BigDecimal.valueOf(100));

        // Simulando o comportamento do serviço
        when(orderItemService.createSingle(any(OrderItemDto.class))).thenReturn(orderItem);

        mockMvc.perform(post("/order-items")
                        .contentType("application/json")
                        .content("{\"orderId\":1, \"productId\":1, \"quantity\":2, \"subtotal\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.subtotal").value(100));

        verify(orderItemService, times(1)).createSingle(any(OrderItemDto.class));
    }

    @Test
    void testFindOrderItemById() throws Exception {
        OrderItemDto orderItemDto = new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100));
        OrderItem orderItem = new OrderItem(); // Mock the OrderItem entity.

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemService.toDtoList(any())).thenReturn(Arrays.asList(orderItemDto));

        mockMvc.perform(get("/order-items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.subtotal").value(100));

        verify(orderItemRepository, times(1)).findById(1L);
        verify(orderItemService, times(1)).toDtoList(any());
    }
    

    @Test
    void testDeleteOrderItem() throws Exception {
        when(orderItemRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/order-items/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(orderItemRepository, times(1)).existsById(1L);
        verify(orderItemRepository, times(1)).deleteById(1L);
    }


}
