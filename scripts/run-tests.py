#!/usr/bin/env python3
"""
Automated test runner for Modpack Servo via RCON.
Runs mcfunction test kits and verifies server responses.

Usage:
    python scripts/run-tests.py                    # Run all tests
    python scripts/run-tests.py --mod packaging    # Run tests for specific mod
    python scripts/run-tests.py --smoke            # Quick smoke test (server alive + mods loaded)
    python scripts/run-tests.py --stages           # Test stage progression
"""
import argparse
import sys
import time
import os

# Add scripts dir to path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from rcon import RconClient


class TestRunner:
    def __init__(self, host='127.0.0.1', port=25575, password='servo-dev'):
        self.rcon = RconClient(host, port, password)
        self.passed = 0
        self.failed = 0
        self.skipped = 0
        self.results = []

    def connect(self):
        try:
            self.rcon.connect()
            return True
        except Exception as e:
            print(f"ERROR: Cannot connect to server: {e}")
            return False

    def cmd(self, command: str) -> str:
        """Execute RCON command and return response."""
        resp = self.rcon.command(command)
        time.sleep(0.15)  # Small delay between commands
        return resp

    def test(self, name: str, command: str, expect_success: bool = True, expect_contains: str = None):
        """Run a test: execute command and check result."""
        try:
            resp = self.cmd(command)
            success = True

            if expect_contains and expect_contains not in resp:
                success = False

            # Check for common error patterns
            if expect_success and ('Unknown' in resp and 'command' in resp.lower()):
                success = False

            status = "PASS" if success else "FAIL"
            if success:
                self.passed += 1
            else:
                self.failed += 1

            self.results.append((status, name, resp[:100]))
            print(f"  [{status}] {name}")
            if not success:
                print(f"         Response: {resp[:200]}")
        except Exception as e:
            self.failed += 1
            self.results.append(("ERROR", name, str(e)))
            print(f"  [ERROR] {name}: {e}")

    def test_smoke(self):
        """Quick smoke test: server alive, mods loaded."""
        print("\n=== SMOKE TEST ===")

        # Server alive
        self.test("Server responds", "list")

        # Reload to ensure clean state
        self.test("Reload succeeds", "reload")
        time.sleep(3)  # Wait for reload

        # Check servo mods are loaded (give a known item)
        self.test("servo_packaging loaded", "give @a servo_packaging:flat_cardboard 1")
        self.test("servo_delivery loaded", "give @a servo_delivery:delivery_terminal 1")
        self.test("servo_core loaded", "give @a servo_core:pepe_coin 1")
        self.test("servo_create loaded (mod present)", "give @a servo_packaging:open_box 1")
        self.test("servo_dungeons loaded", "give @a servo_dungeons:dungeon_essence 1")
        self.test("servo_mart loaded", "give @a servo_mart:pepe_mart 1")

    def test_packaging(self):
        """Test servo_packaging items and functions."""
        print("\n=== SERVO_PACKAGING TESTS ===")

        self.test("test_kit function", "function servo_packaging:test_kit")
        self.test("flat_cardboard give", "give @a servo_packaging:flat_cardboard 16")
        self.test("open_box give", "give @a servo_packaging:open_box 4")
        self.test("packing_station give", "give @a servo_packaging:packing_station 1")

    def test_delivery(self):
        """Test servo_delivery items and functions."""
        print("\n=== SERVO_DELIVERY TESTS ===")

        self.test("test_kit function", "function servo_delivery:test_kit")
        self.test("elevator_base give", "give @a servo_delivery:elevator_base 3")
        self.test("delivery_port give", "give @a servo_delivery:delivery_port 2")
        self.test("delivery_terminal give", "give @a servo_delivery:delivery_terminal 1")
        self.test("elevator_antenna give", "give @a servo_delivery:elevator_antenna 1")

    def test_core(self):
        """Test servo_core items and functions."""
        print("\n=== SERVO_CORE TESTS ===")

        self.test("test_kit function", "function servo_core:test_kit")
        self.test("pepe_coin give", "give @a servo_core:pepe_coin 10")
        self.test("dungeon_essence give", "give @a servo_core:dungeon_essence 5")
        self.test("core_crystal_fragment give", "give @a servo_core:core_crystal_fragment 3")
        self.test("basic_dungeon_key give", "give @a servo_core:basic_dungeon_key 1")
        self.test("advanced_dungeon_key give", "give @a servo_core:advanced_dungeon_key 1")
        self.test("master_dungeon_key give", "give @a servo_core:master_dungeon_key 1")
        self.test("core_dungeon_key give", "give @a servo_core:core_dungeon_key 1")

    def test_dungeons(self):
        """Test servo_dungeons items and functions."""
        print("\n=== SERVO_DUNGEONS TESTS ===")

        self.test("test_kit function", "function servo_dungeons:test_kit")
        self.test("dungeon_essence give", "give @a servo_dungeons:dungeon_essence 8")

    def test_mart(self):
        """Test servo_mart items and functions."""
        print("\n=== SERVO_MART TESTS ===")

        self.test("test_kit function", "function servo_mart:test_kit")
        self.test("pepe_mart block give", "give @a servo_mart:pepe_mart 1")

    def test_stages(self):
        """Test ProgressiveStages stage commands."""
        print("\n=== STAGE PROGRESSION TESTS ===")

        # These require a player to be online
        self.test("stage_unlock function", "function servo_core:stage_unlock")
        time.sleep(0.5)
        self.test("stages list", "kubejs stages list @a")
        self.test("stage_reset function", "function servo_core:stage_reset")
        time.sleep(0.5)
        self.test("stages after reset", "kubejs stages list @a")

        # Progressive unlock
        self.test("add ch2", "kubejs stages add @a servo_ch2")
        self.test("add ch3", "kubejs stages add @a servo_ch3")
        self.test("stages after ch3", "kubejs stages list @a")
        # Reset for clean state
        self.test("final reset", "function servo_core:stage_reset")

    def test_master(self):
        """Test master function that calls all test kits."""
        print("\n=== MASTER TEST KIT ===")
        self.test("test_all_kits function", "function servo_core:test_all_kits")

    def report(self):
        """Print test summary."""
        total = self.passed + self.failed + self.skipped
        print(f"\n{'='*50}")
        print(f"Results: {self.passed} passed, {self.failed} failed, {self.skipped} skipped ({total} total)")
        if self.failed > 0:
            print("\nFailed tests:")
            for status, name, resp in self.results:
                if status != "PASS":
                    print(f"  - {name}: {resp}")
        print(f"{'='*50}")
        return self.failed == 0


