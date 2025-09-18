import React, { createContext, useContext, useReducer, useEffect, useCallback } from 'react';
import cartService from '../services/cartService';
import { useAuth } from './AuthContext';
import { toast } from 'react-toastify';

// Initial state
const initialState = {
  items: [],
  totalItems: 0,
  totalAmount: 0,
  loading: false,
  error: null
};

// Action types
const CART_ACTIONS = {
  SET_LOADING: 'SET_LOADING',
  SET_CART: 'SET_CART',
  ADD_ITEM: 'ADD_ITEM',
  UPDATE_ITEM: 'UPDATE_ITEM',
  REMOVE_ITEM: 'REMOVE_ITEM',
  CLEAR_CART: 'CLEAR_CART',
  SET_ERROR: 'SET_ERROR',
  CLEAR_ERROR: 'CLEAR_ERROR'
};

// Reducer
const cartReducer = (state, action) => {
  switch (action.type) {
    case CART_ACTIONS.SET_LOADING:
      return {
        ...state,
        loading: action.payload
      };
    
    case CART_ACTIONS.SET_CART:
      const totals = cartService.calculateCartTotals(action.payload);
      return {
        ...state,
        items: action.payload,
        totalItems: totals.totalItems,
        totalAmount: totals.totalAmount,
        loading: false,
        error: null
      };
    
    case CART_ACTIONS.ADD_ITEM:
      const newItems = [...state.items];
      const existingItemIndex = newItems.findIndex(
        item => item.productId === action.payload.productId
      );
      
      if (existingItemIndex !== -1) {
        newItems[existingItemIndex].quantity += action.payload.quantity;
      } else {
        newItems.push(action.payload);
      }
      
      const addTotals = cartService.calculateCartTotals(newItems);
      return {
        ...state,
        items: newItems,
        totalItems: addTotals.totalItems,
        totalAmount: addTotals.totalAmount,
        error: null
      };
    
    case CART_ACTIONS.UPDATE_ITEM:
      const updatedItems = state.items.map(item =>
        item.productId === action.payload.productId
          ? { ...item, quantity: action.payload.quantity }
          : item
      );
      
      const updateTotals = cartService.calculateCartTotals(updatedItems);
      return {
        ...state,
        items: updatedItems,
        totalItems: updateTotals.totalItems,
        totalAmount: updateTotals.totalAmount,
        error: null
      };
    
    case CART_ACTIONS.REMOVE_ITEM:
      const filteredItems = state.items.filter(
        item => item.productId !== action.payload
      );
      
      const removeTotals = cartService.calculateCartTotals(filteredItems);
      return {
        ...state,
        items: filteredItems,
        totalItems: removeTotals.totalItems,
        totalAmount: removeTotals.totalAmount,
        error: null
      };
    
    case CART_ACTIONS.CLEAR_CART:
      return {
        ...state,
        items: [],
        totalItems: 0,
        totalAmount: 0,
        error: null
      };
    
    case CART_ACTIONS.SET_ERROR:
      return {
        ...state,
        error: action.payload,
        loading: false
      };
    
    case CART_ACTIONS.CLEAR_ERROR:
      return {
        ...state,
        error: null
      };
    
    default:
      return state;
  }
};

// Create context
const CartContext = createContext();

