package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {

    private Long id;
    private String orderNumber;
    private LocalDateTime createdAt;
    private String status;
    private Double grossTotal;
    private Double discountTotal = 0.0;
    private Double finalTotal;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDetails;

    @NotBlank(message = "Billing full name is required")
    private String billingName;

    private String billingTaxId;

    @NotBlank(message = "Billing street is required")
    private String billingStreet;

    @NotBlank(message = "Billing city is required")
    private String billingCity;

    @NotBlank(message = "Billing country is required")
    private String billingCountry;

    @NotBlank(message = "Billing postal code is required")
    private String billingPostalCode; // añadido

    private String shippingStreet;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private Coupon coupon;
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setCustomerOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setCustomerOrder(null);
    }

}
