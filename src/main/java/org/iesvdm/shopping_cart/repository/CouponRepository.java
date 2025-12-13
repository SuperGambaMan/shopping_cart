package org.iesvdm.shopping_cart.repository;

import org.iesvdm.shopping_cart.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<Coupon> findByCode(String code){
        List<Coupon> results = jdbcTemplate.query(
                "SELECT * FROM coupon WHERE code = ?",
                new Object[]{code},
                (rs, rowNum) -> mapRow(rs)
        );
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.get(0));
    }

    private Coupon mapRow(ResultSet rs){
        try{
            Coupon c = new Coupon();
            c.setId(rs.getLong("id"));
            c.setCode(rs.getString("code"));
            c.setDescription(rs.getString("description"));
            c.setDiscountType(rs.getString("discount_type"));
            c.setDiscountValue(rs.getBigDecimal("discount_value"));
            c.setActive(rs.getBoolean("active"));
            java.sql.Timestamp vf = rs.getTimestamp("valid_from");
            java.sql.Timestamp vt = rs.getTimestamp("valid_to");
            if (vf != null) c.setValidFrom(vf.toLocalDateTime());
            if (vt != null) c.setValidTo(vt.toLocalDateTime());
            return c;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
