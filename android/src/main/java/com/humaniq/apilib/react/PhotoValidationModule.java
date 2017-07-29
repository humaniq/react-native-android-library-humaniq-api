package com.humaniq.apilib.react;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.ValidateRequest;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.FacialImage;
import com.humaniq.apilib.network.models.response.FacialImageValidation;
import com.humaniq.apilib.network.models.response.ValidationResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import com.humaniq.apilib.utils.ModelConverterUtils;
import com.humaniq.apilib.utils.ResponseWrapperUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ognev on 7/29/17.
 */

public class PhotoValidationModule extends ReactContextBaseJavaModule {

  public PhotoValidationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    ServiceBuilder.init(Constants.BASE_URL, reactContext);
  }

  @Override public String getName() {
    return "HumaniqPhotoValidation";
  }

  @ReactMethod public void isRegistered(String base64, final Promise promise) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image", base64);
    ServiceBuilder.getValidationService()
        .isRegistered(jsonObject)
        .enqueue(new Callback<BasePayload<FacialImage>>() {
          @Override public void onResponse(Call<BasePayload<FacialImage>> call,
              Response<BasePayload<FacialImage>> response) {
            if (response.body() != null) {
              try {
                WritableMap writableMap = ModelConverterUtils.convertJsonToMap(
                    new JSONObject(new Gson().toJson(response.body().payload)));

                promise.resolve(writableMap);
              } catch (Exception e) {
                e.printStackTrace();
                promise.reject(e);
              }
            } else {
              promise.reject(ResponseWrapperUtils.wrapErrorBody(response.errorBody()));
            }
          }

          @Override public void onFailure(Call<BasePayload<FacialImage>> call, Throwable t) {
            promise.reject(t);
          }
        });
  }

  @ReactMethod public void createValidation(String facialImageId, final Promise promise) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image_id", facialImageId);
    ServiceBuilder.getValidationService()
        .createValidation(jsonObject)
        .enqueue(new Callback<BasePayload<FacialImageValidation>>() {
          @Override public void onResponse(Call<BasePayload<FacialImageValidation>> call,
              Response<BasePayload<FacialImageValidation>> response) {
            if (response.body() != null) {
              try {
                WritableMap writableMap = new WritableNativeMap();
                writableMap.putString("facial_image_validation_id",
                    response.body().payload.getFacialImageValidationId());

                WritableArray emotionArray = new WritableNativeArray();
                for (String emotion : response.body().payload.getRequiredEmotions()) {
                  WritableMap emotionMap = new WritableNativeMap();
                  emotionMap.putString("emotion", emotion);
                  emotionArray.pushMap(emotionMap);
                }

                writableMap.putArray("required_emotions", emotionArray);

                promise.resolve(writableMap);
              } catch (Exception e) {
                e.printStackTrace();
                promise.reject(e);
              }
            } else {
              promise.reject(ResponseWrapperUtils.wrapErrorBody(response.errorBody()));
            }
          }

          @Override
          public void onFailure(Call<BasePayload<FacialImageValidation>> call, Throwable t) {
            promise.reject(t);
          }
        });
  }

  private String encodeImage(String path)
  {
    File imagefile = new File(Uri.parse(path).getPath());
    FileInputStream fis = null;
    try{
      fis = new FileInputStream(imagefile);
    } catch(FileNotFoundException e){
      e.printStackTrace();
    }
    Bitmap bm = BitmapFactory.decodeStream(fis);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
    byte[] b = baos.toByteArray();
    String encImage = Base64.encodeToString(b, Base64.DEFAULT);
    //Base64.de
    return encImage;

  }

  @ReactMethod
    public void validate(String facialImageValidationId, String path, final Promise promise) {
    ValidateRequest validateRequest = new ValidateRequest();
    validateRequest.setFacialImageId(facialImageValidationId);
    String base64 = encodeImage(path);
    validateRequest.setFacialImage(base64);
    //  JsonObject jsonObject = new JsonObject();
    //jsonObject.addProperty("facial_image_validation_id", facialImageId);
    //  jsonObject.addProperty("facial_image", base64);
    ServiceBuilder.getValidationService()
        .validate(validateRequest)
        .enqueue(new Callback<BasePayload<ValidationResponse>>() {
          @Override public void onResponse(Call<BasePayload<ValidationResponse>> call,
              Response<BasePayload<ValidationResponse>> response) {

            if (response.body() != null) {
              try {
                WritableMap writableMap = new WritableNativeMap();
                writableMap.putString("message", "GREAT DONE");
                promise.resolve(writableMap);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              promise.reject(ResponseWrapperUtils.wrapErrorBody(response.errorBody()));
            }
          }

          @Override public void onFailure(Call<BasePayload<ValidationResponse>> call, Throwable t) {
            promise.reject(t);

          }
        });
  }
}
