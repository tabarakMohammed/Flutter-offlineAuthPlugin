package com.example.key_guardmanager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.KEYGUARD_SERVICE;

/** KeyGuardmanagerPlugin */
public class KeyGuardmanagerPlugin implements MethodCallHandler {
  /** Plugin registration. */
  private static final int REQUEST_CODE = 1; /** code for enter  to on activity when user need  */
  private static MethodChannel.Result result; /** callBack messenger send binary value to the another packages   */
  private static PluginRegistry.Registrar instance; /** instance of object that using to connect with flutter   */

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {

    if (registrar.activity() == null) {
      // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
      // we stop the registering process immediately because requires an activity.
      return;
    }

    final MethodChannel channel = new MethodChannel(registrar.messenger(), "key_guardmanager");
    channel.setMethodCallHandler(new KeyGuardmanagerPlugin());

    instance = registrar;
    /**  Adds a callback allowing the plugin to take part in handling
     *  incoming calls to Activity.onActivityResult(int, int, Intent).
     */
    instance.addActivityResultListener(new PluginRegistry.ActivityResultListener() {

      @Override
      public boolean onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE) {
          if (resultCode == Activity.RESULT_OK) {

            /**  started thread */
            new Thread(new Runnable() {
              @Override
              public void run() {
                runOnUiThread(result, "true", true);
              }
            }).start();

            // Call the desired channel message here.
            return true;
          } else {
            runOnUiThread(result, "false", false);
            return false;
          }
        }else { return false;}
      }
    });
  }

  private static void runOnUiThread(final MethodChannel.Result result, final String authStatets, final boolean success) {

    /**  started thread run on Main Thread */
    instance.activity().runOnUiThread(new Runnable() {
      @Override
      public void run() {

        if (success) {
          result.success(authStatets);
        } else if (authStatets != null) {
          result.error("KEY Guard Manager", authStatets, "Auth Filed");
        } else {
          result.notImplemented();
        }

      }
    });
  }

  @Override
  public  void onMethodCall(MethodCall call, MethodChannel.Result result) {
    if (call.method.equals("AuthStatus")) {

      getPassCode();  /**  set intent SERVICE */

      KeyGuardmanagerPlugin.result = result; /**  send result to flutter */

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
                null, null); /** this title and des for auth phone dialog*/
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
