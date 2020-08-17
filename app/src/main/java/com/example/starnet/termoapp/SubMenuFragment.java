package com.example.starnet.termoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.starnet.termoapp.Util.FirebaseConnection;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SubMenuFragment extends Fragment {
    private Switch calorifer_switch;
    private Switch robinet_switch;
    private Switch incalzire_switch;
    private Switch presiune_switch;

    private TextView calorifer_tv;
    private TextView robinet_tv;
    private TextView incalzire_tv;
    private TextView presiune_tv;

    private int calorifer_value;
    private int robinet_value;
    private int incalzire_value;
    private float presiune_value;

    private Button calorifer_plus;
    private Button calorifer_minus;
    private Button robinet_plus;
    private Button robinet_minus;
    private Button incalzire_plus;
    private Button incalzire_minus;
    private Button presiune_plus;
    private Button presiune_minus;

    private Firebase mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu,container,false);
        setup_controls(view);
        mRef = FirebaseConnection.getInstance();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                calorifer_value = Integer.parseInt(dataSnapshot.child("prefered_values").child("calorifer").getValue().toString());
                robinet_value = Integer.parseInt(dataSnapshot.child("prefered_values").child("robinet").getValue().toString());
                incalzire_value = Integer.parseInt(dataSnapshot.child("prefered_values").child("incalzire").getValue().toString());
                presiune_value = Float.parseFloat(dataSnapshot.child("prefered_values").child("presiune").getValue().toString());
                calorifer_tv.setText(calorifer_value + " °C");
                robinet_tv.setText(robinet_value + " °C");
                incalzire_tv.setText(incalzire_value + " %");
                presiune_tv.setText(String.format("%.1f",presiune_value) + " bar");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int calorifer_state = Integer.parseInt(dataSnapshot.child("states").child("calorifer").getValue().toString());
                if(calorifer_state !=0){
                    calorifer_switch.setChecked(true);
                }else calorifer_switch.setChecked(false);

                int robinet_state = Integer.parseInt(dataSnapshot.child("states").child("robinet").getValue().toString());
                if(robinet_state !=0){
                    robinet_switch.setChecked(true);
                }else robinet_switch.setChecked(false);

                int incalzire_state = Integer.parseInt(dataSnapshot.child("states").child("incalzire").getValue().toString());
                if(incalzire_state !=0){
                    incalzire_switch.setChecked(true);
                }else incalzire_switch.setChecked(false);

                int presiune_state = Integer.parseInt(dataSnapshot.child("states").child("presiune").getValue().toString());
                if(presiune_state !=0){
                    presiune_switch.setChecked(true);
                }else presiune_switch.setChecked(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        onSwitchsStateChanged();

        setOnClickMethods();

        return view;
    }

    private void onSwitchsStateChanged(){
        calorifer_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("states").child("calorifer").setValue(new Integer(1));
                else mRef.child("states").child("calorifer").setValue(new Integer(0));
            }
        });

        robinet_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("states").child("robinet").setValue(1);
                else mRef.child("states").child("robinet").setValue(0);
            }
        });
        incalzire_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("states").child("incalzire").setValue(1);
                else mRef.child("states").child("incalzire").setValue(0);
            }
        });
        presiune_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("states").child("presiune").setValue(1);
                else mRef.child("states").child("presiune").setValue(0);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    private void setOnClickMethods(){
        calorifer_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calorifer_value += 1;
                mRef.child("prefered_values").child("calorifer").setValue(calorifer_value);
                calorifer_tv.setText(calorifer_value + " °C");
            }
        });
        calorifer_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calorifer_value -= 1;
                mRef.child("prefered_values").child("calorifer").setValue(calorifer_value);
                calorifer_tv.setText(calorifer_value + " °C");
            }
        });
        robinet_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robinet_value += 1;
                mRef.child("prefered_values").child("robinet").setValue(robinet_value);
                robinet_tv.setText(robinet_value + " °C");
            }
        });
        robinet_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robinet_value -= 1;
                mRef.child("prefered_values").child("robinet").setValue(robinet_value);
                robinet_tv.setText(robinet_value + " °C");
            }
        });
        incalzire_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incalzire_value += 1;
                mRef.child("prefered_values").child("incalzire").setValue(incalzire_value);
                incalzire_tv.setText(incalzire_value + " %");
            }
        });
        incalzire_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incalzire_value -= 1;
                mRef.child("prefered_values").child("incalzire").setValue(incalzire_value);
                incalzire_tv.setText(incalzire_value + " %");
            }
        });
        presiune_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presiune_value += 0.1;
                mRef.child("prefered_values").child("presiune").setValue(presiune_value);
                presiune_tv.setText(String.format("%.1f",presiune_value) + " bar");
            }
        });
        presiune_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presiune_value -= 0.1;
                mRef.child("prefered_values").child("presiune").setValue(presiune_value);
                presiune_tv.setText(String.format("%.1f",presiune_value) + "bar");
            }
        });
    }

    private void setup_controls(View view){
        calorifer_switch = view.findViewById(R.id.calorifer_switch);
        robinet_switch = view.findViewById(R.id.robinet_switch);
        incalzire_switch = view.findViewById(R.id.incalzire_switch);
        presiune_switch = view.findViewById(R.id.presiune_switch);

        calorifer_tv = view.findViewById(R.id.calorifer_value);
        robinet_tv = view.findViewById(R.id.robinet_value);
        incalzire_tv = view.findViewById(R.id.incalzire_value);
        presiune_tv = view.findViewById(R.id.presiune_value);

        calorifer_plus = view.findViewById(R.id.calorifer_plus);
        calorifer_minus = view.findViewById(R.id.calorifer_minus);
        robinet_plus = view.findViewById(R.id.robinet_plus);
        robinet_minus = view.findViewById(R.id.robinet_minus);
        incalzire_plus = view.findViewById(R.id.incalzire_plus);
        incalzire_minus = view.findViewById(R.id.incalzire_minus);
        presiune_plus = view.findViewById(R.id.presiune_plus);
        presiune_minus = view.findViewById(R.id.presiune_minus);
    }


}
