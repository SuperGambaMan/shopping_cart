package org.iesvdm.shopping_cart.controller;

import org.iesvdm.shopping_cart.dto.ProductAddDTO;
import org.iesvdm.shopping_cart.model.Cart;
import org.iesvdm.shopping_cart.model.CartItem;
import org.iesvdm.shopping_cart.model.Coupon;
import org.iesvdm.shopping_cart.model.Product;
import org.iesvdm.shopping_cart.service.CouponService;
import org.iesvdm.shopping_cart.service.OrderService;
import org.iesvdm.shopping_cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@SessionAttributes("cart")
public class CartController {

    private final ProductService productService;
    private final CouponService couponService;
    private final OrderService orderService;

    public CartController(ProductService productService, CouponService couponService, OrderService orderService) {
        this.productService = productService;
        this.couponService = couponService;
        this.orderService = orderService;
    }

    @ModelAttribute("productAddDTO")
    public ProductAddDTO productAddDTO(){
        return new ProductAddDTO();
    }

    @ModelAttribute("cart")
    public Cart cart(){
        return new Cart();
    }

    @GetMapping("/cart")
    public String mostrarCarro (Model model, @ModelAttribute("cart") Cart cart){

        model.addAttribute("products", productService.getAll());
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("grossTotal", cart.getGrossTotal());
        model.addAttribute("couponDiscount", cart.getCouponDiscount());
        model.addAttribute("finalTotal", cart.getFinalTotal());
        return "cart";
    }

    @PostMapping("/cart/addproduct")
    public String step1Add (@Valid @ModelAttribute ProductAddDTO productAddDTO, BindingResult bindingResult, @ModelAttribute("cart") Cart cart, Model model, RedirectAttributes redirectAttributes){
        // Validamos el DTO; si hay errores, recargamos la página cart mostrando errores
        if (bindingResult.hasErrors()){
            model.addAttribute("products", productService.getAll());
            model.addAttribute("cartItems", cart.getItems());
            model.addAttribute("grossTotal", cart.getGrossTotal());
            model.addAttribute("couponDiscount", cart.getCouponDiscount());
            model.addAttribute("finalTotal", cart.getFinalTotal());
            return "cart";
        }

        // Solo permitir añadir productos seleccionados del catálogo
        if (productAddDTO.getProductId() == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a product from the catalog");
            return "redirect:/cart";
        }
        CartItem item = new CartItem();
        Optional<Product> p = productService.findById(productAddDTO.getProductId());
        if (p.isPresent()){
            Product prod = p.get();
            item.setProductId(prod.getId());
            item.setProductName(prod.getName());
            item.setUnitPrice(prod.getPrice());
            item.setQuantity(productAddDTO.getQuantity() == null ? 1 : productAddDTO.getQuantity());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Selected product was not found");
            return "redirect:/cart";
        }
        // sólo añadimos si tiene nombre y precio
        if (item.getProductName() != null && item.getUnitPrice() != null){
            cart.addItem(item);
            redirectAttributes.addFlashAttribute("successMessage", "Product added to cart");
        }
        return "redirect:/cart";

    }

    @PostMapping("/cart/inc/{id}")
    public String incQuantity(@PathVariable Long id, @ModelAttribute("cart") Cart cart){
        cart.incQuantity(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/dec/{id}")
    public String decQuantity(@PathVariable Long id, @ModelAttribute("cart") Cart cart){
        cart.decQuantity(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeProduct(@PathVariable Long id, @ModelAttribute("cart") Cart cart){
        cart.removeItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/applycoupon")
    public String applyCoupon(@RequestParam String couponCode, @ModelAttribute("cart") Cart cart, RedirectAttributes redirectAttributes){
        if (couponCode == null || couponCode.trim().isEmpty()){
            cart.clearCoupon();
            redirectAttributes.addFlashAttribute("successMessage", "Coupon cleared");
            return "redirect:/cart";
        }
        Optional<Coupon> oc = couponService.findValidByCode(couponCode.trim());
        if (oc.isPresent()){
            cart.applyCoupon(oc.get());
            redirectAttributes.addFlashAttribute("successMessage", "Coupon applied: " + oc.get().getCode());
        } else {
            cart.clearCoupon();
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid coupon code");
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@ModelAttribute("cart") Cart cart, RedirectAttributes redirectAttributes){
        if (cart.getItems().isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty");
            return "redirect:/cart";
        }
        Long orderId = orderService.createOrderFromCart(cart);
        // NO limpiamos el carrito aquí - se mantendrá hasta que se complete el pago
        // Así el usuario puede volver atrás y ver sus productos
        // Redirigimos al paso 2 pasando el id de la orden para completar facturación/envío
        return "redirect:/checkout/step2?orderId=" + orderId;
    }
}

