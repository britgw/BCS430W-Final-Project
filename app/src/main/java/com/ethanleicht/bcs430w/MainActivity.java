package com.ethanleicht.bcs430w;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
                // Check login information
                Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
            }
        });
    }
}