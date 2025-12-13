package org.iesvdm.shopping_cart.service;

import org.iesvdm.shopping_cart.model.Coupon;
import org.iesvdm.shopping_cart.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    CouponRepository couponRepository;

    public Optional<Coupon> findValidByCode(String code){
        Optional<Coupon> oc = couponRepository.findByCode(code);
        if (oc.isEmpty()) return Optional.empty();
        Coupon c = oc.get();
        if (c.getActive() == null || !c.getActive()) return Optional.empty();
        LocalDateTime now = LocalDateTime.now();
        if (c.getValidFrom() != null && now.isBefore(c.getValidFrom())) return Optional.empty();
        if (c.getValidTo() != null && now.isAfter(c.getValidTo())) return Optional.empty();
        return Optional.of(c);
    }
}

