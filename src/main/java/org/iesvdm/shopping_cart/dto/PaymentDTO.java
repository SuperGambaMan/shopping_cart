package org.iesvdm.shopping_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id; // order id

    @NotBlank(message = "Please select a payment method")
    private String paymentMethod;

    // Card fields (conditionally validated in controller)
    private String cardNumber;
    private String expiry;
    private String cvv;

    // PayPal (conditionally validated in controller)
    private String paypalEmail;
}
