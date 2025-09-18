# Grocery Store Frontend

A modern React frontend for the Grocery Store application built with React, Tailwind CSS, and modern web technologies.

## Features

- 🛒 **Product Catalog**: Browse and search through products with advanced filtering
- 🛍️ **Shopping Cart**: Add, remove, and manage items in your cart
- 👤 **User Authentication**: Login, register, and manage user profiles
- 📱 **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- 🎨 **Modern UI**: Clean, intuitive interface built with Tailwind CSS
- 🔒 **Protected Routes**: Secure pages that require authentication
- 👨‍💼 **Admin Dashboard**: Administrative features for managing products and orders
- 🔍 **Search & Filter**: Advanced product search and filtering capabilities
- 💳 **Checkout Process**: Streamlined checkout with payment integration
- 📦 **Order Management**: Track and manage your orders

## Tech Stack

- **React 18** - Modern React with hooks and functional components
- **React Router 6** - Client-side routing
- **Tailwind CSS** - Utility-first CSS framework
- **Heroicons** - Beautiful SVG icons
- **Axios** - HTTP client for API calls
- **React Toastify** - Toast notifications
- **Context API** - State management for auth and cart

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd grocery-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open your browser and visit `http://localhost:3000`

### Available Scripts

- `npm start` - Runs the app in development mode
- `npm build` - Builds the app for production
- `npm test` - Launches the test runner
- `npm eject` - Ejects from Create React App (one-way operation)

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── auth/           # Authentication components
│   ├── cart/           # Shopping cart components
│   ├── common/         # Common/shared components
│   └── products/       # Product-related components
├── context/            # React Context providers
│   ├── AuthContext.js  # Authentication state management
│   └── CartContext.js  # Shopping cart state management
├── pages/              # Page components
│   ├── admin/          # Admin dashboard pages
│   ├── HomePage.js     # Landing page
│   ├── ProductsPage.js # Product catalog
│   ├── CartPage.js     # Shopping cart
│   ├── LoginPage.js    # User login
│   └── ...             # Other pages
├── services/           # API service functions
│   ├── api.js          # Axios configuration
│   ├── authService.js  # Authentication API calls
│   ├── productService.js # Product API calls
│   └── ...             # Other services
├── App.js              # Main app component
├── index.js            # App entry point
└── index.css           # Global styles
```

## Key Features

### Authentication
- User registration and login
- JWT token-based authentication
- Protected routes for authenticated users
- Role-based access control (Admin, Manager, Customer)

### Product Management
- Product catalog with search and filtering
- Category-based browsing
- Product detail pages
- Admin product management (CRUD operations)

### Shopping Cart
- Add/remove items from cart
- Quantity management
- Persistent cart (localStorage for guests, server for authenticated users)
- Cart synchronization between local and server

### Responsive Design
- Mobile-first approach
- Responsive navigation with mobile menu
- Optimized for all screen sizes
- Touch-friendly interface

### State Management
- React Context for global state
- Separate contexts for authentication and cart
- Efficient state updates and re-renders

## API Integration

The frontend communicates with the backend API through:

- **Base URL**: `/api` (proxied to `http://localhost:8080`)
- **Authentication**: JWT tokens in Authorization headers
- **Error Handling**: Centralized error handling with user-friendly messages
- **Request Interceptors**: Automatic token attachment
- **Response Interceptors**: Automatic token refresh and error handling

## Styling

The application uses Tailwind CSS for styling with:

- Custom color palette for brand consistency
- Responsive utilities for mobile-first design
- Custom component classes for reusability
- Dark mode support (can be extended)
- Smooth animations and transitions

## Environment Variables

Create a `.env` file in the root directory:

```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_APP_NAME=Grocery Store
```

## Deployment

### Production Build

```bash
npm run build
```

This creates a `build` folder with optimized production files.

### Deployment Options

- **Netlify**: Connect your GitHub repo for automatic deployments
- **Vercel**: Deploy with zero configuration
- **AWS S3 + CloudFront**: For scalable hosting
- **Docker**: Use the included Dockerfile for containerized deployment

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, email support@grocerystore.com or create an issue in the repository.