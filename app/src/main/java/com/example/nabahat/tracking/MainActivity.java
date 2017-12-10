package com.example.nabahat.tracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText DriverName, DriverEmail, DriverPhone, DriverPassword, DriverBusNumber;
    Button SignUp, SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Intent OpenMap = new Intent(MainActivity.this, DriverMapsActivity.class);
                startActivity(OpenMap);

            }
        });
    }
}
