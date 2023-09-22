package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    Connection connect;
    String cResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import buttons and text inputs
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView username = (TextView) findViewById(R.id.usernameInput);
        TextView password = (TextView) findViewById(R.id.passwordInput);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Log goes to Logcat (look at the bottom of this window
                Log.d("LOGIN", "Username: " + username.getText().toString());
                Log.d("LOGIN", "Password: " + password.getText().toString());
                startActivity(new Intent(getApplicationContext(), MovieList.class));
                // Check login information
                if(username.getText().toString() == "admin" || password.getText().toString() == "admin") {
                    startActivity(new Intent(getApplicationContext(), MovieList.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}