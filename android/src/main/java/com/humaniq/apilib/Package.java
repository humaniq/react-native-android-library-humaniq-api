package com.humaniq.apilib;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.humaniq.apilib.network.models.response.contacts.Contact;
import com.humaniq.apilib.react.BlockchainModule;
import com.humaniq.apilib.react.ContactsModule;
import com.humaniq.apilib.react.DownloadModule;
import com.humaniq.apilib.react.PhotoValidationModule;
import com.humaniq.apilib.react.ToastModule;
import com.humaniq.apilib.react.ProfileModule;
import com.humaniq.apilib.react.TokenModule;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Package implements ReactPackage {

  @Override public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }

    @Override public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      List<NativeModule> modules = new ArrayList<>();


      String projectToken = Constants.MIXPANEL_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
      MixpanelAPI mixpanel = MixpanelAPI.getInstance(reactContext, projectToken);


      modules.add(new ToastModule(reactContext));
      modules.add(new ProfileModule(reactContext));
      modules.add(new DownloadModule(reactContext));
      modules.add(new ContactsModule(reactContext));
      modules.add(new BlockchainModule(reactContext));
      modules.add(new TokenModule(reactContext));
      modules.add(new PhotoValidationModule(reactContext));

      return modules;
    }
}
