package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText DriverName, DriverEmail, DriverPhone, DriverPassword, DriverBusNumber;
    Button SignUp, SignIn;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        firebaseauthlistener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!= null){
                    Intent OpenMap = new Intent(MainActivity.this, DriverMapsActivity.class);
                    startActivity(OpenMap);
                    finish();
                    return;
                }
            }
        };



        DriverName = (EditText) findViewById(R.id.lay_name);
        DriverEmail = (EditText) findViewById(R.id.lay_email);
        DriverPhone = (EditText) findViewById(R.id.lay_phone);
        DriverPassword = (EditText) findViewById(R.id.lay_password);
        DriverBusNumber = (EditText) findViewById(R.id.lay_busnumber);
        SignIn = (Button) findViewById(R.id.lay_signin);
        SignUp = (Button) findViewById(R.id.lay_signup);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = DriverEmail.getText().toString();
                final String password = DriverPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();
 
                        }else{

                        }
                    }
                });

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = DriverEmail.getText().toString();
                final String password = DriverPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            String user_Id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Driver").child(user_Id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseauthlistener);
    }
}
