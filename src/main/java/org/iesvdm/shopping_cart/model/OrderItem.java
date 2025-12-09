package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private Long id;
    private CustomerOrder customerOrder;
    private Product product;
    private String productName;
    private Double unitPrice;
    private Integer quantity;

    /**
     * Calcula el total de la línea (unitPrice * quantity)
     */
    public Double getLineTotal() {
        if (unitPrice != null && quantity != null) {
            return unitPrice * quantity;
        }
        return 0.0;
    }

}

