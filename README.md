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

Exclude okhttp module in app build.gradle file(android/app/build.gradle):
         
      compile (project(':react-native-android-library-humaniq-api')) {exclude module: 'okhttp'}


 ## HOW TO USE

 The library contains  modules for different purposes of implementation api.

1. Contacts module (module consist api functions for contact sychronization with backend) - HumaniqContactsApiLib
   This module also contain a listener for new created contacts, in this case a created contact will be automatically syncronized with backend.

   functions overview:

   for use this module firstly try to attach it, in this sample both modules (HumaniqDownloadFileLib, HumaniqContactsApiLib) will be attached:
   import { HumaniqDownloadFileLib, HumaniqContactsApiLib} from 'react-native-android-library-humaniq-api';
   in .js file.
   after this action, current module will be available for use.

       HumaniqContactsApiLib.extractAllPhoneNumbers().then((array) => {
           console.warn(array);
          });
    in array object will use postman response, just redirect data from backend via java library to react-native app.
    Structure of data presented at https://gold-star-1172.postman.co/docs/collection/view/2376470-2edfad49-aac7-ed2e-5e42-edbf18a258b4#c8f6a1fe-0536-af9b-90e1-50e40213b615

    {
  	  "success": true,
  	  "data": [
        "+7(910)1234567",
        "+7( 910) 987-65-43"
  	   ]
	}

    #Attention
    before use extractAllPhoneNumbers(), need to call and approve by user PermissionsAndroid.PERMISSIONS.WRITE_CONTACTS permission from react side. More details presented at this link https://facebook.github.io/react-native/docs/permissionsandroid.html

2. Profile module (module consist api functions for any profile data requests) - HumaniqProfileApiLib.
	This module conists functions:

	public void getAddressState(String id, final Promise promise), 
 	public void getTransactions(String id, final Promise promise),


3. Download module (module consist api functions for download any files) - HumaniqDownloadFileLib
	This module consists functions:
	public void downloadVideoFile(String uri, final Promise downloadPromise);
	this function allow you to download any-size files from internet.




