import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:key_guardmanager/key_guardmanager.dart';

void main() {
  const MethodChannel channel = MethodChannel('key_guardmanager');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getAuthStatus', () async {
    expect(await KeyGuardmanager.authStatus, '42');
  });
}
