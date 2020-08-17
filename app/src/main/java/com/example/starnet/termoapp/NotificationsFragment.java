package com.example.starnet.termoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.starnet.termoapp.Util.FirebaseConnection;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class NotificationsFragment extends Fragment {

    private TextView calorifer_limits;
    private TextView robinet_limits;
    private TextView incalzire_limits;
    private TextView presiune_limits;

    private Switch calorifer_sw;
    private Switch robinet_sw;
    private Switch incalzire_sw;
    private Switch presiune_sw;

    private Firebase mRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        setup(view);
        mRef = FirebaseConnection.getInstance();
        onSwitchesStateChanged();
        return view;
    }

    private void onSwitchesStateChanged(){
        calorifer_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("notification").child("state_calorifer").setValue(1);
                else mRef.child("notification").child("state_calorifer").setValue(0);
            }
        });
        robinet_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("notification").child("state_robinet").setValue(1);
                else mRef.child("notification").child("state_robinet").setValue(0);
            }
        });
        incalzire_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("notification").child("state_incalzire").setValue(1);
                else mRef.child("notification").child("state_incalzire").setValue(0);
            }
        });
        presiune_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    mRef.child("notification").child("state_presiune").setValue(1);
                else mRef.child("notification").child("state_presiune").setValue(0);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                calorifer_limits.setText(dataSnapshot.child("notification").child("calorifer_min").getValue().toString()
                 + "/ " + dataSnapshot.child("notification").child("calorifer_max").getValue().toString() + " °C");
                robinet_limits.setText(dataSnapshot.child("notification").child("robinet_min").getValue().toString()
                        + "/ " + dataSnapshot.child("notification").child("robinet_max").getValue().toString()+ " °C");
                incalzire_limits.setText(dataSnapshot.child("notification").child("incalzire_min").getValue().toString()
                        + "/ " + dataSnapshot.child("notification").child("incalzire_max").getValue().toString() + " %");
                presiune_limits.setText(dataSnapshot.child("notification").child("presiune_min").getValue().toString()
                        + "/ " + dataSnapshot.child("notification").child("presiune_max").getValue().toString() + " bar");

                calorifer_sw.setChecked(Integer.parseInt(dataSnapshot.child("notification").child("state_calorifer").getValue().toString()) != 0);
                robinet_sw.setChecked(Integer.parseInt(dataSnapshot.child("notification").child("state_robinet").getValue().toString()) != 0);
                incalzire_sw.setChecked(Integer.parseInt(dataSnapshot.child("notification").child("state_incalzire").getValue().toString()) != 0);
                presiune_sw.setChecked(Integer.parseInt(dataSnapshot.child("notification").child("state_presiune").getValue().toString()) != 0);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setup(View view){
        calorifer_limits = view.findViewById(R.id.values_calorifer);
        robinet_limits = view.findViewById(R.id.values_robinet);
        incalzire_limits = view.findViewById(R.id.values_incalzire);
        presiune_limits = view.findViewById(R.id.values_presiune);

        calorifer_sw = view.findViewById(R.id.switch_calorifer);
        robinet_sw = view.findViewById(R.id.switch_robinet);
        incalzire_sw = view.findViewById(R.id.switch_incalzire);
        presiune_sw = view.findViewById(R.id.switch_presiune);
    }

}
