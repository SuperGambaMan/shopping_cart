-- Schema for shopping_cart application (MySQL)
-- Location: src/main/resources/db/schema.sql

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS customer_order;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS product;

SET FOREIGN_KEY_CHECKS=1;

-- Table: product
CREATE TABLE product (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: coupon
CREATE TABLE coupon (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL,
  description TEXT,
  discount_type VARCHAR(20) NOT NULL,
  discount_value DECIMAL(10,2) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  valid_from DATETIME,
  valid_to DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_coupon_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: customer_order
CREATE TABLE customer_order (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_number VARCHAR(50) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(50),
  gross_total DECIMAL(12,2) NOT NULL,
  discount_total DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  final_total DECIMAL(12,2) NOT NULL,
  payment_method VARCHAR(100),
  payment_status VARCHAR(50),
  payment_details TEXT,
  billing_name VARCHAR(255),
  billing_tax_id VARCHAR(100),
  billing_street VARCHAR(255),
  billing_city VARCHAR(100),
  billing_country VARCHAR(100),
  shipping_street VARCHAR(255),
  shipping_city VARCHAR(100),
  shipping_postal_code VARCHAR(20),
  shipping_country VARCHAR(100),
  coupon_id BIGINT DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_number (order_number),
  INDEX idx_order_coupon (coupon_id),
  CONSTRAINT fk_order_coupon FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: order_item
CREATE TABLE order_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_name VARCHAR(255),
  unit_price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  line_total DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_order_item_order (order_id),
  INDEX idx_order_item_product (product_id),
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- End of schema

