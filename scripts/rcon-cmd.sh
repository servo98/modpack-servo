#!/bin/bash
# Quick RCON command helper
# Usage: ./scripts/rcon-cmd.sh "command"
#        ./scripts/rcon-cmd.sh "function servo_core:test_all_kits"
#        ./scripts/rcon-cmd.sh  (interactive mode)

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

if [ $# -eq 0 ]; then
    python "$SCRIPT_DIR/rcon.py" --interactive
else
    python "$SCRIPT_DIR/rcon.py" "$@"
fi
