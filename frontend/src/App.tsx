import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProductListPage from './pages/ProductListPage';
import OrderListPage from './pages/OrderListPage';
import NotificationPage from './pages/NotificationPage';
import UserProfilePage from './pages/UserProfilePage';

const Navigation: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <nav style={{ 
      padding: '1rem', 
      borderBottom: '1px solid #ccc',
      backgroundColor: '#f8f9fa'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <Link to="/" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Home</Link>
          {isAuthenticated ? (
            <>
              <Link to="/products" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Products</Link>
              <Link to="/orders" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Orders</Link>
              <Link to="/notifications" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Notifications</Link>
              <Link to="/profile" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Profile</Link>
            </>
          ) : (
            <>
              <Link to="/login" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Login</Link>
              <Link to="/register" style={{ marginRight: 10, textDecoration: 'none', color: '#1976d2' }}>Register</Link>
            </>
          )}
        </div>
        {isAuthenticated && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <span style={{ color: '#666' }}>Welcome, {user?.name}!</span>
            <button
              onClick={handleLogout}
              style={{
                padding: '5px 10px',
                backgroundColor: '#f44336',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '12px'
              }}
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </nav>
  );
};

const AppContent: React.FC = () => (
  <Router>
    <Navigation />
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/products" element={<ProductListPage />} />
      <Route path="/orders" element={<OrderListPage />} />
      <Route path="/notifications" element={<NotificationPage />} />
      <Route path="/profile" element={<UserProfilePage />} />
    </Routes>
  </Router>
);

const App: React.FC = () => (
  <AuthProvider>
    <AppContent />
  </AuthProvider>
);

export default App;
