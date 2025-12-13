package org.iesvdm.shopping_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAddDTO {

    @NotNull(message = "Please select a product from the catalog")
    private Long productId; // si seleccionamos un producto del catálogo
    private String name; // nombre si se añade manualmente
    private BigDecimal price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
}
