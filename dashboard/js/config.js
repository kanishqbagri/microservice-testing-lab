// Executive Dashboard Configuration
export const CONFIG = {
    // Supabase Configuration
    SUPABASE: {
        URL: 'https://smuaribfocdanafiixzi.supabase.co',
        KEY: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs'
    },

    // Data Configuration
    DATA: {
        TIME_RANGE_MONTHS: 3,
        PAGE_SIZE: 10000, // Increased from 1000 to fetch more records per page
        AUTO_REFRESH_INTERVAL: 300000 // 5 minutes
    },

    // Test Type Weights for Scoring
    TEST_TYPE_WEIGHTS: {
        'unit': 0.25,      // Unit tests - foundation
        'api': 0.30,       // API tests - critical for integration
        'integration': 0.25, // Integration tests - system coherence
        'ui': 0.10,        // UI tests - user experience
        'system': 0.10     // System tests - end-to-end validation
    },

    // Performance Thresholds (in milliseconds)
    PERFORMANCE_THRESHOLDS: {
        'unit': 100,      // Unit tests should be very fast
        'api': 500,       // API tests can be moderately fast
        'integration': 2000, // Integration tests can be slower
        'ui': 5000,       // UI tests can be slow
        'system': 10000   // System tests can be very slow
    },

    // UI Configuration
    UI: {
        COLORS: {
            primary: '#2c3e50',
            secondary: '#3498db',
            success: '#27ae60',
            danger: '#e74c3c',
            warning: '#f39c12',
            info: '#17a2b8',
            executiveBlue: '#1e3a8a',
            executiveGold: '#f59e0b',
            executiveGreen: '#059669',
            executiveRed: '#dc2626'
        }
    }
};

// Service Names Mapping
export const SERVICE_NAMES = {
    'user': 'User Service',
    'users': 'User Service',
    'order': 'Order Service',
    'orders': 'Order Service',
    'product': 'Product Service',
    'products': 'Product Service',
    'notification': 'Notification Service',
    'notifications': 'Notification Service',
    'gateway': 'Gateway Service',
    'api': 'Gateway Service'
};

// Test Type Icons
export const TEST_TYPE_ICONS = {
    'unit': 'code',
    'api': 'plug',
    'integration': 'link',
    'ui': 'desktop',
    'system': 'cogs'
};
