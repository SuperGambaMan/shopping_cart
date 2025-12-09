package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    private Long id;
    private String code;
    private String description;
    private String discountType;
    private Double discountValue;
    private Boolean active = true;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;


}

