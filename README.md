## React Native Android Humaniq API implementation
This project implements API of Humaniq Mobile for react engine.

## Getting started
Firstly need to update up to latest version of github repository.

npm uninstall react-native-android-library-humaniq-api
npm install --save git+https://github.com/humaniq/react-native-android-library-humaniq-api.git 

After download latest version of java library (gate thrue java to api endpoint) may use library functions.



1. For check linked java library you can do instructions:


   import {HumaniqToastApiLib} from 'react-native-android-library-humaniq-api';
   in .js file.
   after this action, current module will be available for use.
   ```
   HumaniqToastApiLib.show(‘Boilerplate runs fine’, HumaniqApiLib.LONG)
       
   There are different modules available for using:

   ContactModule, DownloadModule, ProfileModule, ToastModule - uses for test library.

   ContacnModule has implemented this api call:

   https://gold-star-1172.postman.co/docs/collection/view/2376470-2edfad49-aac7-ed2e-5e42-edbf18a258b4#c8f6a1fe-0536-af9b-90e1-50e40213b615
   ```
   @POST("/contact-checker/api/v1/extract_registered_phone_numbers")
   Call<ContactsResponse> extractPhoneNumbers(@Body List<String> data);
 	```

   this function may be call in react-native .js file:
    ``` 
       HumaniqToastApiLib.extractAllPhoneNumbers().then((array) => {
           console.warn(array);
          });
 	```
2. in array object will use postman response, just redirect data from backend via java library to react-native app.
    
    Structure of data presented at 
    ```
    {
  	  "success": true,
  	  "data": [
        "+7(910)1234567",
        "+7( 910) 987-65-43"
  	   ]
	}
     ```
    #Attention
    before use extractAllPhoneNumbers(), need to call and approve by user PermissionsAndroid.PERMISSIONS.WRITE_CONTACTS permission from react side. More details presented at this link https://facebook.github.io/react-native/docs/permissionsandroid.html

2. Profile module (module consist api functions for any profile data requests) - HumaniqProfileApiLib.
	This module conists functions:
    ```
	public void getAddressState(String id, final Promise promise); 
 	public void getTransactions(String id, final Promise promise);
 	public void getTransactions(String id, int offset, int limit, final Promise promise);
 	public void getBalance(String id, final Promise promise);
 	public void createTransaction(String fromUserId, String toUserId, float amount, final Promise promise);
 	public void updateUserPerson(String accountId, String firstName, String lastName, final Promise promise);
 	public void uploadProfileAvatar(String accountId, String avatarBse64, final Promise promise);
 	public void changeProfilePassword(String oldPassword, String newPassword) - not yet released, only mock data;
 	public void deauthenticateUser(String accountId, final Promise promise);
    ```
3. Download module (module consist api functions for download any files) - HumaniqDownloadFileLib
	This module consists functions:
	public void downloadVideoFile(String uri, final Promise downloadPromise);
	this function allow you to download any-size files from internet.
    ```
	public void downloadVideoFile(String uri, final Promise downloadPromise)
	private void sendEvent(@Nullable WritableMap params) - every time provide progress status of file downloading. Use RCTDeviceEventEmitter class for this purpose.
    ```
4. Blockchain module contains this functions
	```
	public void transferHmq(String fromId, String toId, int amount, final Promise promise)
    ```




for building libraries uses this resources:
http://cmichel.io/how-to-create-react-native-android-library/


