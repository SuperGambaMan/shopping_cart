package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long id; // identificador interno en el carrito
    private Long productId; // puede ser null si el producto se añade manualmente
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    public BigDecimal getLineTotal() {
        if (unitPrice != null && quantity != null) {
            return unitPrice.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}

