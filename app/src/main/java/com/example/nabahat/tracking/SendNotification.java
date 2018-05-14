package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SendNotification extends AppCompatActivity {


    EditText notify;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        notify = (EditText)findViewById(R.id.notifytext);
        send = (Button)findViewById(R.id.sendbtn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String NotificationText = notify.getText().toString();
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Data Received",dataSnapshot.toString());
                        if (dataSnapshot.exists()) {
                            String route= dataSnapshot.child("busnumber").getValue(String.class);
                            String person = "Driver: " + dataSnapshot.child("username").getValue(String.class);
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Notification").child("NotificationbyDriver");

                            Notification notifobject = new Notification(route, NotificationText, person) ;

                            dbref.push().setValue(notifobject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SendNotification.this, "Notification Sent", Toast.LENGTH_SHORT).show();

                                    Intent back = new Intent(SendNotification.this, DriverHome.class);
                                    startActivity(back);
                                    finish();
                                }
                            });


                        }
                        else{
                            Toast.makeText(SendNotification.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent back = new Intent(SendNotification.this, DriverHome.class);
        startActivity(back);
        finish();
    }
}
