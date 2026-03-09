# 🛒 Shopping Cart Application

Una aplicación web de carrito de compras completa desarrollada con **Spring Boot 4.0** y **Thymeleaf**, que implementa un flujo completo de compra en tres pasos: gestión del carrito, checkout con información de facturación/envío, y procesamiento de pagos.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Base de Datos](#-base-de-datos)
- [Funcionalidades Principales](#-funcionalidades-principales)
- [Endpoints de la Aplicación](#-endpoints-de-la-aplicación)
- [Flujo de Compra](#-flujo-de-compra)
- [Uso de la Aplicación](#-uso-de-la-aplicación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)

## ✨ Características

- ✅ **Catálogo de productos** con gestión de inventario
- ✅ **Carrito de compras en sesión** persistente durante la navegación
- ✅ **Sistema de cupones de descuento** (porcentaje o cantidad fija)
- ✅ **Proceso de checkout en 3 pasos** (Wizard):
1. Gestión del carrito
2. Información de facturación y envío
3. Procesamiento de pago
- ✅ **Múltiples métodos de pago** (tarjeta de crédito, PayPal, transferencia bancaria)
- ✅ **Validación de datos** con Jakarta Bean Validation
- ✅ **Gestión de órdenes** con historial completo
- ✅ **Interfaz responsive** con Bootstrap 5
- ✅ **Persistencia con MySQL** usando JDBC

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 25**
- **Spring Boot 4.0.0**
  - Spring Web MVC
  - Spring JDBC
  - Spring Validation
  - Spring DevTools
- **Lombok** - Reducción de código boilerplate
- **Thymeleaf** - Motor de plantillas para vistas

### Frontend
- **Bootstrap 5.3.2** - Framework CSS
- **HTML5 / CSS3**
- **Thymeleaf Templates**

### Base de Datos
- **MySQL 8.x**
- **MySQL Connector/J**

### Herramientas de Build
- **Maven** - Gestión de dependencias y construcción

## 📦 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **JDK 25** o superior
- **MySQL 8.x** o superior
- **Maven 3.6+** (o usa el wrapper incluido `mvnw`)
- **Git** (opcional, para clonar el repositorio)

## 🔧 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/SuperGambaMan/shopping_cart
cd shopping_cart
```

### 2. Configurar la Base de Datos

Crea una base de datos MySQL:

```sql
CREATE DATABASE shopping_cart CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar Credenciales

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.application.name=shopping_chart

spring.datasource.url=jdbc:mysql://localhost:3306/shopping_cart
spring.datasource.username=<tu_usuario>
spring.datasource.password=<tu_contraseña>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> ⚠️ **Importante**: Actualiza `username` y `password` con tus credenciales de MySQL.

### 4. Inicializar el Schema y Datos

Ejecuta los scripts SQL ubicados en `src/main/resources/db/`:

```bash
mysql -u root -p shopping_cart < src/main/resources/db/schema.sql
mysql -u root -p shopping_cart < src/main/resources/db/data.sql
```

O ejecuta manualmente los scripts desde tu cliente MySQL favorito.

## 📁 Estructura del Proyecto

```
shopping_cart/
├── src/
│   ├── main/
│   │   ├── java/org/iesvdm/shopping_cart/
│   │   │   ├── ShoppingChartApplication.java      # Clase principal
│   │   │   ├── controller/
│   │   │   │   ├── CartController.java            # Gestión del carrito
│   │   │   │   └── CheckoutController.java        # Proceso de checkout
│   │   │   ├── dto/
│   │   │   │   ├── PaymentDTO.java                # DTO para pagos
│   │   │   │   └── ProductAddDTO.java             # DTO para añadir productos
│   │   │   ├── model/
│   │   │   │   ├── Cart.java                      # Carrito de compras
│   │   │   │   ├── CartItem.java                  # Ítem del carrito
│   │   │   │   ├── Coupon.java                    # Cupón de descuento
│   │   │   │   ├── CustomerOrder.java             # Orden del cliente
│   │   │   │   ├── OrderItem.java                 # Ítem de la orden
│   │   │   │   └── Product.java                   # Producto
│   │   │   ├── repository/
│   │   │   │   ├── CouponRepository.java          # Acceso a datos de cupones
│   │   │   │   ├── OrderRepository.java           # Acceso a datos de órdenes
│   │   │   │   └── ProductRepository.java         # Acceso a datos de productos
│   │   │   └── service/
│   │   │       ├── CouponService.java             # Lógica de negocio de cupones
│   │   │       ├── OrderService.java              # Lógica de negocio de órdenes
│   │   │       └── ProductService.java            # Lógica de negocio de productos
│   │   └── resources/
│   │       ├── application.properties              # Configuración de la aplicación
│   │       ├── db/
│   │       │   ├── schema.sql                     # Esquema de la base de datos
│   │       │   └── data.sql                       # Datos de prueba
│   │       ├── static/css/
│   │       │   └── styles.css                     # Estilos personalizados
│   │       └── templates/
│   │           ├── cart.html                      # Paso 1: Carrito
│   │           ├── checkout_step2.html            # Paso 2: Dirección
│   │           ├── checkout_step3.html            # Paso 3: Pago
│   │           └── order_confirmation.html        # Confirmación
│   └── test/
│       └── java/org/iesvdm/shopping_cart/
│           └── ShoppingChartApplicationTests.java
├── pom.xml                                        # Configuración de Maven
├── mvnw                                           # Maven Wrapper (Unix)
└── mvnw.cmd                                       # Maven Wrapper (Windows)
```

## 🗄️ Base de Datos

### Esquema de Tablas

La aplicación utiliza 4 tablas principales:

#### 1. **product** - Catálogo de productos
- `id` (PK): Identificador único
- `name`: Nombre del producto
- `description`: Descripción detallada
- `price`: Precio unitario (DECIMAL)
- `active`: Estado del producto (activo/inactivo)
- `created_at`: Fecha de creación

#### 2. **coupon** - Cupones de descuento
- `id` (PK): Identificador único
- `code`: Código del cupón (único)
- `description`: Descripción del cupón
- `discount_type`: Tipo de descuento (PERCENT/AMOUNT)
- `discount_value`: Valor del descuento
- `active`: Estado del cupón
- `valid_from` / `valid_to`: Periodo de validez

#### 3. **customer_order** - Órdenes de compra
- `id` (PK): Identificador único
- `order_number`: Número de orden (único)
- `status`: Estado de la orden
- `gross_total`, `discount_total`, `final_total`: Totales
- `payment_method`, `payment_status`, `payment_details`: Info de pago
- `billing_*` / `shipping_*`: Direcciones de facturación y envío
- `coupon_id` (FK): Cupón aplicado

#### 4. **order_item** - Items de las órdenes
- `id` (PK): Identificador único
- `order_id` (FK): Referencia a la orden
- `product_id` (FK): Referencia al producto
- `product_name`: Nombre del producto (snapshot)
- `unit_price`: Precio unitario (snapshot)
- `quantity`: Cantidad
- `line_total`: Total de la línea

### Datos de Prueba

El archivo `data.sql` incluye:
- **5 productos** de ejemplo (ratón, teclado, cable USB, silla gaming, webcam)
- **2 cupones** activos:
  - `WELCOME10`: 10% de descuento
  - `FIVEOFF`: 5€ de descuento
- **3 órdenes** de ejemplo con sus ítems correspondientes

## 🎯 Funcionalidades Principales

### 🛍️ Gestión del Carrito

- **Añadir productos** desde el catálogo con cantidad personalizada
- **Incrementar/decrementar** cantidad de productos
- **Eliminar** productos del carrito
- **Aplicar cupones** de descuento con validación
- **Cálculo automático** de subtotales, descuentos y total final
- **Persistencia en sesión** del carrito durante la navegación

### 💳 Proceso de Checkout

#### **Paso 1: Revisión del Carrito**
- Visualización de todos los productos
- Modificación de cantidades
- Aplicación/eliminación de cupones
- Validación de carrito no vacío

#### **Paso 2: Información de Facturación y Envío**
- Formulario de datos de facturación
- Formulario de dirección de envío
- Opción "misma dirección para envío"
- Validación de campos obligatorios

#### **Paso 3: Procesamiento de Pago**
- Selección de método de pago:
  - 💳 Tarjeta de crédito (con validación de número, expiración y CVV)
  - 🅿️ PayPal (con email de cuenta)
  - 🏦 Transferencia bancaria (con IBAN)
- Validación condicional según método seleccionado
- Confirmación final de la orden

### 📦 Gestión de Órdenes

- Creación automática de órdenes desde el carrito
- Guardado de snapshot de productos (precio y nombre)
- Tracking de estado de pago
- Generación de número de orden único
- Almacenamiento completo de información de facturación y envío

## 🌐 Endpoints de la Aplicación

### Controlador de Carrito (`CartController`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/cart` | Mostrar el carrito de compras |
| POST | `/cart/addproduct` | Añadir producto al carrito |
| POST | `/cart/inc/{id}` | Incrementar cantidad de un producto |
| POST | `/cart/dec/{id}` | Decrementar cantidad de un producto |
| POST | `/cart/remove/{id}` | Eliminar producto del carrito |
| POST | `/cart/applycoupon` | Aplicar cupón de descuento |
| POST | `/cart/checkout` | Iniciar proceso de checkout |

### Controlador de Checkout (`CheckoutController`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/checkout/step2` | Formulario de dirección (Paso 2) |
| POST | `/checkout/step2` | Procesar dirección de facturación/envío |
| GET | `/checkout/step3` | Formulario de pago (Paso 3) |
| POST | `/checkout/step3` | Procesar pago y finalizar orden |
| GET | `/order/confirmation/{id}` | Página de confirmación de orden |

## 🔄 Flujo de Compra

```
┌─────────────────┐
│  Añadir         │
│  Productos      │
│  al Carrito     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  PASO 1         │
│  Carrito        │
│  - Modificar    │
│  - Cupones      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  PASO 2         │
│  Facturación    │
│  y Envío        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  PASO 3         │
│  Pago           │
│  - Tarjeta      │
│  - PayPal       │
│  - Transferencia│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Confirmación   │
│  de Orden       │
└─────────────────┘
```

## 💻 Uso de la Aplicación

### 1. Explorar el Catálogo
Accede a `/cart` para ver los productos disponibles y tu carrito actual.

### 2. Añadir Productos
Selecciona un producto del dropdown, especifica la cantidad y haz clic en "Add to cart".

### 3. Gestionar el Carrito
- Usa los botones **+** y **-** para ajustar cantidades
- Haz clic en **Remove** para eliminar productos
- Introduce un código de cupón en el campo correspondiente y aplícalo

### 4. Proceder al Checkout
Haz clic en **Checkout** cuando estés listo para comprar.

### 5. Completar Información
- **Paso 2**: Rellena tus datos de facturación y envío
- **Paso 3**: Selecciona método de pago y completa los detalles

### 6. Confirmación
Recibirás una página de confirmación con los detalles de tu orden.

## ⚙️ Configuración

### Variables de Entorno (Opcional)

Puedes configurar las siguientes variables de entorno en lugar de modificar `application.properties`:

```bash
# Base de datos
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/shopping_cart
export SPRING_DATASOURCE_USERNAME=tu_usuario
export SPRING_DATASOURCE_PASSWORD=tu_contraseña
```

### Configuración Avanzada

El archivo `application.properties` admite configuraciones adicionales de Spring Boot como:
- Nivel de logging
- Puerto del servidor
- Configuración de pool de conexiones
- Etc.

## 🚀 Ejecución

### Usando Maven Wrapper (Recomendado)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Unix/Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Usando Maven Instalado

```bash
mvn spring-boot:run
```

### Compilar y Ejecutar JAR

```bash
mvn clean package
java -jar target/shopping_chart-0.0.1-SNAPSHOT.jar
```

### Acceder a la Aplicación

Una vez iniciada, abre tu navegador en:

```
http://localhost:8080/cart
```

## 🧪 Testing

Ejecutar los tests:

```bash
mvnw test
```

## 📝 Notas Importantes

- **Carrito en Sesión**: El carrito se mantiene en la sesión HTTP mediante `@SessionAttributes("cart")`. Se limpia al cerrar el navegador o expirar la sesión.

- **Validación Condicional**: Los formularios implementan validación condicional según el contexto (ej: campos de tarjeta solo se validan si se selecciona pago con tarjeta).

- **Snapshots de Productos**: Los precios y nombres de productos se guardan en `order_item` para mantener histórico, incluso si el producto cambia posteriormente.

- **Cupones**: La validación de cupones verifica:
  - Existencia del código
  - Estado activo
  - Vigencia (fecha válida)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es un ejercicio educativo desarrollado para IES Vega de Mijas.

## 👥 Autores

Desarrollado como proyecto educativo de Spring Boot y aplicaciones web empresariales.

Por Marcos Bernal.

---

⭐ Si este proyecto te ha sido útil, considera darle una estrella en GitHub!

