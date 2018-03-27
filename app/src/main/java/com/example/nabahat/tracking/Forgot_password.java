package com.example.nabahat.tracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_password extends AppCompatActivity {
    private static View view;

    private static EditText emailId;
    private static TextView submit, back;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    //
    // FirebaseAuth.AuthStateListener firebaseauthlistener;
    Driver driver;
    String id;
    Intent DriverAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailId = (EditText) findViewById(R.id.registered_emailid);
        submit = (TextView) findViewById(R.id.forgot_button);
        back = (TextView) findViewById(R.id.backToLoginBtn);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Forgot_password.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmailId = emailId.getText().toString();

                // Pattern for email id validation
                Pattern p = Pattern.compile(Utils.regEx);

                // Match the pattern
                Matcher m = p.matcher(getEmailId);

                // First check if email id is not null else show error toast
                if (getEmailId.equals("") || getEmailId.length() == 0){

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Please enter your Email Id");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast
                }



                    // Check if email id is valid or not
                else if (!m.find()){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Your Email Id is Invalid.");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast

                }

                    // Else submit email id and fetch passwod or do your stuff
                else {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Clicked");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast

                }

            }
        });



    }

}
