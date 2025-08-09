package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.ProductDto;
import Sistema.de.pedidos.sistemaDePedidos.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto(1L, "Product A", BigDecimal.valueOf(100));

        when(productService.save(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"id\":1, \"name\":\"Product A\", \"price\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.price").value(100));

        verify(productService, times(1)).save(any(ProductDto.class));
    }

    @Test
    void testFindAllProducts() throws Exception {
        ProductDto productDto1 = new ProductDto(1L, "Product A", BigDecimal.valueOf(100));
        ProductDto productDto2 = new ProductDto(2L, "Product B", BigDecimal.valueOf(200));
        List<ProductDto> products = Arrays.asList(productDto1, productDto2);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product B"))
                .andExpect(jsonPath("$[1].price").value(200));

        verify(productService, times(1)).findAll();
    }

    @Test
    void testFindProductById() throws Exception {
        ProductDto productDto = new ProductDto(1L, "Product A", BigDecimal.valueOf(100));

        when(productService.findById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.price").value(100));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }
}
