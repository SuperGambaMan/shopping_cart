-- Data for shopping_cart application (MySQL)
-- Location: src/main/resources/db/data.sql

START TRANSACTION;

-- Products
INSERT INTO product (name, description, price, active) VALUES
('Wireless Mouse', 'Ergonomic wireless mouse', 19.99, 1),
('Mechanical Keyboard', 'RGB mechanical keyboard', 79.50, 1),
('USB-C Cable', 'Durable 1m USB-C to USB-C cable', 7.25, 1),
('Gaming Chair', 'Comfortable gaming chair with lumbar support', 199.99, 1),
('Webcam 1080p', 'HD webcam for streaming and meetings', 49.00, 1);

-- Coupons
INSERT INTO coupon (code, description, discount_type, discount_value, active, valid_from, valid_to) VALUES
('WELCOME10', '10% discount for new customers', 'PERCENT', 10.00, 1, NOW() - INTERVAL 30 DAY, NOW() + INTERVAL 365 DAY),
('FIVEOFF', '5 currency units off', 'AMOUNT', 5.00, 1, NOW() - INTERVAL 10 DAY, NOW() + INTERVAL 30 DAY);

-- Orders (customer_order)
-- Note: gross_total is the sum of order_items.line_total; discount_total and final_total computed accordingly.
INSERT INTO customer_order (order_number, created_at, status, gross_total, discount_total, final_total, payment_method, payment_status, payment_details, billing_name, billing_tax_id, billing_street, billing_city, billing_country, shipping_street, shipping_city, shipping_postal_code, shipping_country, coupon_id) VALUES
('ORD-1001', NOW() - INTERVAL 5 DAY, 'COMPLETED', 113.99, 11.40, 102.59, 'CREDIT_CARD', 'PAID', 'Visa **** 4242', 'Alice Smith', 'B12345678', 'Calle Falsa 123', 'Madrid', 'Spain', 'Calle Falsa 123', 'Madrid', '28001', 'Spain', 1),
('ORD-1002', NOW() - INTERVAL 2 DAY, 'PROCESSING', 49.00, 0.00, 49.00, 'PAYPAL', 'PENDING', 'paypal_tx_987', 'Bob Johnson', 'C98765432', 'Avenida Siempreviva 5', 'Valencia', 'Spain', 'Avenida Siempreviva 5', 'Valencia', '46001', 'Spain', NULL),
('ORD-1003', NOW() - INTERVAL 1 DAY, 'COMPLETED', 207.24, 5.00, 202.24, 'CREDIT_CARD', 'PAID', 'Mastercard **** 1111', 'Charlie Brown', 'D19283746', 'Plaza Mayor 1', 'Sevilla', 'Spain', 'Plaza Mayor 1', 'Sevilla', '41001', 'Spain', 2);

-- Order items
-- For ORD-1001 (id 1)
INSERT INTO order_item (order_id, product_id, product_name, unit_price, quantity, line_total) VALUES
(1, 2, 'Mechanical Keyboard', 79.50, 1, 79.50),
(1, 3, 'USB-C Cable', 7.25, 2, 14.50),
(1, 1, 'Wireless Mouse', 19.99, 1, 19.99);

-- For ORD-1002 (id 2)
INSERT INTO order_item (order_id, product_id, product_name, unit_price, quantity, line_total) VALUES
(2, 5, 'Webcam 1080p', 49.00, 1, 49.00);

-- For ORD-1003 (id 3)
INSERT INTO order_item (order_id, product_id, product_name, unit_price, quantity, line_total) VALUES
(3, 4, 'Gaming Chair', 199.99, 1, 199.99),
(3, 3, 'USB-C Cable', 7.25, 1, 7.25);

COMMIT;

-- End of data
