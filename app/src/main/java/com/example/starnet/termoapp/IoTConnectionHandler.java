package com.example.starnet.termoapp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class IoTConnectionHandler {
    private static IoTConnectionHandler me;
    private OkHttpClient client;
    private static final String TAG = IoTConnectionHandler.class.getName();
    private static final String IOT_URL =
            "https://api.thingspeak.com/update?api_key=RV57PVST0G0FT596&field1=0";
    private IoTConnectionHandler() {
        client = new OkHttpClient();
    }
    public static IoTConnectionHandler getInstance() {
        if (me == null)
            me = new IoTConnectionHandler();
        return me;
    }
    public void sendData(String data) {
        Request req = new Request.Builder()
                .url(IOT_URL + data)
                .build();
        client.newCall(req).enqueue(new Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                Log.e(TAG, "Connection error", e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.i(TAG, "Command sent");
            }


        });
    }
}