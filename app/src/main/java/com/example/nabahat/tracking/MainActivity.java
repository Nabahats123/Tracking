package com.example.nabahat.tracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AlertDialog.Builder builder;
    EditText DriverName, DriverEmail, DriverPhone, DriverPassword, DriverBusNumber;
    TextView SignUp;
    Button SignIn;
    private static FragmentManager fragmentManager;
    private static TextView forgotPassword;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Driver driver;
    String id;
    Intent DriverAct;

    FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    public void onClick(View v) {


           // Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            Intent register = new Intent(MainActivity.this, SignUp.class);
            startActivity(register);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }*/

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

        firebaseauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent OpenMap = new Intent(MainActivity.this, DriverHome.class);
//                    OpenMap.putExtra("Bus Number", DriverBusNumber.getText().toString());
                    startActivity(OpenMap);

                    finish();
                }
/*
                if (user.isEmailVerified()){
                    Toast.makeText(MainActivity.this, "verified" , Toast.LENGTH_SHORT).show();
                }
                    else {

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "not veriied and sent" , Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
*/

                return;
            }
        };
        //   DriverName = (EditText) findViewById(R.id.lay_name);
        DriverEmail = (EditText) findViewById(R.id.lay_email);
        //    DriverPhone = (EditText) findViewById(R.id.lay_phone);
        DriverPassword = (EditText) findViewById(R.id.lay_password);
        //     DriverBusNumber = (EditText) findViewById(R.id.lay_busnumber);
        SignIn = (Button) findViewById(R.id.lay_signin);
        SignUp = (TextView) findViewById(R.id.lay_signup);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);

        shakeAnimation = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake);

        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            SignUp.setTextColor(csl);
        } catch (Exception e) {
        }
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            DriverPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            DriverPassword.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            DriverPassword.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            DriverPassword.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forget = new Intent(MainActivity.this, Forgot_password.class);
                startActivity(forget);
                finish();
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = DriverEmail.getText().toString();
                final String password = DriverPassword.getText().toString();
                final FirebaseUser usercheck = FirebaseAuth.getInstance().getCurrentUser();
                // Get email id and password
                String getEmailId = DriverEmail.getText().toString();
                String getPassword = DriverPassword.getText().toString();

                // Check patter for email id
                Pattern p = Pattern.compile(Utils.regEx);

                Matcher m = p.matcher(getEmailId);

                // Check for both field is empty or not
                if (getEmailId.equals("") || getEmailId.length() == 0
                        || getPassword.equals("") || getPassword.length() == 0) {
//                    loginLayout.startAnimation(shakeAnimation);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Enter both credentials");

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

                    // new CustomToast().Show_Toast(MainActivity.class, this,
                    //"Enter both credentials.");

                }
                // Check if email id is valid or not
                else if (!m.find()) {
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
                //new CustomToast().Show_Toast(getActivity(), view,
                //        "Your Email Id is Invalid.");
                // Else do login and do your stuff


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //  Toast.makeText(MainActivity.this, "Sign In Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            checkIfEmailVerified();
                            // Toast.makeText(MainActivity.this, "Email is verified", Toast.LENGTH_SHORT).show();
                        }


                        //DriverAct.putExtra("Bus Number", DriverBusNumber.getText().toString());
                    }
                });


            }
        });

        SignUp.setOnClickListener(this);
    }
         /*       final String DName = DriverName.getText().toString();
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
}*/






    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
 @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop(){
        super.onStop();
       // mAuth.removeAuthStateListener(firebaseauthlistener);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mAuth.removeAuthStateListener(firebaseauthlistener);
        finish();


    }
}
