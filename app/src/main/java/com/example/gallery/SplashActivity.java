package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
          public void run(){
              try {
                  sleep(1200);
              }catch (Exception e){
                  e.printStackTrace();
              }
              finally {
                  Intent intent = new Intent(SplashActivity.this,ScrollingActivity.class);
                  startActivity(intent);
                  finish();
              }
          }
        };
        thread.start();
    }
}