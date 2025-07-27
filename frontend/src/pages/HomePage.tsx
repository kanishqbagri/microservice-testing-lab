import React from 'react';
import { useAuth } from '../context/AuthContext';

const HomePage: React.FC = () => {
  const { user, isAuthenticated } = useAuth();

  return (
    <div style={{ maxWidth: '800px', margin: '50px auto', padding: '20px' }}>
      <h1>Welcome to Microservices Testing Lab</h1>
      
      {isAuthenticated ? (
        <div style={{ marginBottom: '30px' }}>
          <h2>Hello, {user?.name}!</h2>
          <p>You are logged in as: {user?.email}</p>
        </div>
      ) : (
        <div style={{ marginBottom: '30px' }}>
          <h2>Please log in to access the services</h2>
          <p>Use the navigation menu to login or register.</p>
        </div>
      )}

      <div style={{ 
        backgroundColor: '#f5f5f5', 
        padding: '20px', 
        borderRadius: '8px',
        marginBottom: '20px'
      }}>
        <h3>Available Services</h3>
        <ul style={{ lineHeight: '1.6' }}>
          <li><strong>User Service:</strong> Authentication and user management</li>
          <li><strong>Product Service:</strong> Product catalog and inventory management</li>
          <li><strong>Order Service:</strong> Order processing and management</li>
          <li><strong>Notification Service:</strong> User notifications and messaging</li>
          <li><strong>Gateway Service:</strong> API Gateway for routing and load balancing</li>
        </ul>
      </div>

      <div style={{ 
        backgroundColor: '#e3f2fd', 
        padding: '20px', 
        borderRadius: '8px',
        marginBottom: '20px'
      }}>
        <h3>Testing Frameworks</h3>
        <ul style={{ lineHeight: '1.6' }}>
          <li><strong>Contract Test Generator:</strong> Generate tests from Swagger/OpenAPI specs</li>
          <li><strong>Jarvis Test Framework:</strong> AI-powered intelligent testing</li>
          <li><strong>API Tests:</strong> REST Assured based API testing</li>
        </ul>
      </div>

      {isAuthenticated && (
        <div style={{ 
          backgroundColor: '#e8f5e8', 
          padding: '20px', 
          borderRadius: '8px'
        }}>
          <h3>Quick Actions</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '15px' }}>
            <a 
              href="/products" 
              style={{
                display: 'block',
                padding: '15px',
                backgroundColor: '#1976d2',
                color: 'white',
                textDecoration: 'none',
                borderRadius: '4px',
                textAlign: 'center'
              }}
            >
              Manage Products
            </a>
            <a 
              href="/orders" 
              style={{
                display: 'block',
                padding: '15px',
                backgroundColor: '#388e3c',
                color: 'white',
                textDecoration: 'none',
                borderRadius: '4px',
                textAlign: 'center'
              }}
            >
              View Orders
            </a>
            <a 
              href="/notifications" 
              style={{
                display: 'block',
                padding: '15px',
                backgroundColor: '#f57c00',
                color: 'white',
                textDecoration: 'none',
                borderRadius: '4px',
                textAlign: 'center'
              }}
            >
              Check Notifications
            </a>
            <a 
              href="/profile" 
              style={{
                display: 'block',
                padding: '15px',
                backgroundColor: '#7b1fa2',
                color: 'white',
                textDecoration: 'none',
                borderRadius: '4px',
                textAlign: 'center'
              }}
            >
              User Profile
            </a>
          </div>
        </div>
      )}
    </div>
  );
};

export default HomePage; 