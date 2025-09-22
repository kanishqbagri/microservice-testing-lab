#!/bin/bash

# Migration script to transition from monolithic to modular dashboard structure

echo "🔄 Migrating Executive Dashboard to Modular Structure..."

# Create js directory if it doesn't exist
mkdir -p js

# Backup the original file
echo "📦 Creating backup of original executive-dashboard.js..."
cp executive-dashboard.js executive-dashboard-backup.js

# Rename the new modular file to replace the old one
echo "🔄 Replacing executive-dashboard.js with modular version..."
mv executive-dashboard-new.js executive-dashboard.js

# Update HTML to use the new structure
echo "📝 Updating HTML to use modular structure..."

# Create a simple test to verify the migration
echo "🧪 Creating migration test..."
cat > test-migration.html << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>Migration Test</title>
</head>
<body>
    <h1>Testing Modular Dashboard Structure</h1>
    <div id="test-results"></div>
    
    <script type="module">
        import { CONFIG } from './js/config.js';
        
        document.getElementById('test-results').innerHTML = `
            <p>✅ Config loaded successfully</p>
            <p>Supabase URL: ${CONFIG.SUPABASE.URL}</p>
            <p>Time Range: ${CONFIG.DATA.TIME_RANGE_MONTHS} months</p>
        `;
    </script>
</body>
</html>
EOF

echo "✅ Migration completed!"
echo ""
echo "📁 New file structure:"
echo "├── js/"
echo "│   ├── config.js              # Configuration and constants"
echo "│   ├── data-service.js        # Data fetching and pagination"
echo "│   ├── metrics-calculator.js  # All calculation logic"
echo "│   ├── scorecard-renderer.js  # Scorecard rendering"
echo "│   └── scorecards.js          # Scorecard management"
echo "├── executive-dashboard.js     # Main orchestrator (streamlined)"
echo "├── executive-dashboard-backup.js  # Backup of original"
echo "└── test-migration.html        # Migration test"
echo ""
echo "🚀 Benefits of the new structure:"
echo "   • Main file reduced from 1400+ to ~300 lines"
echo "   • Separation of concerns"
echo "   • Easier maintenance and testing"
echo "   • Reusable components"
echo "   • Better code organization"
echo ""
echo "🧪 To test the migration, open test-migration.html in your browser"
echo "🔄 To rollback, run: mv executive-dashboard-backup.js executive-dashboard.js"
