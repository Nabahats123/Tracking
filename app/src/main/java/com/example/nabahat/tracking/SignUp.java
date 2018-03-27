package com.example.nabahat.tracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  SignUp extends AppCompatActivity implements View.OnClickListener {
    AlertDialog.Builder builder;
    private EditText DriverName, DriverEmail, DriverPhone, DriverPassword, DriverBusNumber, confirmPassword;
    TextView Already_User;
    Button SignUp;
    private static FragmentManager fragmentManager;
    private static TextView forgotPassword;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;

    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;



    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    //FirebaseAuth.AuthStateListener firebaseauthlistener;
    Driver driver;
    String id;
    Intent DriverAct;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method



                break;

            case R.id.already_user:

                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.close_activity).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });
        Toast.makeText(SignUp.this, "here", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        id = mDatabase.push().getKey();
        DriverName = (EditText) findViewById(R.id.lay_name);
        DriverEmail = (EditText) findViewById(R.id.lay_emailSignup);
        DriverPhone = (EditText) findViewById(R.id.lay_phone);
        DriverPassword = (EditText) findViewById(R.id.lay_passwordsignup);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        DriverBusNumber = (EditText) findViewById(R.id.lay_routenumber);
        SignUp = (Button) findViewById(R.id.signUpBtn);
        Already_User = (TextView) findViewById(R.id.already_user);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        terms_conditions = (CheckBox)findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }

        SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                final String getFullName = DriverName.getText().toString();
                final String getEmailId = DriverEmail.getText().toString();
                final String getMobileNumber = DriverPhone.getText().toString();
                final String getLocation = DriverBusNumber.getText().toString();
                final String getPassword = DriverPassword.getText().toString();
                String getConfirmPassword = confirmPassword.getText().toString();

                // Pattern match for email id
                Pattern p = Pattern.compile(Utils.regEx);
                Matcher m = p.matcher(getEmailId);

                // Check if all strings are null or not
                if (getFullName.equals("") || getFullName.length() == 0
                        || getEmailId.equals("") || getEmailId.length() == 0
                        || getMobileNumber.equals("") || getMobileNumber.length() == 0
                        || getLocation.equals("") || getLocation.length() == 0
                        || getPassword.equals("") || getPassword.length() == 0
                        || getConfirmPassword.equals("")
                        || getConfirmPassword.length() == 0)
                {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("All fields are required");

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


                    // Check if email id valid or not
                else if (!m.find()){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Your Email Id is Invalid");

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


                    // Check if both password should be equal
                else if (!getConfirmPassword.equals(getPassword)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Both password doesn't match");

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
                    // Make sure user should check Terms and Conditions checkbox
                else if (!terms_conditions.isChecked()) {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Please select Terms and Conditions");

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
                    // Else do signup or do your stuff
                else {
                    mAuth.createUserWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                // inflate the layout over view
                                View layout = inflater.inflate(R.layout.custom_toast,
                                        (ViewGroup) findViewById(R.id.toast_root));

                                // Get TextView id and set error
                                TextView text = (TextView) layout.findViewById(R.id.toast_error);
                                text.setText(task.getException().getMessage());

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

                            } else {
                                String user_Id = mAuth.getCurrentUser().getUid();

                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);
                                current_user_db.setValue(true);
                                Driver driver = new Driver(user_Id, getFullName, getEmailId, getPassword, getMobileNumber, getLocation);
                                current_user_db.setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                            // inflate the layout over view
                                            View layout = inflater.inflate(R.layout.custom_toast,
                                                    (ViewGroup) findViewById(R.id.toast_root));

                                            // Get TextView id and set error
                                            TextView text = (TextView) layout.findViewById(R.id.toast_error);
                                            text.setText(task.getException().getMessage());

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

        Already_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(SignUp.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        });

        shakeAnimation = AnimationUtils.loadAnimation(SignUp.this,
                R.anim.shake);
    }
    @Override
    protected void onStart(){
        super.onStart();
       // mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //mAuth.removeAuthStateListener(firebaseauthlistener);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mAuth.removeAuthStateListener(firebaseauthlistener);
        finish();


    }




}
