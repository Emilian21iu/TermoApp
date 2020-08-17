package com.example.starnet.termoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.starnet.termoapp.Util.FirebaseConnection;
import com.example.starnet.termoapp.Util.NotificationMessage;
import com.example.starnet.termoapp.Util.ParametersAlertStatus;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    private float temperatura,temperatura_apa;
    private float presiune;
    private float temp_min,temp_max;
    private float temp_apa_min,temp_apa_max;
    private float pres_min,pres_max;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRef = FirebaseConnection.getInstance();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();




    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("home_values").child("temperatura_inside").getValue().toString() == "nan"){
                    temperatura = 0;
                }else temperatura = Float.parseFloat(dataSnapshot.child("home_values").child("temperatura_inside").getValue().toString());
                if(dataSnapshot.child("home_values").child("temperatura_apa").getValue().toString() == "nan"){
                    temperatura_apa = 0;
                }else temperatura_apa = Float.parseFloat(dataSnapshot.child("home_values").child("temperatura_apa").getValue().toString());
                if(dataSnapshot.child("home_values").child("presiune").getValue().toString() == "nan"){
                    presiune = 0;
                }else presiune = Float.parseFloat(dataSnapshot.child("home_values").child("presiune").getValue().toString());

                temp_min = Float.parseFloat(dataSnapshot.child("notification").child("calorifer_min").getValue().toString());
                temp_max = Float.parseFloat(dataSnapshot.child("notification").child("calorifer_max").getValue().toString());
                temp_apa_min = Float.parseFloat(dataSnapshot.child("notification").child("robinet_min").getValue().toString());
                temp_apa_max = Float.parseFloat(dataSnapshot.child("notification").child("robinet_max").getValue().toString());
                pres_min = Float.parseFloat(dataSnapshot.child("notification").child("presiune_min").getValue().toString());
                pres_max = Float.parseFloat(dataSnapshot.child("notification").child("presiune_max").getValue().toString());

                if(Integer.parseInt(dataSnapshot.child("notification").child("state_calorifer").getValue().toString()) != 0) {
                    if (temperatura < temp_min && Integer.parseInt(dataSnapshot.child("notification").child("state_calorifer")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.LOW_TEMPERATURE);
                        not.SendNotification(getApplicationContext());
                    }
                    if (temperatura > temp_max && Integer.parseInt(dataSnapshot.child("notification").child("state_calorifer")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.HIGH_TEMPERATURE);
                        not.SendNotification(getApplicationContext());
                    }
                }
                if(Integer.parseInt(dataSnapshot.child("notification").child("state_robinet").getValue().toString()) != 0) {
                    if (temperatura_apa < temp_apa_min && Integer.parseInt(dataSnapshot.child("notification").child("state_robinet")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.LOW_WATER);
                        not.SendNotification(getApplicationContext());
                    }
                    if (temperatura_apa > temp_apa_max && Integer.parseInt(dataSnapshot.child("notification").child("state_robinet")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.HIGH_WATER);
                        not.SendNotification(getApplicationContext());
                    }
                }
                if(Integer.parseInt(dataSnapshot.child("notification").child("state_presiune").getValue().toString()) != 0) {
                    if (presiune < pres_min && Integer.parseInt(dataSnapshot.child("notification").child("state_presiune")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.LOW_PRESSURE);
                        not.SendNotification(getApplicationContext());
                    }
                    if (presiune > pres_max && Integer.parseInt(dataSnapshot.child("notification").child("state_presiune")
                            .getValue().toString()) == 1) {
                        NotificationMessage not = new NotificationMessage(ParametersAlertStatus.HIGH_PRESSURE);
                        not.SendNotification(getApplicationContext());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_notifications:
                    selectedFragment = new NotificationsFragment();
                    break;
                case R.id.nav_submenu:
                    selectedFragment = new SubMenuFragment();
                    break;
                case R.id.nav_mode:
                    selectedFragment = new Mod_confortFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;

        }

    };
}
