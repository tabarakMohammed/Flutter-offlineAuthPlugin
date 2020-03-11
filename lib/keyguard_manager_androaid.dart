import 'dart:async';

import 'package:flutter/services.dart';

class KeyguardManagerAndroaid {

  static const MethodChannel _channel =
      const MethodChannel('keyguard_manager_androaid');

  static Future<String> get authCheck async {

    final String authState = await _channel.invokeMethod('start');
    return authState;
  }
}
