package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    private EditText displayname, displaynumber, displayroute;
    private Button save;
    private TextView emailedit;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        displayname = (EditText)findViewById(R.id.editname);
        emailedit = (TextView) findViewById(R.id.editemail);
        displaynumber = (EditText) findViewById(R.id.editphone);
        displayroute = (EditText)findViewById(R.id.editroute);
        save = (Button)findViewById(R.id.saveprofilebtn);


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
                        emailedit.setText(email);
                        displaynumber.setText(phone);
                        displayroute.setText(route);
                    }

                }
                else{
                    Toast.makeText(EditProfile.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);

              //  ref.child("username").setValue(displayname.getText());

                //Driver driver = new Driver(userId, displayname.getText().toString(), displayname.getText().toString(), displayname.getText().toString(), displayname.getText().toString());)
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name= displayname.getText().toString();
                        String email= emailedit.getText().toString();
                        String phone= displaynumber.getText().toString();
                        String route= displayroute.getText().toString();
                        dataSnapshot.child("username").getRef().setValue(name);
                        dataSnapshot.child("email").getRef().setValue(email);
                        dataSnapshot.child("phonenumber").getRef().setValue(phone);
                        dataSnapshot.child("busnumber").getRef().setValue(route);




                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent viewprofile = new Intent(EditProfile.this, ViewProfile.class);
                startActivity(viewprofile);
                finish();
            }
        });

        image = (ImageView) findViewById(R.id.profileimageupload);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent viewprofile = new Intent(EditProfile.this, ViewProfile.class);
        startActivity(viewprofile);
        finish();// mAuth.removeAuthStateListener(firebaseauthlistener);




    }
}
