#!/usr/bin/env python3
"""
RCON client for Minecraft server.
Usage:
    python rcon.py "command"                    # Single command
    python rcon.py "cmd1" "cmd2" "cmd3"         # Multiple commands
    python rcon.py --host 127.0.0.1 --port 25575 --password servo-dev "command"
    echo "command" | python rcon.py --stdin      # Pipe mode
    python rcon.py --interactive                 # Interactive REPL
"""
import argparse
import socket
import struct
import sys
import time

PACKET_LOGIN = 3
PACKET_COMMAND = 2
PACKET_RESPONSE = 0


def _pack(packet_id: int, packet_type: int, payload: str) -> bytes:
    data = struct.pack('<ii', packet_id, packet_type) + payload.encode('utf-8') + b'\x00\x00'
    return struct.pack('<i', len(data)) + data


def _unpack(data: bytes):
    if len(data) < 14:
        return None, None, None
    length = struct.unpack('<i', data[:4])[0]
    packet_id, packet_type = struct.unpack('<ii', data[4:12])
    payload = data[12:4 + length - 2].decode('utf-8', errors='replace')
    return packet_id, packet_type, payload


class RconClient:
    def __init__(self, host: str = '127.0.0.1', port: int = 25575, password: str = 'servo-dev', timeout: float = 10.0):
        self.host = host
        self.port = port
        self.password = password
        self.timeout = timeout
        self.sock = None
        self._id = 0

    def _next_id(self) -> int:
        self._id += 1
        return self._id

    def connect(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.settimeout(self.timeout)
        self.sock.connect((self.host, self.port))
        # Authenticate
        pid = self._next_id()
        self.sock.sendall(_pack(pid, PACKET_LOGIN, self.password))
        resp = self.sock.recv(4096)
        resp_id, resp_type, _ = _unpack(resp)
        if resp_id == -1:
            raise ConnectionRefusedError("RCON authentication failed. Check password.")

    def command(self, cmd: str) -> str:
        if not self.sock:
            self.connect()
        pid = self._next_id()
        self.sock.sendall(_pack(pid, PACKET_COMMAND, cmd))
        # Read response (may be fragmented for large responses)
        result = b''
        while True:
            try:
                chunk = self.sock.recv(4096)
                result += chunk
                if len(chunk) < 4096:
                    break
            except socket.timeout:
                break
        _, _, payload = _unpack(result)
        return payload or ''

    def close(self):
        if self.sock:
            self.sock.close()
            self.sock = None

    def __enter__(self):
        self.connect()
        return self

    def __exit__(self, *args):
        self.close()


def main():
    parser = argparse.ArgumentParser(description='Minecraft RCON client for Modpack Servo')
    parser.add_argument('commands', nargs='*', help='Commands to execute')
    parser.add_argument('--host', default='127.0.0.1', help='Server host (default: 127.0.0.1)')
    parser.add_argument('--port', type=int, default=25575, help='RCON port (default: 25575)')
    parser.add_argument('--password', default='servo-dev', help='RCON password (default: servo-dev)')
    parser.add_argument('--stdin', action='store_true', help='Read commands from stdin (one per line)')
    parser.add_argument('--interactive', '-i', action='store_true', help='Interactive REPL mode')
    parser.add_argument('--delay', type=float, default=0.1, help='Delay between commands in seconds (default: 0.1)')
    parser.add_argument('--quiet', '-q', action='store_true', help='Only print command responses, no headers')
    args = parser.parse_args()

    commands = list(args.commands)
    if args.stdin:
        commands.extend(line.strip() for line in sys.stdin if line.strip())

    try:
        with RconClient(args.host, args.port, args.password) as rcon:
            if args.interactive:
                if not args.quiet:
                    print(f"Connected to {args.host}:{args.port}")
                    print("Type 'exit' or 'quit' to disconnect. Ctrl+C to abort.")
                while True:
                    try:
                        cmd = input("rcon> ").strip()
                    except (EOFError, KeyboardInterrupt):
                        print()
                        break
                    if cmd.lower() in ('exit', 'quit'):
                        break
                    if not cmd:
                        continue
                    resp = rcon.command(cmd)
                    if resp:
                        print(resp)
            else:
                if not commands:
                    parser.print_help()
                    sys.exit(1)
                for cmd in commands:
                    resp = rcon.command(cmd)
                    if not args.quiet:
                        print(f">>> {cmd}")
                    if resp:
                        print(resp)
                    if len(commands) > 1:
                        time.sleep(args.delay)
    except ConnectionRefusedError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)
    except ConnectionError:
        print("Error: Could not connect to server. Is it running?", file=sys.stderr)
        sys.exit(1)


if __name__ == '__main__':
    main()
