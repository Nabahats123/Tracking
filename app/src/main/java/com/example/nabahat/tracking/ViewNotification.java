package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewNotification extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference ref;
    ListView listiew;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.notif, R.id.info, list);
        notification = new Notification();
        listiew = (ListView)findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Notification").child("NotificationbyDriver");

        ref = database.getReference().child("Notification");
        //dbref.child().getKey();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String jey =  ds.getKey();

                    notification = ds.getValue(Notification.class);
                    String busvalue = ds.child("bus").getValue(String.class);

                    String busmessage = ds.child("message").getValue(String.class);
                    String bussender = ds.child("sender").getValue(String.class);
                    //Toast.makeText(ViewNotification.this,busvalue+busmessage+bussender,Toast.LENGTH_SHORT).show();
                    notification.setBus(busvalue);
                    notification.setMessage(busmessage);
                    notification.setSender(bussender);
                    list.add(busmessage+" sent by "+bussender);

                }
                listiew.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goback = new Intent(ViewNotification.this, DriverHome.class);
        startActivity(goback);
        finish();
    }
}
