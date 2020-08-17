package com.example.starnet.termoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class HomePage extends Activity {

    private static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity a=(Activity)mContext;
       a.setContentView(R.layout.fragment_home);
      String userName=getIntent().getStringExtra("username");
    }
}
