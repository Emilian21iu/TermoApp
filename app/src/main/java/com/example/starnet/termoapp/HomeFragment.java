package com.example.starnet.termoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.starnet.termoapp.Util.FirebaseConnection;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class HomeFragment extends Fragment{

    private TextView lbl_data;
    private TextView lbl_ora;
    private TextView lbl_temp_out;
    private TextView lbl_temp_in;
    private TextView lbl_temp_pref;
    private TextView lbl_umiditate;
    private ImageView img_gas_alert;
    private LinearLayout gas_alert_layout;
    private ToggleButton btn_power;
    private Thread timeThread;
    private Firebase mRef;
    private WebView webv;
    private ImageButton btn_doc;
    private ImageButton btn_grafic;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private DatabaseReference db = fdb.getReference();
    private Handler mainHandler = new Handler();

    private FirebaseAuth firebaseAuth;
    private Button logout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        setup(view);

        mRef = FirebaseConnection.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

         btn_power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    btn_power.setVisibility(View.VISIBLE);
                    mRef.child("power").setValue(0);

                } else
                    mRef.child("power").setValue(1);

            }
        });



        btn_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new PdfFragment()).commit();
            }
        });


        btn_grafic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://thingspeak.com/channels/647036/charts/1"));
                startActivity(intent);
            }
        });

       logout = view.findViewById(R.id.btn_logout);
       logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == logout) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });




        timeThread.start();
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("home_values").child("temperatura_out").getValue().toString() == "nan") {
                    lbl_temp_out.setText("nan");
                } else
                    lbl_temp_out.setText(dataSnapshot.child("home_values").child("temperatura_out").getValue().toString() + " °C");
                if (dataSnapshot.child("home_values").child("temperatura_inside").getValue().toString() == "nan") {
                    lbl_temp_in.setText("nan");
                } else
                    lbl_temp_in.setText(dataSnapshot.child("home_values").child("temperatura_inside").getValue().toString() + " °C");
                //Setez temperatura in functie de ora
                float pref_temp = Float.parseFloat(dataSnapshot.child("prefered_values").child("calorifer").getValue().toString());
                if(pref_temp == 48)
                    lbl_temp_pref.setText(pref_temp + " °C until 19:00 AM");
                else if(pref_temp == 55)
                    lbl_temp_pref.setText(pref_temp + " °C until 7:00 AM");
                else lbl_temp_pref.setText(pref_temp + " °C until 12:00 AM");
                if (dataSnapshot.child("home_values").child("umiditate").getValue().toString() == "nan") {
                    lbl_umiditate.setText("nan");
                } else
                    lbl_umiditate.setText(dataSnapshot.child("home_values").child("umiditate").getValue().toString() + "%");
                if (dataSnapshot.child("home_values").child("gas_alert").getValue().toString() == "nan") {
                    ;
                } else {
                    float _gas_alert = Float.parseFloat(dataSnapshot.child("home_values").child("gas_alert").getValue().toString());

                    if (_gas_alert < 300)
                        gas_alert_layout.setVisibility(View.INVISIBLE);
                    else gas_alert_layout.setVisibility(View.VISIBLE);
                }
                //btn_power.setChecked(Integer.parseInt(dataSnapshot.child("power").getValue().toString()) != 0);
                
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void setup(View view){
        lbl_data = view.findViewById(R.id.lbl_data);
        lbl_ora = view.findViewById(R.id.lbl_ora);
        lbl_temp_out = view.findViewById(R.id.lbl_temperatura_out);
        lbl_temp_in = view.findViewById(R.id.lbl_temperatura_in);
        lbl_temp_pref = view.findViewById(R.id.lbl_temperatura_pref);
        lbl_umiditate = view.findViewById(R.id.lbl_umiditate);
        img_gas_alert = view.findViewById(R.id.img_gas_alert);
        gas_alert_layout =  view.findViewById(R.id.gas_alert_layout);
        btn_power = view.findViewById(R.id.btn_power);
        webv = view.findViewById(R.id.webv_grafic);
        btn_doc = view.findViewById(R.id.btn_documentatie);
        btn_grafic = view.findViewById(R.id.btn_grafic);


        timeThread = new Thread(){
            @Override
            public void run(){
                try {
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE,d MMM yyyy");
                                lbl_data.setText(sdf.format(date));
                                SimpleDateFormat sdo = new SimpleDateFormat("HH:mm a");
                                lbl_ora.setText(sdo.format(date));
                                     }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
