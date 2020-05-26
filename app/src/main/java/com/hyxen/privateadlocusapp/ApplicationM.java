package com.hyxen.privateadlocusapp;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class ApplicationM extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
//        AppsFlyerConversionListener conversionDataListener =
//                new AppsFlyerConversionListener() {
//                    @Override
//                    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
//                        Logger.d(conversionData);
//
//
//                    }
//                    @Override
//                    public void onInstallConversionFailure(String errorMessage) {
//                        Logger.d(errorMessage);
//                    }
//                    @Override
//                    public void onAppOpenAttribution(Map<String, String> attributionData) {
//                        Logger.d(attributionData);
//                    }
//                    @Override
//                    public void onAttributionFailure(String errorMessage) {
//                        Logger.d(errorMessage);
//                    }
//                };

//        AppsFlyerLib.getInstance().init("UpToZrvS3uhp3PAxanuxHE", conversionDataListener, getApplicationContext());
//        AppsFlyerLib.getInstance().startTracking(this);
//
//        Map<String, Object> eventValue = new HashMap<>();
//        eventValue.put(AFInAppEventParameterName.LEVEL,9);
//        eventValue.put(AFInAppEventParameterName.SCORE,100);
//        AppsFlyerLib.getInstance().trackEvent(this,AFInAppEventType.LEVEL_ACHIEVED,eventValue);
    }
}
