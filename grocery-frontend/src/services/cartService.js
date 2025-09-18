import api from './api';

export const cartService = {
  // Get cart for customer
  getCart: async (customerId) => {
    try {
      const response = await api.get(`/cart/${customerId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Add item to cart
  addToCart: async (customerId, item) => {
    try {
      const response = await api.post(`/cart/${customerId}/items`, item);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Update item quantity in cart
  updateCartItem: async (customerId, productId, quantity) => {
    try {
      const response = await api.put(
        `/cart/${customerId}/items/${productId}?quantity=${quantity}`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Remove item from cart
  removeFromCart: async (customerId, productId) => {
    try {
      const response = await api.delete(`/cart/${customerId}/items/${productId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Clear entire cart
  clearCart: async (customerId) => {
    try {
      const response = await api.delete(`/cart/${customerId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Calculate cart totals
  calculateCartTotals: (cartItems) => {
    if (!cartItems || cartItems.length === 0) {
      return {
        totalItems: 0,
        totalAmount: 0,
        itemCount: 0
      };
    }

    const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalAmount = cartItems.reduce(
      (sum, item) => sum + (parseFloat(item.unitPrice || item.price) * item.quantity), 
      0
    );

    return {
      totalItems,
      totalAmount: parseFloat(totalAmount.toFixed(2)),
      itemCount: cartItems.length
    };
  },

  // Local cart management (for non-authenticated users)
  getLocalCart: () => {
    const cart = localStorage.getItem('localCart');
    return cart ? JSON.parse(cart) : [];
  },

  setLocalCart: (cartItems) => {
    localStorage.setItem('localCart', JSON.stringify(cartItems));
  },

  addToLocalCart: (product, quantity = 1) => {
    const cart = cartService.getLocalCart();
    const existingItem = cart.find(item => item.productId === product.id);

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      cart.push({
        productId: product.id,
        productName: product.name,
        price: product.price,
        quantity: quantity,
        category: product.category
      });
    }

    cartService.setLocalCart(cart);
    return cart;
  },

  updateLocalCartItem: (productId, quantity) => {
    const cart = cartService.getLocalCart();
    const itemIndex = cart.findIndex(item => item.productId === productId);

    if (itemIndex !== -1) {
      if (quantity <= 0) {
        cart.splice(itemIndex, 1);
      } else {
        cart[itemIndex].quantity = quantity;
      }
    }

    cartService.setLocalCart(cart);
    return cart;
  },

  removeFromLocalCart: (productId) => {
    const cart = cartService.getLocalCart();
    const filteredCart = cart.filter(item => item.productId !== productId);
    cartService.setLocalCart(filteredCart);
    return filteredCart;
  },

  clearLocalCart: () => {
    localStorage.removeItem('localCart');
    return [];
  }
};

export default cartService;
