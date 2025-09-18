import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, XMarkIcon } from '@heroicons/react/24/outline';
import productService from '../../services/productService';
import LoadingSpinner from './LoadingSpinner';
import { formatPrice } from '../../utils/currency';

const SearchBar = ({ className = '' }) => {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const searchRef = useRef(null);
  const suggestionsRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        searchRef.current && 
        !searchRef.current.contains(event.target) &&
        suggestionsRef.current &&
        !suggestionsRef.current.contains(event.target)
      ) {
        setShowSuggestions(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    const searchProducts = async () => {
      if (query.length < 2) {
        setSuggestions([]);
        setShowSuggestions(false);
        return;
      }

      setLoading(true);
      try {
        const response = await productService.getAllProducts();
        const allProducts = response.data || [];
        
        // Filter products based on query
        const filtered = allProducts.filter(product =>
          product.name.toLowerCase().includes(query.toLowerCase()) ||
          product.category.toLowerCase().includes(query.toLowerCase()) ||
          (product.description && product.description.toLowerCase().includes(query.toLowerCase()))
        ).slice(0, 8);
        
        setSuggestions(filtered);
        setShowSuggestions(true);
      } catch (error) {
        console.error('Search failed:', error);
        setSuggestions([]);
      } finally {
        setLoading(false);
      }
    };

    const debounceTimer = setTimeout(searchProducts, 300);
    return () => clearTimeout(debounceTimer);
  }, [query]);

  const handleSearch = (searchQuery = query) => {
    if (searchQuery.trim()) {
      navigate(`/products?search=${encodeURIComponent(searchQuery.trim())}`);
      setShowSuggestions(false);
      setQuery('');
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSearch();
    } else if (e.key === 'Escape') {
      setShowSuggestions(false);
    }
  };

  const handleSuggestionClick = (product) => {
    navigate(`/products/${product.id}`);
    setShowSuggestions(false);
    setQuery('');
  };

  const clearSearch = () => {
    setQuery('');
    setSuggestions([]);
    setShowSuggestions(false);
  };

  const popularSearches = [
    'iPhone', 'Samsung Galaxy', 'Laptop', 'Headphones', 'Shoes', 'Shirts', 'Books', 'Groceries'
  ];

  return (
    <div className={`relative ${className}`}>
      <div ref={searchRef} className="relative">
        <div className="relative">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={handleKeyDown}
            onFocus={() => query.length >= 2 && setShowSuggestions(true)}
            placeholder="Search for products, brands and more"
            className="w-full pl-4 pr-12 py-2 bg-white border border-gray-300 rounded-sm focus:outline-none focus:border-primary-500 text-sm"
          />
          
          {/* Search Icon */}
          <button
            onClick={() => handleSearch()}
            className="absolute inset-y-0 right-0 pr-3 flex items-center text-primary-500 hover:text-primary-600"
          >
            <MagnifyingGlassIcon className="h-5 w-5" />
          </button>

          {/* Clear Button */}
          {query && (
            <button
              onClick={clearSearch}
              className="absolute inset-y-0 right-8 flex items-center text-gray-400 hover:text-gray-600"
            >
              <XMarkIcon className="h-4 w-4" />
            </button>
          )}

          {/* Loading Spinner */}
          {loading && (
            <div className="absolute inset-y-0 right-12 flex items-center">
              <LoadingSpinner size="sm" />
            </div>
          )}
        </div>

        {/* Search Suggestions */}
        {showSuggestions && (
          <div
            ref={suggestionsRef}
            className="absolute top-full left-0 right-0 mt-1 bg-white border border-gray-200 rounded-sm shadow-lg z-50 max-h-96 overflow-y-auto"
          >
            {suggestions.length > 0 ? (
              <>
                {/* Product Suggestions */}
                <div className="border-b border-gray-100">
                  <div className="px-4 py-2 bg-gray-50 text-xs font-medium text-gray-600 uppercase tracking-wide">
                    Products
                  </div>
                  {suggestions.map((product) => (
                    <button
                      key={product.id}
                      onClick={() => handleSuggestionClick(product)}
                      className="w-full px-4 py-3 text-left hover:bg-gray-50 border-b border-gray-100 last:border-b-0 flex items-center gap-3"
                    >
                      <div className="w-12 h-12 bg-gray-200 rounded flex items-center justify-center flex-shrink-0">
                        {product.imageUrl ? (
                          <img
                            src={product.imageUrl}
                            alt={product.name}
                            className="w-full h-full object-cover rounded"
                          />
                        ) : (
                          <span className="text-sm font-medium text-gray-600">
                            {product.name.charAt(0)}
                          </span>
                        )}
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-gray-900 truncate">
                          {product.name}
                        </p>
                        <div className="flex items-center gap-2">
                          <span className="text-sm font-bold text-gray-900">
                            {formatPrice(product.price)}
                          </span>
                          <span className="text-xs text-gray-500">
                            in {product.category}
                          </span>
                        </div>
                      </div>
                      <div className="text-xs text-green-600">
                        ★ 4.2
                      </div>
                    </button>
                  ))}
                </div>
                
                {/* View All Results */}
                <button
                  onClick={() => handleSearch()}
                  className="w-full px-4 py-3 text-left text-primary-600 hover:bg-primary-50 font-medium border-t border-gray-200 text-sm"
                >
                  View all results for "{query}"
                </button>
              </>
            ) : query.length >= 2 && !loading ? (
              <div className="px-4 py-6 text-center text-gray-500">
                <MagnifyingGlassIcon className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                <p className="text-sm">No products found for "{query}"</p>
                <button
                  onClick={() => handleSearch()}
                  className="mt-2 text-primary-600 hover:text-primary-700 font-medium text-sm"
                >
                  Search anyway
                </button>
              </div>
            ) : (
              /* Popular Searches */
              <div>
                <div className="px-4 py-2 bg-gray-50 text-xs font-medium text-gray-600 uppercase tracking-wide">
                  Popular Searches
                </div>
                <div className="p-4">
                  <div className="flex flex-wrap gap-2">
                    {popularSearches.map((search, index) => (
                      <button
                        key={index}
                        onClick={() => handleSearch(search)}
                        className="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-full transition-colors"
                      >
                        {search}
                      </button>
                    ))}
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchBar;