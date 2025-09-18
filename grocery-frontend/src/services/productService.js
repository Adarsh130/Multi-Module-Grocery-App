import api from './api';

export const productService = {
  // Get all products
  getAllProducts: async () => {
    try {
      const response = await api.get('/products');
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get featured products
  getFeaturedProducts: async () => {
    try {
      const response = await api.get('/products/featured');
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get categories
  getCategories: async () => {
    try {
      const response = await api.get('/products/categories');
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get product by ID
  getProductById: async (id) => {
    try {
      const response = await api.get(`/products/${id}`);
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get products by category
  getProductsByCategory: async (category) => {
    try {
      const response = await api.get(`/products/category/${category}`);
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Admin product management
  createProduct: async (productData) => {
    try {
      // Map frontend fields to backend expected format
      const backendData = {
        name: productData.name,
        description: productData.description,
        price: parseFloat(productData.price),
        category: productData.category,
        stockQuantity: parseInt(productData.stockQuantity),
        quantity: parseInt(productData.stockQuantity), // Backend compatibility
        imageUrl: productData.imageUrl
      };
      
      const response = await api.post('/products', backendData);
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  updateProduct: async (productId, productData) => {
    try {
      // Map frontend fields to backend expected format
      const backendData = {
        name: productData.name,
        description: productData.description,
        price: parseFloat(productData.price),
        category: productData.category,
        stockQuantity: parseInt(productData.stockQuantity),
        quantity: parseInt(productData.stockQuantity), // Backend compatibility
        imageUrl: productData.imageUrl
      };
      
      const response = await api.put(`/products/${productId}`, backendData);
      return response;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  deleteProduct: async (productId) => {
    try {
      const response = await api.delete(`/products/${productId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Search products
  searchProducts: async (query, filters = {}) => {
    try {
      const params = new URLSearchParams();
      if (query) params.append('search', query);
      if (filters.category) params.append('category', filters.category);
      if (filters.minPrice) params.append('minPrice', filters.minPrice);
      if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);
      if (filters.inStock) params.append('inStock', filters.inStock);
      if (filters.sortBy) params.append('sortBy', filters.sortBy);
      if (filters.sortOrder) params.append('sortOrder', filters.sortOrder);
      
      const response = await api.get(`/products/search?${params.toString()}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get product reviews
  getProductReviews: async (productId) => {
    try {
      const response = await api.get(`/products/${productId}/reviews`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Add product review
  addProductReview: async (productId, reviewData) => {
    try {
      const response = await api.post(`/products/${productId}/reviews`, reviewData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Client-side utility functions
  filterByCategory: (products, category) => {
    if (!category || category === 'All') return products;
    return products.filter(product => product.category === category);
  },

  getCategoriesFromProducts: (products) => {
    const categories = [...new Set(products.map(product => product.category))];
    return ['All', ...categories.sort()];
  },

  sortProducts: (products, sortBy) => {
    const sorted = [...products];
    
    switch (sortBy) {
      case 'name':
        return sorted.sort((a, b) => a.name.localeCompare(b.name));
      case 'price-low':
        return sorted.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
      case 'price-high':
        return sorted.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
      case 'category':
        return sorted.sort((a, b) => a.category.localeCompare(b.category));
      default:
        return sorted;
    }
  }
};

export default productService;