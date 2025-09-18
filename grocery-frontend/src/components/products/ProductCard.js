import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { HeartIcon, StarIcon } from '@heroicons/react/24/outline';
import { HeartIcon as HeartIconSolid, StarIcon as StarIconSolid } from '@heroicons/react/24/solid';
import { useCart } from '../../context/CartContext';
import { useAuth } from '../../context/AuthContext';
import { formatPrice, calculateDiscount } from '../../utils/currency';
import { toast } from 'react-toastify';

const ProductCard = ({ product }) => {
  const { addToCart } = useCart();
  const { isAuthenticated } = useAuth();
  const [isWishlisted, setIsWishlisted] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Mock data for demonstration
  const originalPrice = product.price * 1.2; // 20% higher for discount display
  const discountPercent = calculateDiscount(originalPrice, product.price);
  const rating = 4.2; // Mock rating
  const reviewCount = Math.floor(Math.random() * 1000) + 100; // Mock review count
  const deliveryDate = new Date();
  deliveryDate.setDate(deliveryDate.getDate() + 2); // 2 days from now

  const handleAddToCart = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (!isAuthenticated) {
      toast.error('Please login to add items to cart');
      return;
    }

    setIsLoading(true);
    try {
      await addToCart(product, 1);
    } catch (error) {
      console.error('Error adding to cart:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleWishlist = (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (!isAuthenticated) {
      toast.error('Please login to add to wishlist');
      return;
    }

    setIsWishlisted(!isWishlisted);
    toast.success(isWishlisted ? 'Removed from wishlist' : 'Added to wishlist');
  };

  const getStockStatus = () => {
    const stock = product.stockQuantity || product.quantity || 0;
    if (stock === 0) return { text: 'Out of Stock', color: 'text-red-600' };
    if (stock < 10) return { text: `Only ${stock} left`, color: 'text-orange-600' };
    return { text: 'In Stock', color: 'text-green-600' };
  };

  const stockStatus = getStockStatus();

  return (
    <div className="group relative bg-white rounded-lg shadow-flipkart hover:shadow-flipkart-hover transition-all duration-300 overflow-hidden">
      <Link to={`/products/${product.id}`} className="block">
        {/* Wishlist Button */}
        <button
          onClick={handleWishlist}
          className="absolute top-3 right-3 z-10 p-2 rounded-full bg-white shadow-md hover:shadow-lg transition-all duration-200"
        >
          {isWishlisted ? (
            <HeartIconSolid className="w-5 h-5 text-red-500" />
          ) : (
            <HeartIcon className="w-5 h-5 text-gray-400 hover:text-red-500" />
          )}
        </button>

        {/* Discount Badge */}
        {discountPercent > 0 && (
          <div className="absolute top-3 left-3 z-10">
            <span className="bg-green-600 text-white text-xs font-bold px-2 py-1 rounded">
              {discountPercent}% OFF
            </span>
          </div>
        )}

        {/* Product Image */}
        <div className="aspect-square bg-gray-100 overflow-hidden">
          {product.imageUrl ? (
            <img
              src={product.imageUrl}
              alt={product.name}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center bg-gray-200">
              <span className="text-4xl font-bold text-gray-400">
                {product.name?.charAt(0) || 'P'}
              </span>
            </div>
          )}
        </div>

        {/* Product Info */}
        <div className="p-4">
          {/* Brand/Category */}
          <div className="text-xs text-gray-500 uppercase tracking-wide mb-1">
            {product.category}
          </div>

          {/* Product Name */}
          <h3 className="text-sm font-medium text-gray-900 mb-2 line-clamp-2 group-hover:text-primary-600 transition-colors">
            {product.name}
          </h3>

          {/* Rating */}
          <div className="flex items-center mb-2">
            <div className="flex items-center bg-green-600 text-white text-xs px-2 py-1 rounded">
              <span className="font-medium">{rating}</span>
              <StarIconSolid className="w-3 h-3 ml-1" />
            </div>
            <span className="text-xs text-gray-500 ml-2">
              ({reviewCount.toLocaleString()})
            </span>
          </div>

          {/* Price */}
          <div className="flex items-center mb-2">
            <span className="text-lg font-bold text-gray-900">
              {formatPrice(product.price)}
            </span>
            {discountPercent > 0 && (
              <>
                <span className="text-sm text-gray-500 line-through ml-2">
                  {formatPrice(originalPrice)}
                </span>
                <span className="text-sm text-green-600 font-medium ml-2">
                  {discountPercent}% off
                </span>
              </>
            )}
          </div>

          {/* Stock Status */}
          <div className={`text-xs font-medium mb-3 ${stockStatus.color}`}>
            {stockStatus.text}
          </div>

          {/* Delivery Info */}
          <div className="text-xs text-gray-600 mb-3">
            <span className="font-medium">Free delivery</span> by{' '}
            {deliveryDate.toLocaleDateString('en-IN', { 
              weekday: 'short', 
              month: 'short', 
              day: 'numeric' 
            })}
          </div>

          {/* Features */}
          <div className="flex flex-wrap gap-1 mb-3">
            <span className="text-xs bg-gray-100 text-gray-700 px-2 py-1 rounded">
              Assured
            </span>
            <span className="text-xs bg-gray-100 text-gray-700 px-2 py-1 rounded">
              7 Day Return
            </span>
          </div>
        </div>
      </Link>

      {/* Add to Cart Button */}
      <div className="p-4 pt-0">
        <button
          onClick={handleAddToCart}
          disabled={isLoading || (product.stockQuantity || product.quantity || 0) === 0}
          className="w-full bg-secondary-500 hover:bg-secondary-600 text-white font-medium py-2 px-4 rounded transition-colors duration-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          {isLoading ? (
            <div className="flex items-center justify-center">
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
              Adding...
            </div>
          ) : (product.stockQuantity || product.quantity || 0) === 0 ? (
            'Out of Stock'
          ) : (
            'Add to Cart'
          )}
        </button>
      </div>
    </div>
  );
};

export default ProductCard;