package com.example.nabahat.tracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    EditText DriverName, DriverEmail, DriverPhone, DriverPassword, DriverBusNumber;
    TextView SignUp, SignIn;
    private static FragmentManager fragmentManager;

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    Driver driver;
    String id;
    Intent DriverAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }

        // On close icon click finish activity
        findViewById(R.id.close_activity).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        id = mDatabase.push().getKey();

        firebaseauthlistener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!= null){
                    Intent OpenMap = new Intent(MainActivity.this, DriverHome.class);
                    OpenMap.putExtra("Bus Number", DriverBusNumber.getText().toString());
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
        SignIn = (TextView) findViewById(R.id.lay_signin);
        SignUp = (TextView) findViewById(R.id.lay_signup);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = DriverEmail.getText().toString();
                final String password = DriverPassword.getText().toString();
                if (email.equals("") || password.equals("") ) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Error")
                            .setMessage("Please enter Email and Password")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                              //  Toast.makeText(MainActivity.this, "Sign In Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                builder.setTitle("Error")
                                        .setMessage(task.getException().getMessage())
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();


                            }

                            //DriverAct.putExtra("Bus Number", DriverBusNumber.getText().toString());
                        }
                    });
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String DName = DriverName.getText().toString();
                final String DEmail = DriverEmail.getText().toString();
                final String DPassword = DriverPassword.getText().toString();
                final String DPhone = DriverPhone.getText().toString();
                final String DBusNumber = DriverBusNumber.getText().toString();
                if (DName.equals("") || DPassword.equals("") || DBusNumber.equals("") || DEmail.equals("") || DPhone.equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Error")
                            .setMessage("Please Enter All Details")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                mAuth.createUserWithEmailAndPassword(DEmail, DPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sign Up Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            String user_Id = mAuth.getCurrentUser().getUid();

                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);
                            current_user_db.setValue(true);
                            Driver driver = new Driver(user_Id, DName, DEmail, DPassword, DPhone, DBusNumber);
                            current_user_db.setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        AlertDialog.Builder builder;
                                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                        builder.setTitle("Error")
                                                .setMessage(task.getException().getMessage())
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // continue with delete
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                      //  Toast.makeText(MainActivity.this, "Record Not Added" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }

            }
        });

    }
    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
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
    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);
        Fragment ForgotPassword_Fragment = fragmentManager
                .findFragmentByTag(Utils.ForgotPassword_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }
}
