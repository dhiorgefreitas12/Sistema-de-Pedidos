package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderDto;
import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.enums.OrderStatus;
import Sistema.de.pedidos.sistemaDePedidos.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }


    @Test
    void testFindAllOrdersWithRejectedStatus() throws Exception {
        OrderDto orderDto1 = new OrderDto(
                1L,
                101L,
                LocalDate.now(),
                BigDecimal.valueOf(200),
                OrderStatus.REJECTED,
                Arrays.asList(
                        new OrderItemDto(1L, 1L, 2, BigDecimal.valueOf(100)),
                        new OrderItemDto(1L, 2L, 1, BigDecimal.valueOf(100))
                )
        );
        OrderDto orderDto2 = new OrderDto(
                2L,
                102L,
                LocalDate.now(),
                BigDecimal.valueOf(400),
                OrderStatus.REJECTED,
                Arrays.asList(
                        new OrderItemDto(2L, 3L, 4, BigDecimal.valueOf(200)),
                        new OrderItemDto(2L, 4L, 2, BigDecimal.valueOf(200))
                )
        );

        List<OrderDto> orders = Arrays.asList(orderDto1, orderDto2);
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientId").value(101))
                .andExpect(jsonPath("$[0].status").value("REJECTED"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].clientId").value(102))
                .andExpect(jsonPath("$[1].status").value("REJECTED"));

        verify(orderService, times(1)).findAll();
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/orders/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).delete(1L);
    }
}
