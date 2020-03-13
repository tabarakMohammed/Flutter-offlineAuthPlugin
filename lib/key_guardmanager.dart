import 'dart:async';

import 'package:flutter/services.dart';

class KeyGuardmanager {
  static const MethodChannel _channel =
      const MethodChannel('key_guardmanager');

  static Future<String> get authStatus async {
    final String version = await _channel.invokeMethod('AuthStatus');
    return version;
  }
}
