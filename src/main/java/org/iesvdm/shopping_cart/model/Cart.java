package org.iesvdm.shopping_cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private List<CartItem> items = new ArrayList<>();

    // Cupón aplicado (si lo hay)
    private Coupon appliedCoupon;
    private BigDecimal couponDiscount = BigDecimal.ZERO;

    public void addItem(CartItem item){
        // si existe el mismo productId lo incrementamos, si no lo añadimos
        if (item.getProductId() != null){
            for (CartItem ci : items){
                if (ci.getProductId() != null && ci.getProductId().equals(item.getProductId())){
                    ci.setQuantity(ci.getQuantity() + item.getQuantity());
                    return;
                }
            }
        }
        // asignar un id local si no tiene
        if (item.getId() == null){
            item.setId(System.currentTimeMillis());
        }
        items.add(item);
    }

    public void removeItem(Long id){
        items.removeIf(i -> i.getId() != null && i.getId().equals(id));
    }

    public void incQuantity(Long id){
        for (CartItem ci : items){
            if (ci.getId() != null && ci.getId().equals(id)){
                ci.setQuantity(ci.getQuantity() + 1);
                return;
            }
        }
    }

    public void decQuantity(Long id){
        for (CartItem ci : items){
            if (ci.getId() != null && ci.getId().equals(id)){
                ci.setQuantity(Math.max(1, ci.getQuantity() - 1));
                return;
            }
        }
    }

    public BigDecimal getGrossTotal(){
        return items.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getFinalTotal(){
        return getGrossTotal().subtract(couponDiscount == null ? BigDecimal.ZERO : couponDiscount);
    }

    public void applyCoupon(Coupon coupon){
        this.appliedCoupon = coupon;
        // calcular descuento según tipo
        if (coupon == null){
            this.couponDiscount = BigDecimal.ZERO;
            return;
        }
        BigDecimal gross = getGrossTotal();
        if ("PERCENT".equalsIgnoreCase(coupon.getDiscountType())){
            // discountValue contiene porcentaje (ej. 10.00 para 10%)
            this.couponDiscount = gross.multiply(coupon.getDiscountValue().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP));
        } else if ("AMOUNT".equalsIgnoreCase(coupon.getDiscountType())){
            this.couponDiscount = coupon.getDiscountValue();
        } else {
            // por defecto tratamos como amount
            this.couponDiscount = coupon.getDiscountValue();
        }
        // evitar descuento mayor que el total
        if (this.couponDiscount.compareTo(gross) > 0){
            this.couponDiscount = gross;
        }
    }

    public void clearCoupon(){
        this.appliedCoupon = null;
        this.couponDiscount = BigDecimal.ZERO;
    }
}
