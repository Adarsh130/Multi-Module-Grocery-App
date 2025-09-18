import React, { useState } from 'react';
import { useCart } from '../../context/CartContext';
import { PlusIcon, MinusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { toast } from 'react-toastify';
import { formatPrice } from '../../utils/currency';

const CartItem = ({ item }) => {
  const { updateCartItem, removeFromCart } = useCart();
  const [isLoading, setIsLoading] = useState(false);

  const handleUpdateQuantity = async (newQuantity) => {
    if (newQuantity < 1) return;
    
    setIsLoading(true);
    try {
      await updateCartItem(item.productId, newQuantity);
    } catch (error) {
      toast.error('Failed to update quantity');
    } finally {
      setIsLoading(false);
    }
  };

  const handleRemove = async () => {
    setIsLoading(true);
    try {
      await removeFromCart(item.productId);
    } catch (error) {
      toast.error('Failed to remove item');
    } finally {
      setIsLoading(false);
    }
  };

  const itemTotal = (parseFloat(item.unitPrice || item.price) * item.quantity);

  return (
    <div className="flex items-center space-x-4 p-4 bg-white rounded-lg shadow-sm border border-gray-200">
      {/* Product Image Placeholder */}
      <div className="w-16 h-16 bg-gradient-to-br from-primary-100 to-primary-200 rounded-lg flex items-center justify-center flex-shrink-0">
        <span className="text-primary-600 font-bold text-lg">
          {(item.productName || item.name || 'P').charAt(0).toUpperCase()}
        </span>
      </div>

      {/* Product Details */}
      <div className="flex-1 min-w-0">
        <h3 className="font-medium text-gray-900 truncate">
          {item.productName || item.name}
        </h3>
        <p className="text-sm text-gray-500">
          {formatPrice(item.unitPrice || item.price)} each
        </p>
        {item.category && (
          <p className="text-xs text-gray-400">
            {item.category}
          </p>
        )}
      </div>

      {/* Quantity Controls */}
      <div className="flex items-center space-x-2">
        <button
          onClick={() => handleUpdateQuantity(item.quantity - 1)}
          disabled={isLoading || item.quantity <= 1}
          className="w-8 h-8 rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200 flex items-center justify-center transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <MinusIcon className="w-4 h-4" />
        </button>
        
        <span className="font-medium text-lg min-w-[2rem] text-center">
          {item.quantity}
        </span>
        
        <button
          onClick={() => handleUpdateQuantity(item.quantity + 1)}
          disabled={isLoading}
          className="w-8 h-8 rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200 flex items-center justify-center transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <PlusIcon className="w-4 h-4" />
        </button>
      </div>

      {/* Item Total */}
      <div className="text-right">
        <p className="font-semibold text-gray-900">
          {formatPrice(itemTotal)}
        </p>
      </div>

      {/* Remove Button */}
      <button
        onClick={handleRemove}
        disabled={isLoading}
        className="p-2 text-error-600 hover:text-error-700 hover:bg-error-50 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        title="Remove item"
      >
        <TrashIcon className="w-5 h-5" />
      </button>
    </div>
  );
};

export default CartItem;
