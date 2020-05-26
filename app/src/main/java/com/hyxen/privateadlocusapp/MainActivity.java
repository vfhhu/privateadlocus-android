package com.hyxen.privateadlocusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hyxen.privateadlocus.AdLocus;
import com.hyxen.privateadlocus.repository.remote.AdLocusAPI;
import com.hyxen.privateadlocus.util.CallBack;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int TAG_LOCATION = 199;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        TextView text_key=findViewById(R.id.text_key);
        text_key.setText(getString(R.string.fcm_app_key));

        FirebaseApp.initializeApp(this);
        MyFirebaseMessagingService.setCallback(new CallBack<String>() {
            @Override
            public void onData(final String... data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView vvv=findViewById(R.id.text_msg);
                        vvv.setText(data[0]);
                    }
                });
            }
        });



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initSDK();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, TAG_LOCATION);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TAG_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initSDK();
                } else {
                    initSDK();
                }
                return;
            }
        }
    }
    public void initSDK(){
        Logger.d("init SDK");
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if(!TextUtils.isEmpty(token)){
            TextView text_token=findViewById(R.id.text_token);
            text_token.setText(token);
        }


        final String topic=AdLocus.getInstance(this)
                .checkUser(!TextUtils.isEmpty(token) ? token : "",
                        getString(R.string.fcm_app_key), getPackageName(), getString(R.string.app_key))
                .getTopicString();
        if(!TextUtils.isEmpty(topic)){
            TextView text_topic=findViewById(R.id.text_topic);
            text_topic.setText(topic);
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "topic 註冊成功 "+topic;
                            if (!task.isSuccessful()) {
                                msg = "topic 註冊失敗 "+topic;
                            }
                            Log.d(TAG, msg);
//                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if(!TextUtils.isEmpty(topic)){
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }

    }


    public void copyData(View v){
        String key=getString(R.string.fcm_app_key);
        String token = FirebaseInstanceId.getInstance().getToken();
        TextView text_topic=findViewById(R.id.text_topic);

        String sendData="key= "+key+"   \ntoken= "+token+"\ntopic= "+text_topic.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendData);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);


        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(AFInAppEventParameterName.DESCRIPTION,token);
        AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.SHARE,eventValue);

    }
    public void pushToken(View v){
        AdLocusAPI.setCallback(new CallBack<String>() {
            @Override
            public void onData(final String[] data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView vvv=findViewById(R.id.text_msg);
                        vvv.setText(data[0]+"\n\n\n"+data[1]);
                    }
                });
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                AdLocus.getInstance(MainActivity.this).updatePushToken(FirebaseInstanceId.getInstance().getToken());
            }
        }).start();

    }

    public void CopyRet(View v){
        TextView text_msg=findViewById(R.id.text_msg);
        String ttt=text_msg.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, ttt);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);


        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(AFInAppEventParameterName.DESCRIPTION,ttt);
        AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.SHARE,eventValue);
    }
}
