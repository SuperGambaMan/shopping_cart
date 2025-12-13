package org.iesvdm.shopping_cart.service;

import org.iesvdm.shopping_cart.model.Cart;
import org.iesvdm.shopping_cart.model.CartItem;
import org.iesvdm.shopping_cart.model.CustomerOrder;
import org.iesvdm.shopping_cart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Long createOrderFromCart(Cart cart){
        // Construir objeto CustomerOrder mínimo para persistir
        CustomerOrder order = new CustomerOrder();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("NEW");
        double gross = cart.getGrossTotal().doubleValue();
        double discount = cart.getCouponDiscount() == null ? 0.0 : cart.getCouponDiscount().doubleValue();
        double finalTotal = cart.getFinalTotal().doubleValue();
        order.setGrossTotal(gross);
        order.setDiscountTotal(discount);
        order.setFinalTotal(finalTotal);
        // datos de facturación mínimos (vacíos) — el flujo real rellenaría estos campos en step2
        order.setPaymentMethod("");
        order.setPaymentStatus("");
        order.setPaymentDetails("");
        order.setBillingName("");
        order.setBillingTaxId("");
        order.setBillingStreet("");
        order.setBillingCity("");
        order.setBillingCountry("");
        order.setShippingStreet("");
        order.setShippingCity("");
        order.setShippingPostalCode("");
        order.setShippingCountry("");
        order.setCoupon(cart.getAppliedCoupon());

        Long orderId = orderRepository.insertOrder(order);
        // insertar items
        for (CartItem item : cart.getItems()){
            orderRepository.insertOrderItem(orderId, item);
        }
        return orderId;
    }
}

