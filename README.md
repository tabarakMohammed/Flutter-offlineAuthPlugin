# key guard manager For Android from 5.0 Lollipop version and higher


A new flutter plugin project. keyguard_manager_androaid for Local_auth
This Flutter plugin provides means to perform local,
 on-device authentication of the user.

This means referring to  [PIN | Passcode | Pattern] authentication on Android
 (introduced in Android 5.0 - Lollipop).

## How to use ?

```
1- import 'package:key_guardmanager/key_guardmanager.dart';
```
get auth check
first open android auth dialog to user for enter his password.
```
2-  platformAuth = await KeyGuardmanager.authStatus;
```
 return "true" or "false" String's refer to the auth status when user enter

```
 Future<void> initPlatformState() async {
     String platformAuth;
     try {
       platformAuth = await KeyGuardmanager.authStatus;
     } on PlatformException {
       platformAuth = 'Failed to get platform auth.';
     }
```
