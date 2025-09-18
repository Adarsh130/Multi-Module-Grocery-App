import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  ShoppingCartIcon, 
  UserIcon, 
  Bars3Icon, 
  XMarkIcon,
  ChevronDownIcon,
  MapPinIcon
} from '@heroicons/react/24/outline';
import { useAuth } from '../../context/AuthContext';
import { useCart } from '../../context/CartContext';
import SearchBar from './SearchBar';
import NotificationCenter from './NotificationCenter';

const Header = () => {
  const { user, isAuthenticated, logout, isAdmin, isManager } = useAuth();
  const { totalItems } = useCart();
  const navigate = useNavigate();
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const [showLocationDropdown, setShowLocationDropdown] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
    setShowMobileMenu(false);
  };

  const toggleMenu = () => {
    setShowMobileMenu(!showMobileMenu);
  };

  const categories = [
    'Fresh Produce',
    'Dairy & Eggs', 
    'Meat & Seafood',
    'Bakery',
    'Pantry Essentials',
    'Beverages',
    'Snacks & Sweets',
    'Frozen Foods',
    'Health & Beauty'
  ];

  return (
    <>
      {/* Main Header */}
      <header className="bg-primary-500 shadow-lg sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-14">
            {/* Logo */}
            <div className="flex-shrink-0">
              <Link to="/" className="flex items-center">
                <div className="text-white">
                  <svg width="32" height="32" viewBox="0 0 32 32" className="fill-current">
                    <path d="M8 4h16l2 4v16a4 4 0 01-4 4H10a4 4 0 01-4-4V8l2-4z" stroke="currentColor" strokeWidth="2" fill="none"/>
                    <path d="M8 8h16" stroke="currentColor" strokeWidth="2"/>
                    <circle cx="12" cy="20" r="2" fill="currentColor"/>
                    <circle cx="20" cy="20" r="2" fill="currentColor"/>
                  </svg>
                </div>
                <div className="ml-2">
                  <span className="text-white text-lg font-bold">Grocery App</span>
                </div>
              </Link>
            </div>

            {/* Location */}
            <div className="hidden lg:flex items-center relative">
              <button 
                className="flex items-center text-white hover:bg-primary-600 px-3 py-2 rounded"
                onClick={() => setShowLocationDropdown(!showLocationDropdown)}
              >
                <MapPinIcon className="w-4 h-4 mr-1" />
                <span className="text-sm">Deliver to</span>
                <ChevronDownIcon className="w-4 h-4 ml-1" />
              </button>
              
              {showLocationDropdown && (
                <div className="absolute top-full left-0 mt-1 w-64 bg-white rounded-lg shadow-lg border z-50">
                  <div className="p-4">
                    <div className="flex items-center mb-3">
                      <MapPinIcon className="w-5 h-5 text-primary-500 mr-2" />
                      <span className="font-medium text-gray-900">Select Delivery Location</span>
                    </div>
                    <div className="space-y-2">
                      <div className="p-2 hover:bg-gray-50 rounded cursor-pointer">
                        <div className="font-medium text-sm">Mumbai, Maharashtra</div>
                        <div className="text-xs text-gray-600">400001</div>
                      </div>
                      <div className="p-2 hover:bg-gray-50 rounded cursor-pointer">
                        <div className="font-medium text-sm">Delhi, Delhi</div>
                        <div className="text-xs text-gray-600">110001</div>
                      </div>
                      <div className="p-2 hover:bg-gray-50 rounded cursor-pointer">
                        <div className="font-medium text-sm">Bangalore, Karnataka</div>
                        <div className="text-xs text-gray-600">560001</div>
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* Search Bar */}
            <div className="flex-1 max-w-2xl mx-4">
              <SearchBar />
            </div>

            {/* Right Side Actions */}
            <div className="hidden md:flex items-center space-x-6">
              {/* Login/User */}
              {isAuthenticated ? (
                <div className="relative group">
                  <button className="flex items-center text-white hover:bg-primary-600 px-3 py-2 rounded">
                    <UserIcon className="h-5 w-5 mr-1" />
                    <span className="text-sm font-medium">{user?.firstName || user?.username}</span>
                    <ChevronDownIcon className="w-4 h-4 ml-1" />
                  </button>
                  
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                    <Link
                      to="/profile"
                      className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      My Profile
                    </Link>
                    <Link
                      to="/orders"
                      className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      Orders
                    </Link>
                    <Link
                      to="/wishlist"
                      className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      Wishlist
                    </Link>
                    {(isAdmin() || isManager()) && (
                      <Link
                        to="/admin"
                        className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                      >
                        Admin Panel
                      </Link>
                    )}
                    <button
                      onClick={handleLogout}
                      className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      Logout
                    </button>
                  </div>
                </div>
              ) : (
                <Link
                  to="/login"
                  className="text-white hover:bg-primary-600 px-3 py-2 rounded text-sm font-medium"
                >
                  Login
                </Link>
              )}

              {/* More Dropdown */}
              <div className="relative group">
                <button className="flex items-center text-white hover:bg-primary-600 px-3 py-2 rounded">
                  <span className="text-sm font-medium">More</span>
                  <ChevronDownIcon className="w-4 h-4 ml-1" />
                </button>
                
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                  {/* Notifications */}
                  {isAuthenticated && (
                    <div className="px-4 py-2">
                      <NotificationCenter />
                    </div>
                  )}
                  <Link
                    to="/customer-care"
                    className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    24x7 Customer Care
                  </Link>
                  <Link
                    to="/advertise"
                    className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Advertise
                  </Link>
                  <Link
                    to="/download-app"
                    className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Download App
                  </Link>
                </div>
              </div>

              {/* Cart */}
              <Link
                to="/cart"
                className="relative flex items-center text-white hover:bg-primary-600 px-3 py-2 rounded"
              >
                <ShoppingCartIcon className="w-5 h-5 mr-1" />
                <span className="text-sm font-medium">Cart</span>
                {totalItems > 0 && (
                  <span className="absolute -top-1 -right-1 bg-secondary-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                    {totalItems > 9 ? '9+' : totalItems}
                  </span>
                )}
              </Link>
            </div>

            {/* Mobile menu button */}
            <div className="md:hidden">
              <button
                onClick={toggleMenu}
                className="p-2 rounded-md text-white hover:bg-primary-600"
              >
                {showMobileMenu ? (
                  <XMarkIcon className="h-6 w-6" />
                ) : (
                  <Bars3Icon className="h-6 w-6" />
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Categories Bar */}
        <div className="hidden lg:block bg-white border-t border-gray-200">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-10">
              {categories.map((category, index) => (
                <Link
                  key={index}
                  to={`/products?category=${category.toLowerCase().replace(/[^a-z0-9]/g, '-')}`}
                  className="text-sm text-gray-700 hover:text-primary-500 font-medium px-2 py-2 transition-colors duration-200"
                >
                  {category}
                </Link>
              ))}
            </div>
          </div>
        </div>

        {/* Mobile Navigation Menu */}
        {showMobileMenu && (
          <div className="md:hidden bg-white border-t border-gray-200">
            <div className="px-2 pt-2 pb-3 space-y-1">
              {/* Mobile Search */}
              <div className="px-3 py-2">
                <SearchBar />
              </div>

              {/* Mobile Categories */}
              {categories.map((category, index) => (
                <Link
                  key={index}
                  to={`/products?category=${category.toLowerCase().replace(/[^a-z0-9]/g, '-')}`}
                  className="block px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                  onClick={() => setShowMobileMenu(false)}
                >
                  {category}
                </Link>
              ))}

              {/* Mobile User Actions */}
              <div className="border-t border-gray-200 pt-2">
                <Link
                  to="/cart"
                  className="flex items-center px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                  onClick={() => setShowMobileMenu(false)}
                >
                  <ShoppingCartIcon className="h-5 w-5 mr-2" />
                  Cart
                  {totalItems > 0 && (
                    <span className="ml-2 bg-primary-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                      {totalItems}
                    </span>
                  )}
                </Link>

                {isAuthenticated ? (
                  <>
                    <Link
                      to="/profile"
                      className="block px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                      onClick={() => setShowMobileMenu(false)}
                    >
                      My Profile
                    </Link>
                    <Link
                      to="/orders"
                      className="block px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                      onClick={() => setShowMobileMenu(false)}
                    >
                      Orders
                    </Link>
                    <button
                      onClick={handleLogout}
                      className="block w-full text-left px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                    >
                      Logout
                    </button>
                  </>
                ) : (
                  <Link
                    to="/login"
                    className="block px-3 py-2 text-gray-700 hover:text-primary-500 hover:bg-gray-100 rounded-md"
                    onClick={() => setShowMobileMenu(false)}
                  >
                    Login
                  </Link>
                )}
              </div>
            </div>
          </div>
        )}
      </header>
    </>
  );
};

export default Header;