// Provider component
export const CartProvider = ({ children }) => {
  const [state, dispatch] = useReducer(cartReducer, initialState);
  const { user, isAuthenticated } = useAuth();

  // Load cart data
  const loadCart = useCallback(async () => {
    dispatch({ type: CART_ACTIONS.SET_LOADING, payload: true });
    
    try {
      if (isAuthenticated && user) {
        // Load cart from server for authenticated users
        const response = await cartService.getCart(user.username);
        dispatch({
          type: CART_ACTIONS.SET_CART,
          payload: response.data?.items || []
        });
      } else {
        // Load cart from localStorage for non-authenticated users
        const localCart = cartService.getLocalCart();
        dispatch({
          type: CART_ACTIONS.SET_CART,
          payload: localCart
        });
      }
    } catch (error) {
      console.error('Failed to load cart:', error);
      // Fallback to local cart
      const localCart = cartService.getLocalCart();
      dispatch({
        type: CART_ACTIONS.SET_CART,
        payload: localCart
      });
    }
  }, [user, isAuthenticated]);

  // Load cart on component mount or user change
  useEffect(() => {
    loadCart();
  }, [loadCart]);

  // Add item to cart
  const addToCart = async (product, quantity = 1) => {
    try {
      if (isAuthenticated && user) {
        // Calculate prices
        const unitPrice = parseFloat(product.price);
        const totalPrice = unitPrice * quantity;
        
        // Add to server cart
        await cartService.addToCart(user.username, {
          productId: product.id,
          productName: product.name,
          productCategory: product.category,
          quantity: quantity,
          unitPrice: unitPrice,
          totalPrice: totalPrice
        });
        
        // Reload cart from server
        await loadCart();
      } else {
        // Add to local cart
        const updatedCart = cartService.addToLocalCart(product, quantity);
        dispatch({
          type: CART_ACTIONS.SET_CART,
          payload: updatedCart
        });
      }
      
      toast.success(`${product.name} added to cart!`);
    } catch (error) {
      console.error('Failed to add to cart:', error);
      dispatch({
        type: CART_ACTIONS.SET_ERROR,
        payload: error.message || 'Failed to add item to cart'
      });
      toast.error('Failed to add item to cart');
    }
  };

  // Update item quantity
  const updateCartItem = async (productId, quantity) => {
    try {
      if (quantity <= 0) {
        await removeFromCart(productId);
        return;
      }

      if (isAuthenticated && user) {
        // Update server cart
        await cartService.updateCartItem(user.username, productId, quantity);
        await loadCart();
      } else {
        // Update local cart
        const updatedCart = cartService.updateLocalCartItem(productId, quantity);
        dispatch({
          type: CART_ACTIONS.SET_CART,
          payload: updatedCart
        });
      }
    } catch (error) {
      console.error('Failed to update cart item:', error);
      dispatch({
        type: CART_ACTIONS.SET_ERROR,
        payload: error.message || 'Failed to update cart item'
      });
      toast.error('Failed to update cart item');
    }
  };

  // Remove item from cart
  const removeFromCart = async (productId) => {
    try {
      if (isAuthenticated && user) {
        // Remove from server cart
        await cartService.removeFromCart(user.username, productId);
        await loadCart();
      } else {
        // Remove from local cart
        const updatedCart = cartService.removeFromLocalCart(productId);
        dispatch({
          type: CART_ACTIONS.SET_CART,
          payload: updatedCart
        });
      }
      
      toast.success('Item removed from cart');
    } catch (error) {
      console.error('Failed to remove from cart:', error);
      dispatch({
        type: CART_ACTIONS.SET_ERROR,
        payload: error.message || 'Failed to remove item from cart'
      });
      toast.error('Failed to remove item from cart');
    }
  };

  // Clear entire cart
  const clearCart = async () => {
    try {
      if (isAuthenticated && user) {
        // Clear server cart
        await cartService.clearCart(user.username);
      } else {
        // Clear local cart
        cartService.clearLocalCart();
      }
      
      dispatch({ type: CART_ACTIONS.CLEAR_CART });
      toast.success('Cart cleared');
    } catch (error) {
      console.error('Failed to clear cart:', error);
      dispatch({
        type: CART_ACTIONS.SET_ERROR,
        payload: error.message || 'Failed to clear cart'
      });
      toast.error('Failed to clear cart');
    }
  };

  // Get item quantity in cart
  const getItemQuantity = (productId) => {
    const item = state.items.find(item => item.productId === productId);
    return item ? item.quantity : 0;
  };

  // Check if item is in cart
  const isInCart = (productId) => {
    return state.items.some(item => item.productId === productId);
  };

  // Clear error
  const clearError = () => {
    dispatch({ type: CART_ACTIONS.CLEAR_ERROR });
  };

  const value = {
    ...state,
    addToCart,
    updateCartItem,
    removeFromCart,
    clearCart,
    getItemQuantity,
    isInCart,
    clearError,
    loadCart
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};

// Custom hook to use cart context
export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

export default CartContext;
