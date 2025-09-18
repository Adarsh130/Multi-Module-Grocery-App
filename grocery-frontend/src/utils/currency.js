/**
 * Currency utility functions for Indian Rupee formatting
 */

export const formatCurrency = (amount) => {
  if (amount === null || amount === undefined) return '₹0';
  
  const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
  
  if (isNaN(numAmount)) return '₹0';
  
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(numAmount);
};

export const formatPrice = (amount) => {
  if (amount === null || amount === undefined) return '₹0';
  
  const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
  
  if (isNaN(numAmount)) return '₹0';
  
  // Format with Indian number system
  return `₹${numAmount.toLocaleString('en-IN')}`;
};

export const formatCompactPrice = (amount) => {
  if (amount === null || amount === undefined) return '₹0';
  
  const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
  
  if (isNaN(numAmount)) return '₹0';
  
  if (numAmount >= 10000000) {
    return `₹${(numAmount / 10000000).toFixed(1)}Cr`;
  } else if (numAmount >= 100000) {
    return `₹${(numAmount / 100000).toFixed(1)}L`;
  } else if (numAmount >= 1000) {
    return `₹${(numAmount / 1000).toFixed(1)}K`;
  }
  
  return `₹${numAmount.toLocaleString('en-IN')}`;
};

export const calculateDiscount = (originalPrice, salePrice) => {
  if (!originalPrice || !salePrice) return 0;
  
  const original = typeof originalPrice === 'string' ? parseFloat(originalPrice) : originalPrice;
  const sale = typeof salePrice === 'string' ? parseFloat(salePrice) : salePrice;
  
  if (original <= sale) return 0;
  
  return Math.round(((original - sale) / original) * 100);
};

export const formatDiscountPrice = (originalPrice, discountPercent) => {
  if (!originalPrice || !discountPercent) return formatPrice(originalPrice);
  
  const original = typeof originalPrice === 'string' ? parseFloat(originalPrice) : originalPrice;
  const discount = typeof discountPercent === 'string' ? parseFloat(discountPercent) : discountPercent;
  
  const discountedPrice = original * (1 - discount / 100);
  
  return formatPrice(discountedPrice);
};