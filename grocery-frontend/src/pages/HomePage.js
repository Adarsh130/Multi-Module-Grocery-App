import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  ShoppingCartIcon, 
  TruckIcon, 
  ShieldCheckIcon, 
  ClockIcon,
  ArrowRightIcon,
  StarIcon
} from '@heroicons/react/24/outline';
import ProductGrid from '../components/products/ProductGrid';
import LoadingSpinner from '../components/common/LoadingSpinner';
import productService from '../services/productService';
import { formatPrice } from '../utils/currency';

const HomePage = () => {
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadFeaturedProducts();
  }, []);

  const loadFeaturedProducts = async () => {
    try {
      setLoading(true);
      const response = await productService.getAllProducts();
      setFeaturedProducts(response.data || []);
    } catch (error) {
      console.error('Failed to load featured products:', error);
      setError('Failed to load featured products');
    } finally {
      setLoading(false);
    }
  };

  const categories = [
    {
      name: 'Fresh Produce',
      image: '🥬',
      link: '/products?category=fresh',
      offers: 'Up to 30% Off'
    },
    {
      name: 'Dairy & Eggs',
      image: '🥛',
      link: '/products?category=dairy',
      offers: 'Up to 25% Off'
    },
    {
      name: 'Meat & Seafood',
      image: '🍖',
      link: '/products?category=meat',
      offers: 'Fresh Daily'
    },
    {
      name: 'Bakery',
      image: '🍞',
      link: '/products?category=bakery',
      offers: 'Freshly Baked'
    },
    {
      name: 'Pantry',
      image: '🏺',
      link: '/products?category=pantry',
      offers: 'Up to 40% Off'
    },
    {
      name: 'Beverages',
      image: '🥤',
      link: '/products?category=beverages',
      offers: 'Up to 35% Off'
    },
    {
      name: 'Snacks',
      image: '🍿',
      link: '/products?category=snacks',
      offers: 'Buy 2 Get 1'
    },
    {
      name: 'Frozen Foods',
      image: '🧊',
      link: '/products?category=frozen',
      offers: 'Up to 45% Off'
    },
    {
      name: 'Health & Beauty',
      image: '💊',
      link: '/products?category=health',
      offers: 'Up to 50% Off'
    }
  ];

  const bannerOffers = [
    {
      title: 'Grocery',
      subtitle: 'Now get all your essentials delivered',
      image: 'https://images.unsplash.com/photo-1542838132-92c53300491e',
      cta: 'Shop Now'
    },
    {
      title: 'Electronics Sale',
      subtitle: 'Up to 80% off on Electronics',
      image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661',
      cta: 'Explore'
    },
    {
      title: 'Fashion Week',
      subtitle: 'Trending styles at best prices',
      image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8',
      cta: 'Shop Fashion'
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Banner Carousel */}
      <section className="bg-white">
        <div className="max-w-7xl mx-auto">
          <div className="grid grid-cols-1 lg:grid-cols-4 gap-0">
            {/* Categories Sidebar */}
            <div className="hidden lg:block bg-white border-r border-gray-200">
              <div className="p-4">
                <h3 className="font-semibold text-gray-900 mb-4">Top Categories</h3>
                <div className="space-y-2">
                  {categories.slice(0, 8).map((category, index) => (
                    <Link
                      key={index}
                      to={category.link}
                      className="flex items-center p-2 hover:bg-primary-50 rounded-lg transition-colors group"
                    >
                      <span className="text-2xl mr-3">{category.image}</span>
                      <div className="flex-1">
                        <div className="text-sm font-medium text-gray-900 group-hover:text-primary-600">
                          {category.name}
                        </div>
                        <div className="text-xs text-green-600">{category.offers}</div>
                      </div>
                    </Link>
                  ))}
                </div>
              </div>
            </div>

            {/* Main Banner */}
            <div className="lg:col-span-3">
              <div className="relative h-64 lg:h-80 overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-r from-primary-600 to-primary-800">
                  <div className="flex items-center justify-between h-full px-8">
                    <div className="text-white">
                      <h1 className="text-3xl lg:text-5xl font-bold mb-4">
                        Big Billion Days
                      </h1>
                      <p className="text-lg lg:text-xl mb-6 opacity-90">
                        The Biggest Sale of the Year
                      </p>
                      <Link
                        to="/products"
                        className="bg-secondary-500 hover:bg-secondary-600 text-white px-6 py-3 rounded-lg font-semibold transition-colors inline-flex items-center"
                      >
                        Shop Now
                        <ArrowRightIcon className="w-5 h-5 ml-2" />
                      </Link>
                    </div>
                    <div className="hidden lg:block">
                      <img
                        src="https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da"
                        alt="Sale"
                        className="w-64 h-48 object-cover rounded-lg"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Categories Grid */}
      <section className="py-8 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-9 gap-4">
            {categories.map((category, index) => (
              <Link
                key={index}
                to={category.link}
                className="group text-center p-4 hover:shadow-flipkart rounded-lg transition-all duration-200"
              >
                <div className="text-4xl mb-2">{category.image}</div>
                <h3 className="text-xs font-medium text-gray-900 group-hover:text-primary-600 mb-1">
                  {category.name}
                </h3>
                <p className="text-xs text-green-600">{category.offers}</p>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Deals of the Day */}
      <section className="py-8 bg-white border-t border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center">
              <h2 className="text-2xl font-bold text-gray-900">Deals of the Day</h2>
              <div className="ml-4 flex items-center text-sm text-gray-600">
                <ClockIcon className="w-4 h-4 mr-1" />
                <span>22h 15m Left</span>
              </div>
            </div>
            <Link
              to="/products"
              className="text-primary-600 hover:text-primary-700 font-medium flex items-center"
            >
              View All
              <ArrowRightIcon className="w-4 h-4 ml-1" />
            </Link>
          </div>

          {loading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size="lg" />
            </div>
          ) : error ? (
            <div className="text-center py-12">
              <p className="text-red-600 mb-4">{error}</p>
              <button
                onClick={loadFeaturedProducts}
                className="bg-primary-500 hover:bg-primary-600 text-white px-6 py-2 rounded-lg"
              >
                Try Again
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
              {featuredProducts.slice(0, 6).map((product) => (
                <Link
                  key={product.id}
                  to={`/products/${product.id}`}
                  className="group bg-white border border-gray-200 rounded-lg p-4 hover:shadow-flipkart transition-all duration-200"
                >
                  <div className="aspect-square bg-gray-100 rounded-lg mb-3 overflow-hidden">
                    {product.imageUrl ? (
                      <img
                        src={product.imageUrl}
                        alt={product.name}
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                    ) : (
                      <div className="w-full h-full flex items-center justify-center bg-gray-200">
                        <span className="text-2xl font-bold text-gray-400">
                          {product.name?.charAt(0) || 'P'}
                        </span>
                      </div>
                    )}
                  </div>
                  <h3 className="text-sm font-medium text-gray-900 mb-2 line-clamp-2">
                    {product.name}
                  </h3>
                  <div className="flex items-center mb-2">
                    <span className="text-lg font-bold text-gray-900">
                      {formatPrice(product.price)}
                    </span>
                    <span className="text-sm text-gray-500 line-through ml-2">
                      {formatPrice(product.price * 1.3)}
                    </span>
                  </div>
                  <div className="text-xs text-green-600 font-medium">
                    23% off
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* Featured Products */}
      <section className="py-8 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold text-gray-900">Suggested for You</h2>
            <Link
              to="/products"
              className="text-primary-600 hover:text-primary-700 font-medium flex items-center"
            >
              View All
              <ArrowRightIcon className="w-4 h-4 ml-1" />
            </Link>
          </div>

          {!loading && !error && (
            <ProductGrid products={featuredProducts.slice(0, 12)} />
          )}
        </div>
      </section>

      {/* Services Section */}
      <section className="py-8 bg-white border-t border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            {[
              {
                icon: TruckIcon,
                title: 'Free Delivery',
                description: 'Free delivery on orders above ₹499'
              },
              {
                icon: ClockIcon,
                title: 'Quick Service',
                description: 'Same day delivery available'
              },
              {
                icon: ShieldCheckIcon,
                title: 'Flipkart Assured',
                description: 'Quality products guaranteed'
              },
              {
                icon: StarIcon,
                title: '7 Day Returns',
                description: 'Easy returns & exchanges'
              }
            ].map((service, index) => (
              <div key={index} className="text-center p-4">
                <div className="inline-flex items-center justify-center w-12 h-12 bg-primary-100 text-primary-600 rounded-full mb-3">
                  <service.icon className="w-6 h-6" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">{service.title}</h3>
                <p className="text-gray-600 text-sm">{service.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Newsletter Section */}
      <section className="py-12 bg-primary-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-white mb-4">Stay Updated</h2>
            <p className="text-xl text-primary-100 mb-8 max-w-2xl mx-auto">
              Get notified about exclusive deals and new arrivals
            </p>
            
            <div className="max-w-md mx-auto">
              <div className="flex gap-4">
                <input
                  type="email"
                  placeholder="Enter your email"
                  className="flex-1 px-4 py-3 rounded-lg border-0 focus:ring-2 focus:ring-primary-300 focus:outline-none"
                />
                <button className="bg-secondary-500 hover:bg-secondary-600 text-white px-6 py-3 rounded-lg font-semibold transition-colors duration-200">
                  Subscribe
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;