package org.iesvdm.shopping_cart.repository;

import org.iesvdm.shopping_cart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Product> findAll(){

        return jdbcTemplate.query("""
                SELECT * FROM product
                """,(rs, rowNum) -> Product.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .price(rs.getBigDecimal("price"))
                        .active(rs.getBoolean("active"))
                        .build()
                );
    }

    public Optional<Product> findById(Long id){
        List<Product> results = jdbcTemplate.query(
                "SELECT * FROM product WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> Product.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .price(rs.getBigDecimal("price"))
                        .active(rs.getBoolean("active"))
                        .build()
        );
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.get(0));
    }

}
