package com.example.starnet.termoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.starnet.termoapp.Util.FirebaseConnection;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class Mod_confortFragment extends Fragment implements View.OnClickListener{

    private ImageButton none_btn;
    private ImageButton auto_btn;
    private ImageButton plecat_btn;
    private ImageButton vara_btn;
    private ImageButton vacanta_btn;
    private ImageButton antiinghet_btn;
    private ImageButton alexa_btn;

    private TextView none_lbl;
    private TextView auto_lbl;
    private TextView plecat_lbl;
    private TextView vara_lbl;
    private TextView vacanta_lbl;
    private TextView antiinghet_lbl;
    private  TextView alexa_lbl;


    private Firebase mRef;
    private char REQ_SPEECH_RESULT;
    private View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mod_confort,container,false);
        setup(view);
        mRef = FirebaseConnection.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resetAll();
                switch (dataSnapshot.child("mod_confort").child("mod_curent").getValue().toString()){
                    case "none":
                        none_btn.setImageResource(R.drawable.none_icon);
                        none_lbl.setTextColor(Color.RED);
                        mRef.child("states").child("calorifer").setValue(0);
                        break;
                    case "auto":
                        auto_btn.setImageResource(R.drawable.auto_icon);
                        auto_lbl.setTextColor(Color.RED);
                        long date = System.currentTimeMillis();
                        SimpleDateFormat sdo = new SimpleDateFormat("HH");
                        int ora = Integer.parseInt(sdo.format(date));
                        if(ora > 12 && ora < 19){
                            mRef.child("prefered_values").child("calorifer").setValue(48);
                            mRef.child("states").child("calorifer").setValue(1);
                        }
                        if(ora >= 19 && ora <=24 ||  ora < 7){
                            mRef.child("prefered_values").child("calorifer").setValue(55);
                            mRef.child("states").child("calorifer").setValue(1);
                        }
                        if(ora >= 7 && ora < 12){
                            mRef.child("prefered_values").child("calorifer").setValue(45);
                            mRef.child("states").child("calorifer").setValue(1);
                        }
                        break;
                    case "plecat":
                        plecat_btn.setImageResource(R.drawable.away_icon);
                        plecat_lbl.setTextColor(Color.RED);
                        break;
                    case "vara":
                        vara_btn.setImageResource(R.drawable.vara_icon);
                        vara_lbl.setTextColor(Color.RED);
                        break;
                    case "vacanta":
                        vacanta_btn.setImageResource(R.drawable.vacancy_icon);
                        vacanta_lbl.setTextColor(Color.RED);
                        break;
                    case "antiinghet":
                        antiinghet_btn.setImageResource(R.drawable.antifreze_icon);
                        antiinghet_lbl.setTextColor(Color.RED);
                        break;
                    case "alexa":
                        alexa_btn.setImageResource(R.drawable.alexa_icon);
                        alexa_lbl.setTextColor(Color.RED);
                        break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setup(View view){
        none_btn = view.findViewById(R.id.none_img);
        auto_btn = view.findViewById(R.id.auto_img);
        plecat_btn = view.findViewById(R.id.plecat_img);
        vara_btn = view.findViewById(R.id.vara_img);
        vacanta_btn = view.findViewById(R.id.vacanta_img);
        antiinghet_btn = view.findViewById(R.id.antiinghet_img);
        alexa_btn=view.findViewById(R.id.alexa_img);
        none_lbl = view.findViewById(R.id.none_lbl);
        auto_lbl = view.findViewById(R.id.auto_lbl);
        plecat_lbl = view.findViewById(R.id.plecat_lbl);
        vara_lbl = view.findViewById(R.id.vara_lbl);
        vacanta_lbl = view.findViewById(R.id.vacanta_lbl);
        antiinghet_lbl = view.findViewById(R.id.antiinghet_lbl);
        alexa_lbl=view.findViewById(R.id.alexa_lbl);
        none_btn.setOnClickListener(this);
        auto_btn.setOnClickListener(this);
        plecat_btn.setOnClickListener(this);
        vara_btn.setOnClickListener(this);
        vacanta_btn.setOnClickListener(this);
        antiinghet_btn.setOnClickListener(this);
        alexa_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.none_img:
                resetAll();
                none_btn.setImageResource(R.drawable.none_icon);
                none_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("none");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(60);
                break;
            case R.id.auto_img:
                resetAll();
                auto_btn.setImageResource(R.drawable.auto_icon);
                auto_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("auto");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(45);
                break;
            case R.id.plecat_img:
                resetAll();
                plecat_btn.setImageResource(R.drawable.away_icon);
                plecat_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("plecat");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(30);
                break;
            case R.id.vara_img:
                resetAll();
                vara_btn.setImageResource(R.drawable.vara_icon);
                vara_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("vara");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(20);
                break;
            case R.id.vacanta_img:
                resetAll();
                vacanta_btn.setImageResource(R.drawable.vacancy_icon);
                vacanta_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("vacanta");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(15);
                break;
            case R.id.antiinghet_img:
                resetAll();
                antiinghet_btn.setImageResource(R.drawable.antifreze_icon);
                antiinghet_lbl.setTextColor(Color.RED);
                mRef.child("mod_confort").child("mod_curent").setValue("antiinghet");
                mRef.child("prefered_values").child("temperatura_apa_centrala").setValue(7);
                break;

            case R.id.alexa_img:
                resetAll();
                alexa_btn.setImageResource(R.drawable.alexa_icon);
                alexa_lbl.setTextColor(Color.RED);
                alexa_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startVoiceCommand();
                    }
                });
                break;
        }
    }


    private void startVoiceCommand() {
        Log.d(TAG, "Starting Voice intent...");
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tell me, I'm ready!");
        try {
            startActivityForResult(i, REQ_SPEECH_RESULT);
        }
        catch (Exception e) {
            Snackbar.make(v, "Speech to text not supported", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check the Request code
        if (requestCode ==  REQ_SPEECH_RESULT) {
            Log.d(TAG, "Request speech result..");
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String command = results.get(0);
            Log.d(TAG, "Current command ["+command+"]");
            // Now we send commands to the IoT device
        }
    }

    private void resetAll(){
        none_btn.setImageResource(R.drawable.none_icon_gray);
        auto_btn.setImageResource(R.drawable.auto_icon_gray);
        plecat_btn.setImageResource(R.drawable.away_icon_gray);
        vara_btn.setImageResource(R.drawable.vara_icon_gray);
        vacanta_btn.setImageResource(R.drawable.vacancy_icon_gray);
        antiinghet_btn.setImageResource(R.drawable.antifreze_icon_gray);
        alexa_btn.setImageResource(R.drawable.alexa_icon_grey);
        none_lbl.setTextColor(Color.WHITE);
        auto_lbl.setTextColor(Color.WHITE);
        plecat_lbl.setTextColor(Color.WHITE);
        vara_lbl.setTextColor(Color.WHITE);
        vacanta_lbl.setTextColor(Color.WHITE);
        antiinghet_lbl.setTextColor(Color.WHITE);
        alexa_lbl.setTextColor(Color.WHITE);
    }
}
