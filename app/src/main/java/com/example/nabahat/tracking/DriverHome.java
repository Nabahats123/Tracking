package com.example.nabahat.tracking;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.animation.Animator;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DriverHome extends AppCompatActivity {
    //TextView StartTracker, Logout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    CardView notification, StartTracker, profile, Logout, Stoptracker, viewnotification ;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    long servicetime;
    double servicedistance;
    double servicefuel,servicespeed;
    double fuelConsumed;
    double latti,longi;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    MyReceiver myReceiver = new MyReceiver();
    String route ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StartTracker = (CardView) findViewById(R.id.cardtracking);
        Stoptracker = (CardView) findViewById(R.id.cardstop);
        viewnotification = (CardView) findViewById(R.id.cardviewnotification);
        Logout = (CardView) findViewById(R.id.cardsignout);
        notification = (CardView) findViewById(R.id.cardsendnotification);
        profile = (CardView) findViewById(R.id.cardprofile);
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        IntentFilter filter = new IntentFilter("GPSLocationUpdates");

        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver,filter);


        /*Register reciever , name of intent "GPSLocationUpdates" */
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                myReceiver, new IntentFilter("GPSLocationUpdates"));
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putString("TimeStamp", timeStamp);
        editor.commit();
        Intent serviceIntent = new Intent(DriverHome.this, CustomIntentServiceJava.class);
        startService(serviceIntent);

        //STEP#1
        StartTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SERVICE INTENT
                Intent serviceIntent = new Intent(getApplicationContext(),MyService.class);

                serviceIntent.putExtra("Latitude",latti);
                serviceIntent.putExtra("Longitude",longi);
                serviceIntent.putExtra("StartingSeconds",System.currentTimeMillis());

                //START INTENT
                getApplicationContext().startService(serviceIntent);
                //MAP INTENT
                String Bus = "";
                Intent OpenMap = new Intent(DriverHome.this, DriverMapsActivity.class);
                //String user_Id = mAuth.getCurrentUser().getUid();
                // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Bus = extras.getString("Bus Number");
                }
                OpenMap.putExtra("Bus Number", Bus);



                startActivity(OpenMap);
                // finish();
            }
        });
        /*SERVCE STOP and DESTROYED*/
        Stoptracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getApplicationContext(),MyService.class);
                getApplicationContext().stopService(serviceIntent);
                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(myReceiver);
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
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(DriverHome.this, PhoneActivity.class));

                startActivity(new Intent(DriverHome.this, ViewProfile.class));
                finish();
            }
        });
        viewnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverHome.this, ViewNotification.class));
                finish();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverHome.this, SendNotification.class));
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

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();

        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DriverHome.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DriverHome.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DriverHome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();

            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();

            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }


    private class MyReceiver extends BroadcastReceiver {
        /*STEP#8 reciever class recieves value from intent
        * Display dialog box*/

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean dataadded = false;
            // TODO Auto-generated method stub
            servicetime = intent.getLongExtra("seconds",servicetime);
            servicedistance = intent.getDoubleExtra("distance",servicedistance);
            servicespeed = intent.getDoubleExtra("speed",servicespeed);
            servicefuel = intent.getDoubleExtra("totalfuel",servicefuel);
            fuelConsumed = intent.getDoubleExtra("fuelconsumed", fuelConsumed);
            Bundle b = intent.getBundleExtra("Location");

            android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(DriverHome.this);
            builderSingle.setTitle("Total fuel cost: Rs "+servicefuel);
            builderSingle.setMessage("Time: "+servicetime+ " seconds \n" + "Distance Covered: "+servicedistance+" Meters \n" +
                    "Average Speed:"+servicespeed +" m/s");
            builderSingle.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
//            builderSingle.show();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    route = dataSnapshot.child("busnumber").getValue(String.class);
                    Log.d("BusNumber",route);
                    final Calendar c = Calendar.getInstance();
                    SimpleDateFormat df1 = new SimpleDateFormat("h:mm a");
                    String formattedTime = df1.format(c.getTime());
                    DateFormat dateFormatter = new SimpleDateFormat("yyyy:MM:dd");
                    String formattedDate = dateFormatter.format(c.getTime());

                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Fuel");
                    Fuel fuel = new Fuel(fuelConsumed ,servicefuel, servicetime, route, servicedistance, servicespeed, formattedDate, formattedTime);
                    ref2.push().setValue(fuel);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



        }
    }
}
