package Sistema.de.pedidos.sistemaDePedidos.service;

import Sistema.de.pedidos.sistemaDePedidos.dto.ProductDto;
import Sistema.de.pedidos.sistemaDePedidos.model.Product;
import Sistema.de.pedidos.sistemaDePedidos.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto save(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());

        return toDto(productRepository.save(product));
    }


    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }
}
