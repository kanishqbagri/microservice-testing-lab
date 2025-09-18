#!/bin/bash
# Stop All Port Forwards Script

echo "🛑 Stopping all kubectl port forwards..."

# Kill all kubectl port-forward processes
pkill -f "kubectl port-forward"

# Remove PID file if it exists
rm -f /tmp/port-forward-pids.txt

echo "✅ All port forwards stopped"
