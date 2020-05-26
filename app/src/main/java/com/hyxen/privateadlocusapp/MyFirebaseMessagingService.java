package com.hyxen.privateadlocusapp;

import android.text.TextUtils;

import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hyxen.privateadlocus.AdLocus;
import com.hyxen.privateadlocus.constants.Constants;
import com.hyxen.privateadlocus.repository.data.response.GetFCMDataResponse;
import com.hyxen.privateadlocus.util.CallBack;
import com.hyxen.privateadlocus.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo3x on 2019/4/1.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public static void setCallback(CallBack<String> callback) {
        MyFirebaseMessagingService.callback = callback;
    }

    private static CallBack<String> callback;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Logger.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d("onMessageReceived");



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();
            if(callback!=null){
                JSONObject jo=new JSONObject();
                for(Map.Entry<String, String> entry:data.entrySet()){
                    try {
                        jo.put(entry.getKey(),entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onData(jo.toString());
            }
            Map<String, Object> eventValue = new HashMap<>();
            for(Map.Entry<String, String> entry:data.entrySet()){
                eventValue.put(entry.getKey(),entry.getValue());
            }
            AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.RE_ENGAGE,eventValue);
//            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (TextUtils.equals(data.get(GetFCMDataResponse.TAG_TARGET), Constants.TAG_FCM_TARGET))
                AdLocus.getInstance().sendFCMMessage(this,data);
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }
        }
    }
    @Override
    public void onNewToken(String token) {
        Log.d( "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        //save and update token to server


        //AdLocus.getInstance().updatePushToken(token);
    }
}
