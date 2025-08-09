package Sistema.de.pedidos.sistemaDePedidos.controller;

import Sistema.de.pedidos.sistemaDePedidos.dto.OrderItemDto;
import Sistema.de.pedidos.sistemaDePedidos.model.OrderItem;
import Sistema.de.pedidos.sistemaDePedidos.repository.OrderItemRepository;
import Sistema.de.pedidos.sistemaDePedidos.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemDto>> findAll() {
        List<OrderItem> items = orderItemRepository.findAll();
        return ResponseEntity.ok(orderItemService.toDtoList(items));
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> create(@RequestBody OrderItemDto dto) {
        OrderItem created = orderItemService.createSingle(dto);
        return ResponseEntity.ok(
                new OrderItemDto(
                        created.getOrder().getId(),
                        created.getProduct().getId(),
                        created.getQuantity(),
                        created.getSubtotal()
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> findById(@PathVariable Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));
        return ResponseEntity.ok(orderItemService.toDtoList(List.of(item)).get(0));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem not found");
        }
        orderItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
