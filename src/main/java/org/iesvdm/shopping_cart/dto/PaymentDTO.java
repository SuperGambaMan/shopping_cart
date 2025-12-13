package org.iesvdm.shopping_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id; // order id

    @NotBlank(message = "Please select a payment method")
    private String paymentMethod;

    // Card fields
    @Pattern(regexp = "^\\d{13,19}$", message = "Card number must be 13 to 19 digits")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2})$", message = "Expiry must be in MM/YY format")
    private String expiry;

    @Pattern(regexp = "^\\d{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;

    // PayPal
    @Email(message = "Please enter a valid email address")
    private String paypalEmail;
}
