import React from 'react';
import { Link } from 'react-router-dom';
import { 
  ShoppingCartIcon, 
  PlusIcon, 
  MinusIcon, 
  TrashIcon,
  ShieldCheckIcon,
  TruckIcon,
  MapPinIcon
} from '@heroicons/react/24/outline';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { formatPrice } from '../utils/currency';

const CartPage = () => {
  const { items, totalAmount, totalItems, loading, updateCartItem, removeFromCart } = useCart();
  const { isAuthenticated } = useAuth();

  const handleQuantityChange = async (productId, newQuantity) => {
    if (newQuantity < 1) {
      await removeFromCart(productId);
    } else {
      await updateCartItem(productId, newQuantity);
    }
  };

  const handleRemoveItem = async (productId) => {
    await removeFromCart(productId);
  };

  // Calculate totals in INR
  const subtotal = totalAmount;
  const deliveryFee = subtotal >= 500 ? 0 : 99; // ₹99 delivery fee for orders under ₹500
  const gst = subtotal * 0.18; // 18% GST in India
  const total = subtotal + deliveryFee + gst;

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="max-w-md w-full bg-white rounded-lg shadow-flipkart p-8 text-center">
          <ShoppingCartIcon className="w-16 h-16 text-gray-300 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-900 mb-2">Please Login</h2>
          <p className="text-gray-600 mb-6">
            You need to login to view your cart items
          </p>
          <Link
            to="/login"
            className="bg-primary-500 hover:bg-primary-600 text-white px-6 py-2 rounded-sm font-medium"
          >
            Login
          </Link>
        </div>
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="bg-white rounded-lg shadow-flipkart p-12 text-center">
            <ShoppingCartIcon className="w-24 h-24 text-gray-300 mx-auto mb-6" />
            <h2 className="text-2xl font-semibold text-gray-900 mb-4">Your cart is empty</h2>
            <p className="text-gray-600 mb-8">
              Add items to it now
            </p>
            <Link
              to="/products"
              className="bg-primary-500 hover:bg-primary-600 text-white px-8 py-3 rounded-sm font-medium inline-flex items-center"
            >
              Shop now
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {/* Breadcrumb */}
        <div className="flex items-center text-sm text-gray-600 mb-6">
          <Link to="/" className="hover:text-primary-600">Home</Link>
          <span className="mx-2">/</span>
          <span className="text-gray-900 font-medium">My Cart</span>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Cart Items */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow-flipkart">
              {/* Header */}
              <div className="p-6 border-b border-gray-200">
                <div className="flex items-center justify-between">
                  <h1 className="text-xl font-semibold text-gray-900">
                    My Cart ({totalItems} item{totalItems !== 1 ? 's' : ''})
                  </h1>
                  <div className="flex items-center text-sm text-gray-600">
                    <MapPinIcon className="w-4 h-4 mr-1" />
                    <span>Deliver to 400001</span>
                  </div>
                </div>
              </div>

              {/* Cart Items List */}
              <div className="divide-y divide-gray-200">
                {items.map((item) => (
                  <div key={item.productId} className="p-6">
                    <div className="flex gap-4">
                      {/* Product Image */}
                      <div className="w-20 h-20 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
                        {item.imageUrl ? (
                          <img
                            src={item.imageUrl}
                            alt={item.productName}
                            className="w-full h-full object-cover"
                          />
                        ) : (
                          <div className="w-full h-full flex items-center justify-center bg-gray-200">
                            <span className="text-lg font-bold text-gray-400">
                              {item.productName?.charAt(0) || 'P'}
                            </span>
                          </div>
                        )}
                      </div>

                      {/* Product Details */}
                      <div className="flex-1">
                        <div className="flex justify-between">
                          <div className="flex-1">
                            <h3 className="text-lg font-medium text-gray-900 mb-1">
                              {item.productName}
                            </h3>
                            <p className="text-sm text-gray-600 mb-2">
                              {item.productCategory}
                            </p>
                            
                            {/* Price */}
                            <div className="flex items-center mb-3">
                              <span className="text-xl font-bold text-gray-900">
                                {formatPrice(item.unitPrice || item.price)}
                              </span>
                              <span className="text-lg text-gray-500 line-through ml-3">
                                {formatPrice((item.unitPrice || item.price) * 1.3)}
                              </span>
                              <span className="text-lg text-green-600 font-medium ml-3">
                                23% off
                              </span>
                            </div>

                            {/* Features */}
                            <div className="flex items-center gap-4 text-sm text-gray-600 mb-4">
                              <div className="flex items-center">
                                <TruckIcon className="w-4 h-4 mr-1" />
                                <span>Free delivery</span>
                              </div>
                              <div className="flex items-center">
                                <ShieldCheckIcon className="w-4 h-4 mr-1" />
                                <span>7 days return</span>
                              </div>
                            </div>

                            {/* Quantity Controls */}
                            <div className="flex items-center gap-4">
                              <div className="flex items-center border border-gray-300 rounded-sm">
                                <button
                                  onClick={() => handleQuantityChange(item.productId, item.quantity - 1)}
                                  className="p-2 hover:bg-gray-50 text-gray-600"
                                  disabled={loading}
                                >
                                  <MinusIcon className="w-4 h-4" />
                                </button>
                                <span className="px-4 py-2 border-x border-gray-300 min-w-[3rem] text-center">
                                  {item.quantity}
                                </span>
                                <button
                                  onClick={() => handleQuantityChange(item.productId, item.quantity + 1)}
                                  className="p-2 hover:bg-gray-50 text-gray-600"
                                  disabled={loading}
                                >
                                  <PlusIcon className="w-4 h-4" />
                                </button>
                              </div>

                              <button
                                onClick={() => handleRemoveItem(item.productId)}
                                className="flex items-center text-gray-600 hover:text-red-600 font-medium"
                                disabled={loading}
                              >
                                <TrashIcon className="w-4 h-4 mr-1" />
                                Remove
                              </button>
                            </div>
                          </div>

                          {/* Item Total */}
                          <div className="text-right">
                            <div className="text-xl font-bold text-gray-900">
                              {formatPrice((item.unitPrice || item.price) * item.quantity)}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              {/* Place Order Button - Mobile */}
              <div className="lg:hidden p-6 border-t border-gray-200">
                <Link
                  to="/checkout"
                  className="w-full bg-secondary-500 hover:bg-secondary-600 text-white py-3 px-6 rounded-sm font-semibold text-center block"
                >
                  Place Order
                </Link>
              </div>
            </div>
          </div>

          {/* Price Summary */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-flipkart p-6 sticky top-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Price Details</h3>
              
              <div className="space-y-3 mb-6">
                <div className="flex justify-between text-gray-700">
                  <span>Price ({totalItems} item{totalItems !== 1 ? 's' : ''})</span>
                  <span>{formatPrice(subtotal)}</span>
                </div>
                <div className="flex justify-between text-gray-700">
                  <span>Delivery Charges</span>
                  <span className={deliveryFee === 0 ? 'text-green-600' : ''}>
                    {deliveryFee === 0 ? (
                      <>
                        <span className="line-through text-gray-500">₹99</span>
                        <span className="ml-2">FREE</span>
                      </>
                    ) : (
                      formatPrice(deliveryFee)
                    )}
                  </span>
                </div>
                <div className="flex justify-between text-gray-700">
                  <span>Total GST</span>
                  <span>{formatPrice(gst)}</span>
                </div>
              </div>

              <div className="border-t border-gray-200 pt-4 mb-6">
                <div className="flex justify-between text-lg font-bold text-gray-900">
                  <span>Total Amount</span>
                  <span>{formatPrice(total)}</span>
                </div>
                {deliveryFee === 0 && (
                  <p className="text-sm text-green-600 mt-2">
                    You saved ₹99 on delivery!
                  </p>
                )}
              </div>

              {/* Safe and Secure */}
              <div className="bg-gray-50 rounded-lg p-4 mb-6">
                <div className="flex items-center text-sm text-gray-700">
                  <ShieldCheckIcon className="w-5 h-5 text-green-600 mr-2" />
                  <span>Safe and Secure Payments. Easy returns. 100% Authentic products.</span>
                </div>
              </div>

              {/* Place Order Button - Desktop */}
              <Link
                to="/checkout"
                className="w-full bg-secondary-500 hover:bg-secondary-600 text-white py-3 px-6 rounded-sm font-semibold text-center block transition-colors duration-200"
              >
                Place Order
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage;