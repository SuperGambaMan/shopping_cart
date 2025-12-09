package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String billingName;
    private String billingTaxId;
    private String billingStreet;
    private String billingCity;
    private String billingCountry;
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

