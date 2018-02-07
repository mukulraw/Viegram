package com.relinns.viegram.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.relinns.viegram.R;
import com.relinns.viegram.service.MyFirebaseInstanceIDService;
import io.fabric.sdk.android.Fabric;

public class Splash_screen extends Activity {
    private SharedPreferences preferences;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(FirebaseInstanceId.getInstance()==null){
            Log.d("instance","nhi");
        }
        else {
            Log.d("instance","hai");
        }


        new Thread() {
            @Override
            public void run() {
                //If there are stories, add them to the table
                try {
                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            new MyFirebaseInstanceIDService().onTokenRefresh();
                        }
                    });
                } catch (final Exception ignored) {
                }
            }
        }.start();
        Fabric.with(this, new Crashlytics());
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        if (!preferences.getString("user_id", "").equals("")) {
            Intent i = new Intent(Splash_screen.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            timer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    Intent i = new Intent(Splash_screen.this, Login_Screen.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }.start();
        }
    }
}
