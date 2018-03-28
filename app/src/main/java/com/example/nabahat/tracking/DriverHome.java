package com.example.nabahat.tracking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.animation.Animator;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverHome extends AppCompatActivity {
    //TextView StartTracker, Logout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    CardView notification, StartTracker, profile, Logout;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StartTracker = (CardView) findViewById(R.id.cardtracking);
        Logout = (CardView) findViewById(R.id.cardsignout);
        notification = (CardView) findViewById(R.id.cardnotification);
        profile = (CardView) findViewById(R.id.cardprofile);
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        StartTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Bus = "";
                Intent OpenMap = new Intent(DriverHome.this, DriverMapsActivity.class);
                //String user_Id = mAuth.getCurrentUser().getUid();
                // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Bus = extras.getString("Bus Number");
                }
                OpenMap.putExtra("Bus Number", Bus);


                Toast.makeText( DriverHome.this ,Bus.toString(), Toast.LENGTH_SHORT).show();
                startActivity(OpenMap);
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.getInstance().signOut();
                startActivity(new Intent(DriverHome.this, MainActivity.class));
                finish();
            }
        });
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

}
