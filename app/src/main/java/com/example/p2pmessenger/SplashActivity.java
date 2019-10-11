package com.example.p2pmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent welcomeIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(welcomeIntent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}