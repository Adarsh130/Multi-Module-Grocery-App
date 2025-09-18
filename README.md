# 🛒 Grocery App - Complete Multi-Module Spring Boot Application

A comprehensive, production-ready multi-module Spring Boot application for managing a grocery store with features including user authentication, product management, shopping cart, order processing, and role-based access control.

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Role-based access control (Admin, Manager, Customer)
- Secure password encryption with BCrypt
- User registration and login

### 🛍️ Product Management
- Complete CRUD operations for products
- Category-based product organization
- Stock management with automatic quantity updates
- Product search and filtering

### 🛒 Shopping Cart
- Persistent shopping cart per customer
- Add, update, and remove items
- Real-time price calculations
- Stock validation

### 📦 Order Management
- Complete order lifecycle management
- Order status tracking (Pending → Delivered)
- Payment status management
- Order history and reporting

### 👥 User Management
- Customer profile management
- Admin user management
- Role assignment and permissions

## 🏗️ Architecture

### Multi-Module Structure
```
grocery-app/
├── pom.xml                    # Parent POM with dependency management
├── grocery-common/            # Shared DTOs, constants, exceptions, utilities
├── grocery-persistence/       # MongoDB models and repositories
├── grocery-service/          # Business logic and service layer
├── grocery-security/         # JWT authentication and Spring Security
└── grocery-api/              # REST controllers and main application
```

### 📦 Module Details

#### 1. grocery-common
**Shared Components**
- **DTOs**: ProductDto, CustomerDto, OrderDto, CartDto, UserDto, AuthDto
- **Enums**: OrderStatus, PaymentMethod, PaymentStatus, UserRole
- **Exceptions**: Custom exception classes
- **Constants**: API paths, error messages, roles
- **Utilities**: Date utilities, validation helpers

#### 2. grocery-persistence
**Data Access Layer**
- **Models**: Product, Customer, Order, Cart, User, Category
- **Repositories**: Spring Data MongoDB repositories
- **Database**: MongoDB with indexed collections

#### 3. grocery-service
**Business Logic Layer**
- **Services**: ProductService, CustomerService, OrderService, CartService, UserService, AuthService
- **Business Rules**: Validation, calculations, stock management
- **Transactions**: Order processing with stock updates

#### 4. grocery-security
**Security Layer**
- **JWT**: Token generation, validation, and filtering
- **Security Config**: Spring Security configuration
- **User Details**: Custom UserDetailsService implementation
- **Authentication**: Login/logout handling

#### 5. grocery-api
**Presentation Layer**
- **Controllers**: REST API endpoints
- **Exception Handling**: Global exception handler
- **Configuration**: Application properties and data initialization
- **Main Application**: Spring Boot entry point

## 🚀 Technologies Used

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.5** - Latest Spring Boot
- **Spring Data MongoDB** - Database integration
- **Spring Security** - Authentication and authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Maven Multi-Module** - Project structure
- **Lombok** - Boilerplate code reduction
- **MongoDB** - NoSQL database
- **BCrypt** - Password encryption

## 🛠️ Setup Instructions

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**
- **MongoDB 4.4+**
- **IDE** (IntelliJ IDEA recommended)

### 1. Clone and Build
```bash
git clone <repository-url>
cd grocery-app
mvn clean install
```

### 2. MongoDB Setup
```bash
# Start MongoDB service
mongod

# MongoDB will create the database automatically
# Default database name: grocerydb
```

### 3. Run the Application
```bash
# From the root directory
mvn spring-boot:run -pl grocery-api

# Or using Maven wrapper (if available)
./mvnw spring-boot:run -pl grocery-api
```

### 4. Access the Application
- **Base URL**: http://localhost:8080
- **API Base**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/products

## 🔑 Default Users

The application comes with pre-configured users for testing:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@groceryapp.com |
| manager | manager123 | MANAGER | manager@groceryapp.com |
| customer | customer123 | CUSTOMER | customer@groceryapp.com |

## 📋 API Endpoints

### 🔐 Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user info

### 🛍️ Products
- `GET /api/products` - Get all products (public)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (Admin/Manager)
- `PUT /api/products/{id}` - Update product (Admin/Manager)
- `DELETE /api/products/{id}` - Delete product (Admin)
- `GET /api/products/category/{category}` - Get products by category

### 👥 Customers
- `GET /api/customers` - Get all customers (Admin/Manager)
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create customer (Admin/Manager)
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer (Admin)

### 🛒 Shopping Cart
- `GET /api/cart/{customerId}` - Get customer's cart
- `POST /api/cart/{customerId}/items` - Add item to cart
- `PUT /api/cart/{customerId}/items/{productId}` - Update item quantity
- `DELETE /api/cart/{customerId}/items/{productId}` - Remove item
- `DELETE /api/cart/{customerId}` - Clear cart

### 📦 Orders
- `GET /api/orders` - Get all orders (Admin/Manager)
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/customer/{customerId}` - Get customer orders
- `POST /api/orders` - Create order
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

### 👤 User Management
- `GET /api/users` - Get all users (Admin)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user (Admin)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (Admin)

## 🧪 Testing the API

### 1. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

### 2. Use Token for Authenticated Requests
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 3. Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Product",
    "quantity": 100,
    "price": 9.99,
    "category": "Electronics"
  }'
```

## 🔒 Security Features

1. **JWT Authentication** - Stateless, secure token-based auth
2. **Role-based Access Control** - Fine-grained permissions
3. **Password Encryption** - BCrypt hashing
4. **CORS Support** - Cross-origin requests enabled
5. **Input Validation** - Comprehensive request validation
6. **Exception Handling** - Centralized error management

## 📊 Sample Data

The application automatically initializes with:
- **3 Users** (Admin, Manager, Customer)
- **10 Categories** (Fruits, Vegetables, Dairy, etc.)
- **20+ Products** across different categories

## 🔧 Configuration

### Application Properties
```properties
# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=grocerydb

# JWT
jwt.secret=mySecretKey
jwt.expiration=86400000

# Server
server.port=8080
```

## 🐳 Docker Support (Future)

```dockerfile
# Dockerfile example for future implementation
FROM openjdk:21-jdk-slim
COPY grocery-api/target/grocery-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📈 Performance Features

- **Database Indexing** - Optimized MongoDB queries
- **Connection Pooling** - Efficient database connections
- **Caching Ready** - Structure supports Redis integration
- **Pagination Support** - Ready for large datasets

## 🧪 Testing Strategy

- **Unit Tests** - Service layer testing
- **Integration Tests** - API endpoint testing
- **Security Tests** - Authentication and authorization
- **Performance Tests** - Load testing capabilities

## 📚 Documentation

- **API Documentation**: See `API_DOCUMENTATION.md`
- **Module Guide**: See `add-new-module-guide.md`
- **Inline Documentation**: Comprehensive JavaDoc comments

## 🚀 Deployment

### Production Checklist
- [ ] Update JWT secret key
- [ ] Configure production MongoDB
- [ ] Set up logging configuration
- [ ] Enable HTTPS
- [ ] Configure monitoring
- [ ] Set up backup strategy

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB team for the robust database
- JWT.io for authentication standards
- All contributors and testers

---

**Built with ❤️ using Spring Boot and modern Java practices**