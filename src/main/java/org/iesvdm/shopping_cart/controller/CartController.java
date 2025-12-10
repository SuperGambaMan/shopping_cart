package org.iesvdm.shopping_cart.controller;

import org.iesvdm.shopping_cart.dto.ProductAddDTO;
import org.iesvdm.shopping_cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String mostrarCarro (Model model){

        model.addAttribute("productAddDTO", new ProductAddDTO());
        model.addAttribute("products", productService.getAll());
        return "cart";
    }

    @PostMapping("/cart/addproduct")
    public String step1Add (){
        return"addproduct";

    }
}
