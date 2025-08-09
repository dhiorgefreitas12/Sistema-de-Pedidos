package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.ProductDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(BigDecimal.valueOf(50));
    }

    @Test
    void testSaveProduct() {
        ProductDto productDto = new ProductDto(null, "Product A", BigDecimal.valueOf(50));

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Product A");
        savedProduct.setPrice(BigDecimal.valueOf(50));

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = productService.save(productDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Product A", result.name());
        assertEquals(BigDecimal.valueOf(50), result.price());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testFindAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(50));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(75));

        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDto> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).name());
        assertEquals("Product B", result.get(1).name());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Product A", result.name());
        assertEquals(BigDecimal.valueOf(50), result.price());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.findById(1L));
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.delete(1L));
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).existsById(1L);
    }
}
