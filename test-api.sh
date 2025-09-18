#!/bin/bash

# Grocery App API Test Script
# This script tests the main functionality of the Grocery App API

BASE_URL="http://localhost:8080/api"
echo "🛒 Testing Grocery App API at $BASE_URL"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Test 1: Check if application is running
echo -e "${YELLOW}1. Testing application health...${NC}"
curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/products" | grep -q "200"
print_result $? "Application is running"

# Test 2: Get all products (public endpoint)
echo -e "${YELLOW}2. Testing public products endpoint...${NC}"
PRODUCTS_RESPONSE=$(curl -s "$BASE_URL/products")
echo "$PRODUCTS_RESPONSE" | grep -q "success"
print_result $? "Products endpoint accessible"

# Test 3: Login as admin
echo -e "${YELLOW}3. Testing admin login...${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}')

TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "$LOGIN_RESPONSE" | grep -q "Login successful"
print_result $? "Admin login successful"

if [ -n "$TOKEN" ]; then
    echo -e "${GREEN}🔑 JWT Token obtained${NC}"
    
    # Test 4: Get all users (admin only)
    echo -e "${YELLOW}4. Testing admin-only endpoint...${NC}"
    USERS_RESPONSE=$(curl -s "$BASE_URL/users" \
      -H "Authorization: Bearer $TOKEN")
    echo "$USERS_RESPONSE" | grep -q "success"
    print_result $? "Admin can access users endpoint"
    
    # Test 5: Create a new product
    echo -e "${YELLOW}5. Testing product creation...${NC}"
    CREATE_PRODUCT_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" \
      -d '{
        "name": "Test Product",
        "quantity": 50,
        "price": 9.99,
        "category": "Test Category"
      }')
    echo "$CREATE_PRODUCT_RESPONSE" | grep -q "success"
    print_result $? "Product creation successful"
    
    # Test 6: Get current user info
    echo -e "${YELLOW}6. Testing current user endpoint...${NC}"
    ME_RESPONSE=$(curl -s "$BASE_URL/auth/me" \
      -H "Authorization: Bearer $TOKEN")
    echo "$ME_RESPONSE" | grep -q "admin"
    print_result $? "Current user info retrieved"
    
else
    echo -e "${RED}❌ Could not obtain JWT token, skipping authenticated tests${NC}"
fi

# Test 7: Test customer login
echo -e "${YELLOW}7. Testing customer login...${NC}"
CUSTOMER_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "customer", "password": "customer123"}')

CUSTOMER_TOKEN=$(echo "$CUSTOMER_LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "$CUSTOMER_LOGIN_RESPONSE" | grep -q "Login successful"
print_result $? "Customer login successful"

# Test 8: Test unauthorized access
echo -e "${YELLOW}8. Testing unauthorized access...${NC}"
UNAUTHORIZED_RESPONSE=$(curl -s -w "%{http_code}" "$BASE_URL/users" | tail -n1)
[ "$UNAUTHORIZED_RESPONSE" = "401" ]
print_result $? "Unauthorized access properly blocked"

# Test 9: Test invalid login
echo -e "${YELLOW}9. Testing invalid login...${NC}"
INVALID_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "invalid", "password": "invalid"}')
echo "$INVALID_LOGIN_RESPONSE" | grep -q "Invalid username or password"
print_result $? "Invalid login properly rejected"

# Test 10: Test product categories
echo -e "${YELLOW}10. Testing product categories...${NC}"
FRUITS_RESPONSE=$(curl -s "$BASE_URL/products/category/Fruits")
echo "$FRUITS_RESPONSE" | grep -q "success"
print_result $? "Product category filtering works"

echo ""
echo "=================================================="
echo -e "${GREEN}🎉 API Testing Complete!${NC}"
echo ""
echo "📝 Test Summary:"
echo "- Application health check"
echo "- Public endpoints access"
echo "- Authentication (admin & customer)"
echo "- Authorization (role-based access)"
echo "- CRUD operations"
echo "- Error handling"
echo ""
echo "🔗 Useful URLs:"
echo "- API Base: $BASE_URL"
echo "- Products: $BASE_URL/products"
echo "- Login: $BASE_URL/auth/login"
echo ""
echo "📚 For complete API documentation, see API_DOCUMENTATION.md"