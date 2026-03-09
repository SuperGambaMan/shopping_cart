# 🛒 Shopping Cart Application

[English](README.md) | [Español](README.es.md)

A complete shopping cart web application built with **Spring Boot 4.0** and **Thymeleaf**, implementing a full three-step purchase flow: cart management, checkout with billing/shipping information, and payment processing.

## 📋 Table of Contents

- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Installation and Setup](#-installation-and-setup)
- [Project Structure](#-project-structure)
- [Database](#-database)
- [Main Functionalities](#-main-functionalities)
- [Application Endpoints](#-application-endpoints)
- [Purchase Flow](#-purchase-flow)
- [How to Use the Application](#-how-to-use-the-application)
- [Configuration](#-configuration)
- [Run](#-run)

## ✨ Features

- ✅ **Product catalog** with inventory management
- ✅ **Session-based shopping cart** persistent during navigation
- ✅ **Discount coupon system** (percentage or fixed amount)
- ✅ **3-step checkout process** (Wizard):
1. Cart management
2. Billing and shipping information
3. Payment processing
- ✅ **Multiple payment methods** (credit card, PayPal, bank transfer)
- ✅ **Data validation** with Jakarta Bean Validation
- ✅ **Order management** with full history
- ✅ **Responsive interface** with Bootstrap 5
- ✅ **MySQL persistence** using JDBC

## 🚀 Technologies Used

### Backend
- **Java 25**
- **Spring Boot 4.0.0**
  - Spring Web MVC
  - Spring JDBC
  - Spring Validation
  - Spring DevTools
- **Lombok** - Reduces boilerplate code
- **Thymeleaf** - Template engine for views

### Frontend
- **Bootstrap 5.3.2** - CSS framework
- **HTML5 / CSS3**
- **Thymeleaf Templates**

### Database
- **MySQL 8.x**
- **MySQL Connector/J**

### Build Tools
- **Maven** - Dependency management and build automation

## 📦 Prerequisites

Before running the project, make sure you have installed:

- **JDK 25** or higher
- **MySQL 8.x** or higher
- **Maven 3.6+** (or use the included `mvnw` wrapper)
- **Git** (optional, to clone the repository)

## 🔧 Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/SuperGambaMan/shopping_cart
cd shopping_cart
```

### 2. Configure the Database

Create a MySQL database:

```sql
CREATE DATABASE shopping_cart CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Credentials

Edit the file `src/main/resources/application.properties`:

```properties
spring.application.name=shopping_chart

spring.datasource.url=jdbc:mysql://localhost:3306/shopping_cart
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> ⚠️ **Important**: Update `username` and `password` with your MySQL credentials.

### 4. Initialize Schema and Data

Run the SQL scripts located in `src/main/resources/db/`:

```bash
mysql -u root -p shopping_cart < src/main/resources/db/schema.sql
mysql -u root -p shopping_cart < src/main/resources/db/data.sql
```

Or run the scripts manually from your preferred MySQL client.

## 📁 Project Structure

```
shopping_cart/
├── src/
│   ├── main/
│   │   ├── java/org/iesvdm/shopping_cart/
│   │   │   ├── ShoppingChartApplication.java      # Main class
│   │   │   ├── controller/
│   │   │   │   ├── CartController.java            # Cart management
│   │   │   │   └── CheckoutController.java        # Checkout flow
│   │   │   ├── dto/
│   │   │   │   ├── PaymentDTO.java                # DTO for payments
│   │   │   │   └── ProductAddDTO.java             # DTO to add products
│   │   │   ├── model/
│   │   │   │   ├── Cart.java                      # Shopping cart
│   │   │   │   ├── CartItem.java                  # Cart item
│   │   │   │   ├── Coupon.java                    # Discount coupon
│   │   │   │   ├── CustomerOrder.java             # Customer order
│   │   │   │   ├── OrderItem.java                 # Order item
│   │   │   │   └── Product.java                   # Product
│   │   │   ├── repository/
│   │   │   │   ├── CouponRepository.java          # Coupon data access
│   │   │   │   ├── OrderRepository.java           # Order data access
│   │   │   │   └── ProductRepository.java         # Product data access
│   │   │   └── service/
│   │   │       ├── CouponService.java             # Coupon business logic
│   │   │       ├── OrderService.java              # Order business logic
│   │   │       └── ProductService.java            # Product business logic
│   │   └── resources/
│   │       ├── application.properties              # Application configuration
│   │       ├── db/
│   │       │   ├── schema.sql                     # Database schema
│   │       │   └── data.sql                       # Sample data
│   │       ├── static/css/
│   │       │   └── styles.css                     # Custom styles
│   │       └── templates/
│   │           ├── cart.html                      # Step 1: Cart
│   │           ├── checkout_step2.html            # Step 2: Address
│   │           ├── checkout_step3.html            # Step 3: Payment
│   │           └── order_confirmation.html        # Confirmation
│   └── test/
│       └── java/org/iesvdm/shopping_cart/
│           └── ShoppingChartApplicationTests.java
├── pom.xml                                        # Maven configuration
├── mvnw                                           # Maven Wrapper (Unix)
└── mvnw.cmd                                       # Maven Wrapper (Windows)
```

## 🗄️ Database

### Table Schema

The application uses 4 main tables:

#### 1. **product** - Product catalog
- `id` (PK): Unique identifier
- `name`: Product name
- `description`: Detailed description
- `price`: Unit price (DECIMAL)
- `active`: Product status (active/inactive)
- `created_at`: Creation date

#### 2. **coupon** - Discount coupons
- `id` (PK): Unique identifier
- `code`: Coupon code (unique)
- `description`: Coupon description
- `discount_type`: Discount type (PERCENT/AMOUNT)
- `discount_value`: Discount value
- `active`: Coupon status
- `valid_from` / `valid_to`: Validity period

#### 3. **customer_order** - Purchase orders
- `id` (PK): Unique identifier
- `order_number`: Order number (unique)
- `status`: Order status
- `gross_total`, `discount_total`, `final_total`: Totals
- `payment_method`, `payment_status`, `payment_details`: Payment info
- `billing_*` / `shipping_*`: Billing and shipping addresses
- `coupon_id` (FK): Applied coupon

#### 4. **order_item** - Order items
- `id` (PK): Unique identifier
- `order_id` (FK): Reference to the order
- `product_id` (FK): Reference to the product
- `product_name`: Product name (snapshot)
- `unit_price`: Unit price (snapshot)
- `quantity`: Quantity
- `line_total`: Line total

### Sample Data

The `data.sql` file includes:
- **5 sample products** (mouse, keyboard, USB cable, gaming chair, webcam)
- **2 active coupons**:
  - `WELCOME10`: 10% discount
  - `FIVEOFF`: 5 EUR discount
- **3 sample orders** with their corresponding items

## 🎯 Main Functionalities

### 🛍️ Cart Management

- **Add products** from the catalog with custom quantity
- **Increase/decrease** product quantities
- **Remove** products from the cart
- **Apply discount coupons** with validation
- **Automatic calculation** of subtotals, discounts, and final total
- **Session persistence** of the cart during navigation

### 💳 Checkout Process

#### **Step 1: Cart Review**
- Display all products
- Modify quantities
- Apply/remove coupons
- Validate non-empty cart

#### **Step 2: Billing and Shipping Information**
- Billing information form
- Shipping address form
- "Same address for shipping" option
- Required field validation

#### **Step 3: Payment Processing**
- Payment method selection:
  - 💳 Credit card (with card number, expiration, and CVV validation)
  - 🅿️ PayPal (with account email)
  - 🏦 Bank transfer (with IBAN)
- Conditional validation based on selected method
- Final order confirmation

### 📦 Order Management

- Automatic order creation from the cart
- Product snapshot storage (name and price)
- Payment status tracking
- Unique order number generation
- Full billing and shipping data storage

## 🌐 Application Endpoints

### Cart Controller (`CartController`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/cart` | Show shopping cart |
| POST | `/cart/addproduct` | Add product to cart |
| POST | `/cart/inc/{id}` | Increase product quantity |
| POST | `/cart/dec/{id}` | Decrease product quantity |
| POST | `/cart/remove/{id}` | Remove product from cart |
| POST | `/cart/applycoupon` | Apply discount coupon |
| POST | `/cart/checkout` | Start checkout process |

### Checkout Controller (`CheckoutController`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/checkout/step2` | Address form (Step 2) |
| POST | `/checkout/step2` | Process billing/shipping address |
| GET | `/checkout/step3` | Payment form (Step 3) |
| POST | `/checkout/step3` | Process payment and complete order |
| GET | `/order/confirmation/{id}` | Order confirmation page |

## 🔄 Purchase Flow

```
┌─────────────────┐
│  Add            │
│  Products       │
│  to Cart        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  STEP 1         │
│  Cart           │
│  - Modify       │
│  - Coupons      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  STEP 2         │
│  Billing        │
│  and Shipping   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  STEP 3         │
│  Payment        │
│  - Card         │
│  - PayPal       │
│  - Transfer     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Order          │
│  Confirmation   │
└─────────────────┘
```

## 💻 How to Use the Application

### 1. Browse the Catalog
Go to `/cart` to see available products and your current cart.

### 2. Add Products
Select a product from the dropdown, set the quantity, and click "Add to cart".

### 3. Manage the Cart
- Use the **+** and **-** buttons to adjust quantities
- Click **Remove** to delete products
- Enter a coupon code in the corresponding field and apply it

### 4. Proceed to Checkout
Click **Checkout** when you are ready to buy.

### 5. Complete Information
- **Step 2**: Fill in your billing and shipping details
- **Step 3**: Select a payment method and complete the details

### 6. Confirmation
You will receive a confirmation page with your order details.

## ⚙️ Configuration

### Environment Variables (Optional)

You can configure the following environment variables instead of editing `application.properties`:

```bash
# Database
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/shopping_cart
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
```

### Advanced Configuration

The `application.properties` file supports additional Spring Boot settings such as:
- Logging level
- Server port
- Connection pool settings
- Etc.

## 🚀 Run

### Using Maven Wrapper (Recommended)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Unix/Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Using Installed Maven

```bash
mvn spring-boot:run
```

### Build and Run JAR

```bash
mvn clean package
java -jar target/shopping_chart-0.0.1-SNAPSHOT.jar
```

### Access the Application

Once started, open your browser at:

```
http://localhost:8080/cart
```

## 🧪 Testing

Run the tests:

```bash
mvnw test
```

## 📝 Important Notes

- **Session Cart**: The cart is kept in the HTTP session with `@SessionAttributes("cart")`. It is cleared when the browser is closed or the session expires.

- **Conditional Validation**: Forms implement conditional validation based on context (e.g., card fields are only validated when card payment is selected).

- **Product Snapshots**: Product names and prices are saved in `order_item` to preserve historical data, even if products change later.

- **Coupons**: Coupon validation checks:
  - Code existence
  - Active status
  - Validity period (valid date)

## 🤝 Contributing

Contributions are welcome. Please:

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is an educational exercise developed for IES Vega de Mijas.

## 👥 Authors

Developed as an educational project for Spring Boot and enterprise web applications.

By Marcos Bernal.

---

⭐ If this project was useful to you, consider giving it a star on GitHub!
