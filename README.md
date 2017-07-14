## React Native Android Humaniq API implementation
This project implements API of Humaniq Mobile for react engine.

## Getting started
1. Do `npm install --save git+https://github.com/humaniq/react-native-android-library-humaniq-api.git` in your main project.
3. Link the library:
    * Add the following to `android/settings.gradle`:
        ```
        include ':react-native-android-library-humaniq-api'
        project(':react-native-android-library-humaniq-api').projectDir = new File(settingsDir, '../node_modules/react-native-android-library-humaniq-api')
        ```

    * Add the following to `android/app/build.gradle`:
        ```xml
        ...

        dependencies {
            ...
            compile project(':react-native-android-library-humaniq-api')
        }
        ```
    * Add the following to `android/app/src/main/java/**/MainApplication.java`:
        ```java
        package com.motivation;

        import io.cmichel.boilerplate.Package;  // add this for react-native-android-library-humaniq-api

        public class MainApplication extends Application implements ReactApplication {

            @Override
            protected List<ReactPackage> getPackages() {
                return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new Package()     // add this for react-native-android-library-humaniq-api
                );
            }
        }
        ```
4. Simply `import/require` it by the name defined in your library's `package.json`:

    ```javascript
    import HumaniqApiLib from 'react-native-android-library-humaniq-api'
    HumaniqApiLib.show('Boilerplate runs fine', HumaniqApiLib.LONG)
    ```
5. You can test and develop your library by importing the `node_modules` library into **Android Studio** if you don't want to install it from _git_ all the time.


or 


react-native link

## IMPORTANT!
If build failed with:

     UNEXPECTED TOP-LEVEL EXCEPTION:
         com.android.dex.DexException: Multiple dex files define Lokhttp3/internal/ws/RealWebSocket$1;

Exclude okhttp module app build.gradle file(android/app/build.gradle):
         
      compile (project(':react-native-android-library-humaniq-api')) {exclude module: 'okhttp'}
