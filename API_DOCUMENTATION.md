# Grocery App API Documentation

## Overview
The Grocery App is a comprehensive REST API for managing a grocery store with features including user authentication, product management, shopping cart, and order processing.

## Base URL
```
http://localhost:8080/api
```

## Authentication
The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Default Users
The application comes with pre-configured users for testing:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@groceryapp.com |
| manager | manager123 | MANAGER | manager@groceryapp.com |
| customer | customer123 | CUSTOMER | customer@groceryapp.com |

## API Endpoints

### 🔐 Authentication Endpoints

#### POST /api/auth/login
Login with username and password.

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "admin",
    "email": "admin@groceryapp.com",
    "fullName": "System Administrator",
    "roles": ["ADMIN"],
    "expiresIn": 86400000
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

#### POST /api/auth/register
Register a new user.

**Request Body:**
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "fullName": "New User",
  "phoneNumber": "1234567890"
}
```

#### GET /api/auth/me
Get current user information (requires authentication).

---

### 🛍️ Product Endpoints

#### GET /api/products
Get all products (public access).

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": "product-id",
      "name": "Apple",
      "quantity": 50,
      "price": 2.50,
      "category": "Fruits"
    }
  ]
}
```

#### GET /api/products/{id}
Get product by ID.

#### POST /api/products
Create a new product (Admin/Manager only).

**Request Body:**
```json
{
  "name": "New Product",
  "quantity": 100,
  "price": 5.99,
  "category": "Category Name"
}
```

#### PUT /api/products/{id}
Update a product (Admin/Manager only).

#### DELETE /api/products/{id}
Delete a product (Admin only).

#### GET /api/products/category/{category}
Get products by category.

---

### 👥 Customer Endpoints

#### GET /api/customers
Get all customers (Admin/Manager only).

#### GET /api/customers/{id}
Get customer by ID (Admin/Manager or own profile).

#### POST /api/customers
Create a new customer (Admin/Manager only).

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "1234567890"
}
```

#### PUT /api/customers/{id}
Update customer (Admin/Manager or own profile).

#### DELETE /api/customers/{id}
Delete customer (Admin only).

---

### 🛒 Shopping Cart Endpoints

#### GET /api/cart/{customerId}
Get shopping cart for customer.

#### POST /api/cart/{customerId}/items
Add item to cart.

**Request Body:**
```json
{
  "productId": "product-id",
  "quantity": 2
}
```

#### PUT /api/cart/{customerId}/items/{productId}
Update item quantity in cart.

**Query Parameters:**
- `quantity`: New quantity

#### DELETE /api/cart/{customerId}/items/{productId}
Remove item from cart.

#### DELETE /api/cart/{customerId}
Clear entire cart.

---

### 📦 Order Endpoints

#### GET /api/orders
Get all orders (Admin/Manager only).

#### GET /api/orders/{id}
Get order by ID.

#### GET /api/orders/customer/{customerId}
Get orders for specific customer.

#### GET /api/orders/status/{status}
Get orders by status (Admin/Manager only).

**Available Statuses:**
- PENDING
- CONFIRMED
- PROCESSING
- SHIPPED
- DELIVERED
- CANCELLED
- RETURNED

#### POST /api/orders
Create a new order.

**Request Body:**
```json
{
  "customerId": "customer-id",
  "customerName": "John Doe",
  "items": [
    {
      "productId": "product-id",
      "quantity": 2
    }
  ],
  "paymentMethod": "CREDIT_CARD",
  "deliveryAddress": "123 Main St, City, State",
  "notes": "Leave at door"
}
```

#### PUT /api/orders/{id}/status
Update order status (Admin/Manager only).

**Query Parameters:**
- `status`: New status

#### PUT /api/orders/{id}/payment-status
Update payment status (Admin/Manager only).

**Query Parameters:**
- `paymentStatus`: New payment status (PENDING, PAID, FAILED, REFUNDED, CANCELLED)

#### DELETE /api/orders/{id}
Cancel order.

---

### 👤 User Management Endpoints

#### GET /api/users
Get all users (Admin only).

#### GET /api/users/{id}
Get user by ID (Admin or own profile).

#### POST /api/users
Create a new user (Admin only).

#### PUT /api/users/{id}
Update user (Admin or own profile).

#### DELETE /api/users/{id}
Delete user (Admin only).

---

## Error Responses

All error responses follow this format:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-01T10:00:00",
  "path": "uri=/api/endpoint"
}
```

### Common HTTP Status Codes

- `200 OK` - Success
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Sample Workflows

### 1. User Registration and Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 2. Shopping Workflow
```bash
# Get products
curl http://localhost:8080/api/products

# Add to cart
curl -X POST http://localhost:8080/api/cart/customer-id/items \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "product-id",
    "quantity": 2
  }'

# Create order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "customer-id",
    "items": [{"productId": "product-id", "quantity": 2}],
    "paymentMethod": "CREDIT_CARD"
  }'
```

### 3. Admin Operations
```bash
# Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Add new product
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <admin-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Product",
    "quantity": 100,
    "price": 9.99,
    "category": "Electronics"
  }'

# Update order status
curl -X PUT "http://localhost:8080/api/orders/order-id/status?status=SHIPPED" \
  -H "Authorization: Bearer <admin-token>"
```

## Security Features

1. **JWT Authentication** - Secure token-based authentication
2. **Role-based Access Control** - Different permissions for Admin, Manager, and Customer
3. **Password Encryption** - BCrypt password hashing
4. **CORS Support** - Cross-origin resource sharing enabled
5. **Input Validation** - Request data validation with detailed error messages

## Database Collections

The application uses MongoDB with the following collections:
- `users` - User accounts and authentication
- `products` - Product catalog
- `customers` - Customer information
- `categories` - Product categories
- `orders` - Order management
- `carts` - Shopping cart data

## Development Notes

- All timestamps are in ISO 8601 format
- Prices are stored as BigDecimal for precision
- Product quantities are automatically updated when orders are placed
- Cart data persists across sessions
- Orders can be cancelled only if not delivered
- Stock validation is performed before order creation