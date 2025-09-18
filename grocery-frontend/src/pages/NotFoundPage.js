import React from 'react';
import { Link } from 'react-router-dom';
import { HomeIcon, ShoppingCartIcon } from '@heroicons/react/24/outline';

const NotFoundPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="text-center">
          {/* 404 Illustration */}
          <div className="mb-8">
            <div className="text-9xl font-bold text-primary-600 mb-4">404</div>
            <div className="text-6xl mb-4">🛒</div>
          </div>
          
          <h1 className="text-3xl font-bold text-gray-900 mb-4">
            Oops! Page Not Found
          </h1>
          
          <p className="text-lg text-gray-600 mb-8 max-w-md mx-auto">
            The page you're looking for seems to have wandered off. 
            Don't worry, even our best products sometimes get misplaced!
          </p>
          
          {/* Action Buttons */}
          <div className="space-y-4 sm:space-y-0 sm:space-x-4 sm:flex sm:justify-center">
            <Link
              to="/"
              className="btn-primary inline-flex items-center justify-center w-full sm:w-auto"
            >
              <HomeIcon className="w-5 h-5 mr-2" />
              Go Home
            </Link>
            
            <Link
              to="/products"
              className="btn-secondary inline-flex items-center justify-center w-full sm:w-auto"
            >
              <ShoppingCartIcon className="w-5 h-5 mr-2" />
              Shop Products
            </Link>
          </div>
          
          {/* Help Text */}
          <div className="mt-12 pt-8 border-t border-gray-200">
            <p className="text-sm text-gray-500 mb-4">
              If you think this is a mistake, please contact our support team.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center text-sm">
              <Link
                to="/contact"
                className="text-primary-600 hover:text-primary-700 font-medium"
              >
                Contact Support
              </Link>
              
              <span className="hidden sm:inline text-gray-300">|</span>
              
              <Link
                to="/help"
                className="text-primary-600 hover:text-primary-700 font-medium"
              >
                Help Center
              </Link>
              
              <span className="hidden sm:inline text-gray-300">|</span>
              
              <button
                onClick={() => window.history.back()}
                className="text-primary-600 hover:text-primary-700 font-medium"
              >
                Go Back
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFoundPage;
