#!/bin/bash

echo "Testing compilation fixes..."

# Check if SecurityConfig uses deprecated methods
echo "Checking SecurityConfig for deprecated methods..."
if grep -q "\.cors()\.and()" grocery-security/src/main/java/com/groceryapp/security/config/SecurityConfig.java; then
    echo "❌ SecurityConfig still uses deprecated cors().and() method"
else
    echo "✅ SecurityConfig updated to use new lambda-based configuration"
fi

if grep -q "\.csrf()\.disable()" grocery-security/src/main/java/com/groceryapp/security/config/SecurityConfig.java; then
    echo "❌ SecurityConfig still uses deprecated csrf().disable() method"
else
    echo "✅ SecurityConfig updated to use new lambda-based CSRF configuration"
fi

# Check if duplicate Product model was removed
echo "Checking for duplicate Product models..."
if [ -d "grocery-service/src/main/java/com/groceryapp/persistence" ]; then
    echo "❌ Duplicate persistence package still exists in grocery-service"
else
    echo "✅ Duplicate persistence package removed from grocery-service"
fi

# Check if services use correct Product methods
echo "Checking service classes for correct Product method usage..."
if grep -q "product\.getQuantity()" grocery-service/src/main/java/com/groceryapp/service/OrderService.java; then
    echo "❌ OrderService still uses deprecated getQuantity() method"
else
    echo "✅ OrderService updated to use getStockQuantity()"
fi

if grep -q "product\.setQuantity(" grocery-service/src/main/java/com/groceryapp/service/OrderService.java; then
    echo "❌ OrderService still uses deprecated setQuantity() method"
else
    echo "✅ OrderService updated to use setStockQuantity()"
fi

echo "Compilation fix verification complete!"