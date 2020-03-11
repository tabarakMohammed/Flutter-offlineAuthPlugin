package com.keyandroidmanager.keyguard_manager_androaid;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.KEYGUARD_SERVICE;

/** KeyguardManagerAndroaidPlugin */


public class KeyguardManagerAndroaidPlugin implements MethodCallHandler {
  private static final int REQUEST_CODE = 1;
  private static MethodChannel.Result result;
  private static PluginRegistry.Registrar instance;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "keyguard_manager_androaid");
    channel.setMethodCallHandler(new KeyguardManagerAndroaidPlugin());
    instance = registrar;
    instance.addActivityResultListener(new PluginRegistry.ActivityResultListener() {

      @Override
      public boolean onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

          new Thread(new Runnable() {
            @Override
            public void run() {

              runOnUiThread(result, "true", true);

            }
          }).start();
          return true;
        } else {
          runOnUiThread(result, "false", false);
          return false;}
      }
    });

  }

  private static void runOnUiThread(final MethodChannel.Result result, final String authStatets, final boolean success) {
    instance.activity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (success) {
          result.success(authStatets);
        } else if (authStatets != null) {
          result.error("KEYgrudManager", authStatets, "Auth Filed");
        } else {
          result.notImplemented();
        }
      }
    });
  }

  @Override
  public void onMethodCall(MethodCall call, MethodChannel.Result result) {
    if (call.method.equals("start")) {
      getPassCode();

      KeyguardManagerAndroaidPlugin.result = result;
    } else {
      result.notImplemented();
    }

  }

  private  void getPassCode(){

    KeyguardManager km = (KeyguardManager) instance.activity().getSystemService(KEYGUARD_SERVICE);
    if (km.isKeyguardSecure()) {
      Intent authIntent = null;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        authIntent = km.createConfirmDeviceCredentialIntent(
                null, null);
      }
      if (authIntent != null) {
        try {

          instance.activity().startActivityForResult(authIntent, REQUEST_CODE);


        } catch (Exception e) {

          e.printStackTrace();
          result.error("don't Support","sported Filled",e.toString());
        }
      }


    }

  }
}
