package com.example.key_guardmanager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry;

import static android.content.Context.KEYGUARD_SERVICE;

/** KeyGuardmanagerPlugin */
public class KeyGuardmanagerPlugin implements MethodCallHandler,  FlutterPlugin, ActivityAware {
  /** Plugin registration. */
  private static final int REQUEST_CODE = 1; /** code for enter  to on activity when user need  */
  private static MethodChannel.Result result; /** callBack messenger send binary value to the another packages   */
  private static ActivityPluginBinding instanceActivity;

  @Override
  public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {

  
    instanceActivity = binding;

    instanceActivity.addActivityResultListener(new PluginRegistry.ActivityResultListener() {

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




  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  /** instance of object that using to connect with flutter   */



  @Override
  public void onAttachedToEngine(@NonNull @NotNull FlutterPluginBinding binding) {
    final MethodChannel channel = new MethodChannel(binding.getBinaryMessenger(), "key_guardmanager");
    channel.setMethodCallHandler(new KeyGuardmanagerPlugin());
  }

  @Override
  public void onDetachedFromEngine(@NonNull @NotNull FlutterPluginBinding binding) {

  }



  private static void runOnUiThread(final MethodChannel.Result result, final String authStatets, final boolean success) {

    /**  started thread run on Main Thread */
      instanceActivity.getActivity().runOnUiThread(new Runnable() {
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

    KeyguardManager km;


       km = (KeyguardManager) instanceActivity.getActivity().getSystemService(KEYGUARD_SERVICE);

    if (km.isKeyguardSecure()) {
      Intent authIntent = null;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        authIntent = km.createConfirmDeviceCredentialIntent(
                null, null); /** this title and des for auth phone dialog*/
      }


      if (authIntent != null) {
        try {


            instanceActivity.getActivity().startActivityForResult(authIntent, REQUEST_CODE);


        } catch (Exception e) {

          e.printStackTrace();
          result.error("don't Support","sported Filled",e.toString());
        }
      }


    }

  }
}