def main():
    parser = argparse.ArgumentParser(description='Modpack Servo test runner')
    parser.add_argument('--host', default='127.0.0.1')
    parser.add_argument('--port', type=int, default=25575)
    parser.add_argument('--password', default='servo-dev')
    parser.add_argument('--mod', choices=['packaging', 'delivery', 'core', 'create', 'dungeons', 'mart'],
                        help='Test specific mod only')
    parser.add_argument('--smoke', action='store_true', help='Quick smoke test only')
    parser.add_argument('--stages', action='store_true', help='Test stage progression')
    parser.add_argument('--all', action='store_true', help='Run all tests (default)')
    args = parser.parse_args()

    runner = TestRunner(args.host, args.port, args.password)
    if not runner.connect():
        sys.exit(1)

    print(f"Connected to server at {args.host}:{args.port}")

    try:
        if args.smoke:
            runner.test_smoke()
        elif args.stages:
            runner.test_stages()
        elif args.mod:
            test_fn = getattr(runner, f'test_{args.mod}', None)
            if test_fn:
                test_fn()
            else:
                print(f"No tests for mod: {args.mod}")
        else:
            # Run everything
            runner.test_smoke()
            runner.test_packaging()
            runner.test_delivery()
            runner.test_core()
            runner.test_dungeons()
            runner.test_mart()
            runner.test_stages()
            runner.test_master()

        success = runner.report()
        sys.exit(0 if success else 1)
    finally:
        runner.rcon.close()


if __name__ == '__main__':
    main()
