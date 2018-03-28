package com.example.nabahat.tracking;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {


    private TextView displayname, displayemail, rating, displaynumber, displayroute;
    private Button edit;
    RatingBar ratingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        displayname = (TextView)findViewById(R.id.displayname);
        displayemail = (TextView)findViewById(R.id.displayemail);
        rating = (TextView)findViewById(R.id.rating);
        displaynumber = (TextView) findViewById(R.id.displayphone);
        displayroute = (TextView)findViewById(R.id.displayroute);
        edit = (Button)findViewById(R.id.editprofilebtn);
        ratingbar = (RatingBar)findViewById(R.id.ratingBar);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Data Received",dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name= dataSnapshot.child("username").getValue(String.class);
                        String email= dataSnapshot.child("email").getValue(String.class);
                        String phone= dataSnapshot.child("phonenumber").getValue(String.class);
                        String route= dataSnapshot.child("busnumber").getValue(String.class);
                        displayname.setText(name);
                        displayemail.setText(email);
                        displaynumber.setText(phone);
                        displayroute.setText("Route Number: "+route);
                    }

                }
                else{
                    Toast.makeText(ViewProfile.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent editprofile = new Intent(ViewProfile.this, EditProfile.class);
            startActivity(editprofile);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mAuth.removeAuthStateListener(firebaseauthlistener);
        finish();


    }
}
