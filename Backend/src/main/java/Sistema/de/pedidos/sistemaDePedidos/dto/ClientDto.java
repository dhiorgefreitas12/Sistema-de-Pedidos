package Sistema.de.pedidos.sistemaDePedidos.dto;

import java.math.BigDecimal;

public record ClientDto(
        Long id,
        String name,
        BigDecimal creditLimit
) {
}
