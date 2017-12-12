package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverHome extends AppCompatActivity {
    TextView StartTracker, EnterFuelDetails, Logout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        StartTracker = (TextView)findViewById(R.id.lay_starttracker);
        EnterFuelDetails = (TextView)findViewById(R.id.lay_enterfuel);
        Logout = (TextView)findViewById(R.id.lay_signout);
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");

        StartTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OpenMap = new Intent(DriverHome.this, DriverMapsActivity.class);
                //String user_Id = mAuth.getCurrentUser().getUid();
               // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);

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
        EnterFuelDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);

                        Toast.makeText(DriverHome.this, value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}
