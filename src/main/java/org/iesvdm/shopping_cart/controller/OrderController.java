package org.iesvdm.shopping_cart.controller;

import org.iesvdm.shopping_cart.model.CustomerOrder;
import org.iesvdm.shopping_cart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order/confirmation")
    public String confirmation(@RequestParam Long orderId, Model model){
        CustomerOrder order = orderRepository.findById(orderId);
        model.addAttribute("order", order);
        return "order_confirmation";
    }
}

