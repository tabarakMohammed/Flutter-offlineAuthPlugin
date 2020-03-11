import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:keyguard_manager_androaid/keyguard_manager_androaid.dart';

void main() {
  const MethodChannel channel = MethodChannel('keyguard_manager_androaid');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await KeyguardManagerAndroaid.authCheck, '42');
  });
}
