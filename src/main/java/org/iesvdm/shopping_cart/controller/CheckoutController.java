package org.iesvdm.shopping_cart.controller;

import org.iesvdm.shopping_cart.dto.PaymentDTO;
import org.iesvdm.shopping_cart.model.Cart;
import org.iesvdm.shopping_cart.model.CustomerOrder;
import org.iesvdm.shopping_cart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CheckoutController {

    @Autowired
    private OrderRepository orderRepository;

    // Mostrar el formulario del paso 2
    @GetMapping("/checkout/step2")
    public String showStep2(@RequestParam Long orderId, Model model){
        CustomerOrder order = orderRepository.findById(orderId);
        model.addAttribute("customerOrder", order);
        return "checkout_step2";
    }

    // Procesar el formulario del paso 2
    @PostMapping("/checkout/step2")
    public String processStep2(@Valid @ModelAttribute("customerOrder") CustomerOrder customerOrder, BindingResult bindingResult, @RequestParam(required = false) String sameAsBilling, RedirectAttributes redirectAttributes, Model model){
        // Copy billing -> shipping server-side if requested
        if (sameAsBilling != null && sameAsBilling.equals("on")){
            customerOrder.setShippingStreet(customerOrder.getBillingStreet());
            customerOrder.setShippingCity(customerOrder.getBillingCity());
            customerOrder.setShippingPostalCode(customerOrder.getBillingPostalCode());
            customerOrder.setShippingCountry(customerOrder.getBillingCountry());
        }

        // Conditional validation: if shipping fields are empty, require them
        boolean shippingProvided = (customerOrder.getShippingStreet() != null && !customerOrder.getShippingStreet().isBlank())
                || (customerOrder.getShippingCity() != null && !customerOrder.getShippingCity().isBlank())
                || (customerOrder.getShippingCountry() != null && !customerOrder.getShippingCountry().isBlank())
                || (customerOrder.getShippingPostalCode() != null && !customerOrder.getShippingPostalCode().isBlank());

        if (!shippingProvided){
            bindingResult.rejectValue("shippingStreet", "NotBlank", "Shipping address is required or check 'Use the same address for shipping'");
        }

        if (bindingResult.hasErrors()){
            // re-show the form with errors
            model.addAttribute("customerOrder", customerOrder);
            model.addAttribute("errorMessage", "Please correct the errors below");
            return "checkout_step2";
        }

        // persist changes
        orderRepository.updateOrder(customerOrder);
        redirectAttributes.addFlashAttribute("successMessage", "Billing and shipping addresses saved");
        return "redirect:/checkout/step3?orderId=" + customerOrder.getId();
    }

    // Mostrar el formulario del paso 3
    @GetMapping("/checkout/step3")
    public String showStep3(@RequestParam Long orderId, Model model){
        CustomerOrder order = orderRepository.findById(orderId);
        model.addAttribute("order", order);
        // populate empty PaymentDTO
        PaymentDTO payment = new PaymentDTO();
        payment.setId(orderId);
        payment.setPaymentMethod(order.getPaymentMethod());
        model.addAttribute("paymentDTO", payment);
        return "checkout_step3";
    }

    // Procesar el formulario del paso 3 (pago)
    @PostMapping("/checkout/step3")
    public String processStep3(@Valid @ModelAttribute("paymentDTO") PaymentDTO paymentDTO, BindingResult bindingResult, @ModelAttribute("cart") Cart cart, Model model, RedirectAttributes redirectAttributes, HttpSession session){
        // Load order for context
        CustomerOrder order = orderRepository.findById(paymentDTO.getId());
        model.addAttribute("order", order);

        // Conditional validation based on payment method
        if ("CARD".equals(paymentDTO.getPaymentMethod())){
            // Validate card fields presence
            if (paymentDTO.getCardNumber() == null || paymentDTO.getCardNumber().isBlank()){
                bindingResult.rejectValue("cardNumber", "NotBlank", "Card number is required");
            } else if (!paymentDTO.getCardNumber().matches("^\\d{13,19}$")){
                bindingResult.rejectValue("cardNumber", "Pattern", "Card number must be 13 to 19 digits");
            }

            if (paymentDTO.getExpiry() == null || paymentDTO.getExpiry().isBlank()){
                bindingResult.rejectValue("expiry", "NotBlank", "Expiry is required");
            } else if (!paymentDTO.getExpiry().matches("^(0[1-9]|1[0-2])/(\\d{2})$")){
                bindingResult.rejectValue("expiry", "Pattern", "Expiry must be in MM/YY format");
            }

            if (paymentDTO.getCvv() == null || paymentDTO.getCvv().isBlank()){
                bindingResult.rejectValue("cvv", "NotBlank", "CVV is required");
            } else if (!paymentDTO.getCvv().matches("^\\d{3,4}$")){
                bindingResult.rejectValue("cvv", "Pattern", "CVV must be 3 or 4 digits");
            }
        } else if ("PAYPAL".equals(paymentDTO.getPaymentMethod())){
            // Validate PayPal email
            if (paymentDTO.getPaypalEmail() == null || paymentDTO.getPaypalEmail().isBlank()){
                bindingResult.rejectValue("paypalEmail", "NotBlank", "PayPal email is required");
            } else if (!paymentDTO.getPaypalEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
                bindingResult.rejectValue("paypalEmail", "Email", "Please enter a valid email address");
            }
        }
        // BANK does not require extra fields

        if (bindingResult.hasErrors()){
            // return view with errors
            model.addAttribute("errorMessage", "Please correct the errors below");
            return "checkout_step3";
        }

        // Apply simulated payment
        if ("CARD".equals(paymentDTO.getPaymentMethod())){
            order.setPaymentMethod("CARD");
            order.setPaymentStatus("PAID");
            String card = paymentDTO.getCardNumber();
            order.setPaymentDetails("Card **** " + (card != null && card.length() >= 4 ? card.substring(card.length()-4) : "XXXX"));
            order.setStatus("COMPLETED");
        } else if ("PAYPAL".equals(paymentDTO.getPaymentMethod())){
            order.setPaymentMethod("PAYPAL");
            order.setPaymentStatus("PAID");
            order.setPaymentDetails("PayPal: " + paymentDTO.getPaypalEmail());
            order.setStatus("COMPLETED");
        } else if ("BANK".equals(paymentDTO.getPaymentMethod())){
            order.setPaymentMethod("BANK");
            order.setPaymentStatus("PENDING");
            order.setPaymentDetails("Bank transfer - IBAN ES00 1234 5678 9000 0000 0000");
            order.setStatus("PENDING");
        }

        // Guardar en BD
        orderRepository.updatePayment(order);

        // AHORA SÍ limpiamos el carrito después del pago exitoso
        cart.getItems().clear();
        cart.clearCoupon();

        // Eliminar el carrito de la sesión directamente
        session.removeAttribute("cart");

        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully");

        // Redirigir a página final de confirmación con resumen
        return "redirect:/order/confirmation?orderId=" + order.getId();
    }

    // Mostrar página de confirmación
    @GetMapping("/order/confirmation")
    public String showConfirmation(@RequestParam Long orderId, Model model){
        CustomerOrder order = orderRepository.findById(orderId);
        model.addAttribute("order", order);
        return "order_confirmation";
    }
}
