import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useCart } from '../../context/CartContext';
import { formatPrice } from '../../utils/currency';

const CartSummary = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { items, totalAmount, totalItems, clearCart } = useCart();

  const subtotal = totalAmount;
  const tax = subtotal * 0.18; // 18% GST
  const shipping = subtotal > 500 ? 0 : 99; // Free shipping over ₹500
  const total = subtotal + tax + shipping;

  const handleCheckout = () => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: '/checkout' } });
      return;
    }
    navigate('/checkout');
  };

  if (!items || items.length === 0) {
    return null;
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
      <h2 className="text-xl font-semibold text-gray-900 mb-4">Order Summary</h2>
      
      {/* Summary Details */}
      <div className="space-y-3 mb-6">
        <div className="flex justify-between text-gray-600">
          <span>Items ({totalItems})</span>
          <span>{formatPrice(subtotal)}</span>
        </div>
        
        <div className="flex justify-between text-gray-600">
          <span>GST (18%)</span>
          <span>{formatPrice(tax)}</span>
        </div>
        
        <div className="flex justify-between text-gray-600">
          <span>Shipping</span>
          <span>
            {shipping === 0 ? (
              <span className="text-success-600 font-medium">FREE</span>
            ) : (
              formatPrice(shipping)
            )}
          </span>
        </div>
        
        {shipping > 0 && (
          <p className="text-sm text-gray-500">
            Add {formatPrice(500 - subtotal)} more for free shipping
          </p>
        )}
        
        <div className="border-t border-gray-200 pt-3">
          <div className="flex justify-between text-lg font-semibold text-gray-900">
            <span>Total</span>
            <span>{formatPrice(total)}</span>
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="space-y-3">
        <button
          onClick={handleCheckout}
          className="w-full btn-primary text-lg py-3"
        >
          {isAuthenticated ? 'Proceed to Checkout' : 'Login to Checkout'}
        </button>
        
        <button
          onClick={clearCart}
          className="w-full btn-secondary"
        >
          Clear Cart
        </button>
      </div>

      {/* Security Badge */}
      <div className="mt-6 pt-4 border-t border-gray-200">
        <div className="flex items-center justify-center space-x-2 text-sm text-gray-500">
          <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clipRule="evenodd" />
          </svg>
          <span>Secure checkout</span>
        </div>
      </div>
    </div>
  );
};

export default CartSummary;
