package com.example.starnet.termoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    Thread timer= new Thread(){

  public void run(){
      try{sleep(3000);}catch (InterruptedException e){
          e.printStackTrace();
      }
      finally {
          Intent myintent=new Intent(SplashScreen.this,LoginActivity.class);
          startActivity(myintent);
      }

  }

};
timer.start();
    }
}
