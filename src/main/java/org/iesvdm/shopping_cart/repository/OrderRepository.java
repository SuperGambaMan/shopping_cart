package org.iesvdm.shopping_cart.repository;

import org.iesvdm.shopping_cart.model.CartItem;
import org.iesvdm.shopping_cart.model.CustomerOrder;
import org.iesvdm.shopping_cart.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class OrderRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Long insertOrder(CustomerOrder order){
        String sql = "INSERT INTO customer_order (order_number, created_at, status, gross_total, discount_total, final_total, payment_method, payment_status, payment_details, billing_name, billing_tax_id, billing_street, billing_city, billing_country, shipping_street, shipping_city, shipping_postal_code, shipping_country, coupon_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getOrderNumber());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(order.getCreatedAt()));
            ps.setString(3, order.getStatus());
            ps.setDouble(4, order.getGrossTotal() == null ? 0.0 : order.getGrossTotal());
            ps.setDouble(5, order.getDiscountTotal() == null ? 0.0 : order.getDiscountTotal());
            ps.setDouble(6, order.getFinalTotal() == null ? 0.0 : order.getFinalTotal());
            ps.setString(7, order.getPaymentMethod());
            ps.setString(8, order.getPaymentStatus());
            ps.setString(9, order.getPaymentDetails());
            ps.setString(10, order.getBillingName());
            ps.setString(11, order.getBillingTaxId());
            ps.setString(12, order.getBillingStreet());
            ps.setString(13, order.getBillingCity());
            ps.setString(14, order.getBillingCountry());
            ps.setString(15, order.getShippingStreet());
            ps.setString(16, order.getShippingCity());
            ps.setString(17, order.getShippingPostalCode());
            ps.setString(18, order.getShippingCountry());
            if (order.getCoupon() != null && order.getCoupon().getId() != null){
                ps.setLong(19, order.getCoupon().getId());
            } else {
                ps.setNull(19, java.sql.Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public void insertOrderItem(Long orderId, CartItem item){
        String sql = "INSERT INTO order_item (order_id, product_id, product_name, unit_price, quantity, line_total) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                orderId,
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }

    // Nuevo: obtener Order por id
    public CustomerOrder findById(Long id){
        String sql = "SELECT id, order_number, created_at, status, gross_total, discount_total, final_total, payment_method, payment_status, payment_details, billing_name, billing_tax_id, billing_street, billing_city, billing_country, shipping_street, shipping_city, shipping_postal_code, shipping_country, coupon_id FROM customer_order WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<CustomerOrder>(){
            @Override
            public CustomerOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
                CustomerOrder o = new CustomerOrder();
                o.setId(rs.getLong("id"));
                o.setOrderNumber(rs.getString("order_number"));
                o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                o.setStatus(rs.getString("status"));
                o.setGrossTotal(rs.getDouble("gross_total"));
                o.setDiscountTotal(rs.getDouble("discount_total"));
                o.setFinalTotal(rs.getDouble("final_total"));
                o.setPaymentMethod(rs.getString("payment_method"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setPaymentDetails(rs.getString("payment_details"));
                o.setBillingName(rs.getString("billing_name"));
                o.setBillingTaxId(rs.getString("billing_tax_id"));
                o.setBillingStreet(rs.getString("billing_street"));
                o.setBillingCity(rs.getString("billing_city"));
                o.setBillingCountry(rs.getString("billing_country"));
                o.setShippingStreet(rs.getString("shipping_street"));
                o.setShippingCity(rs.getString("shipping_city"));
                o.setShippingPostalCode(rs.getString("shipping_postal_code"));
                o.setShippingCountry(rs.getString("shipping_country"));
                long couponId = rs.getLong("coupon_id");
                if (!rs.wasNull()){
                    Coupon c = new Coupon();
                    c.setId(couponId);
                    o.setCoupon(c);
                }
                return o;
            }
        });
    }

    // Nuevo: actualizar campos de billing/shipping para una orden
    public void updateOrder(CustomerOrder order){
        String sql = "UPDATE customer_order SET billing_name=?, billing_tax_id=?, billing_street=?, billing_city=?, billing_country=?, shipping_street=?, shipping_city=?, shipping_postal_code=?, shipping_country=? WHERE id = ?";
        jdbcTemplate.update(sql,
                order.getBillingName(),
                order.getBillingTaxId(),
                order.getBillingStreet(),
                order.getBillingCity(),
                order.getBillingCountry(),
                order.getShippingStreet(),
                order.getShippingCity(),
                order.getShippingPostalCode(),
                order.getShippingCountry(),
                order.getId()
        );
    }

    // Nuevo: actualizar campos de pago
    public void updatePayment(CustomerOrder order){
        String sql = "UPDATE customer_order SET payment_method=?, payment_status=?, payment_details=?, status=? WHERE id = ?";
        jdbcTemplate.update(sql,
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getPaymentDetails(),
                order.getStatus(),
                order.getId()
        );
    }
}
