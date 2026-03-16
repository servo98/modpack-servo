#!/bin/bash
# Start the Modpack Servo dev server with RCON enabled
# Usage: ./scripts/start-server.sh [--background]
#
# The server uses server/server.properties for config.
# RCON is enabled on port 25575 with password 'servo-dev'.
# Connect with: python scripts/rcon.py -i

set -e
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# Copy server configs to run/ if not present
if [ ! -f "$PROJECT_DIR/run/server.properties" ]; then
    echo "[Servo] Copying server configs to run/..."
    cp "$PROJECT_DIR/server/server.properties" "$PROJECT_DIR/run/server.properties"
    cp "$PROJECT_DIR/server/eula.txt" "$PROJECT_DIR/run/eula.txt"
fi

# Always sync server.properties (dev convenience)
cp "$PROJECT_DIR/server/server.properties" "$PROJECT_DIR/run/server.properties"
cp "$PROJECT_DIR/server/eula.txt" "$PROJECT_DIR/run/eula.txt"

if [ -f "$PROJECT_DIR/server/ops.json" ]; then
    cp "$PROJECT_DIR/server/ops.json" "$PROJECT_DIR/run/ops.json"
fi

echo "[Servo] Starting dev server..."
echo "[Servo] RCON enabled on port 25575 (password: servo-dev)"
echo "[Servo] Connect with: python scripts/rcon.py -i"
echo ""

if [ "$1" = "--background" ]; then
    cd "$PROJECT_DIR"
    ./gw runServer 2>&1 | tee run/server-latest.log &
    SERVER_PID=$!
    echo "[Servo] Server started in background (PID: $SERVER_PID)"
    echo "$SERVER_PID" > run/server.pid
    echo "[Servo] Waiting for server to be ready..."
    # Wait for RCON to be available
    for i in $(seq 1 60); do
        if python "$SCRIPT_DIR/rcon.py" -q "list" 2>/dev/null; then
            echo "[Servo] Server is ready! RCON connected."
            break
        fi
        sleep 2
    done
else
    cd "$PROJECT_DIR"
    exec ./gw runServer 2>&1 | tee run/server-latest.log
fi
