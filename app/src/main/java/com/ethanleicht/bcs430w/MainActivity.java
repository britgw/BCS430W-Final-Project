package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import buttons and text inputs
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView username = (TextView) findViewById(R.id.usernameInput);
        TextView password = (TextView) findViewById(R.id.passwordInput);

        loginButton.setOnClickListener(v -> {
            // Log goes to Logcat (look at the bottom of this window
            Log.d("LOGIN", "Username: " + username.getText().toString());
            Log.d("LOGIN", "Password: " + password.getText().toString());
            // Check login information
            if(!username.getText().toString().equals("") && !password.getText().toString().equals("") || true) {
                try{
                    // Validate username and password   TODO: Decrypt passwords
                    String query = "SELECT * FROM users WHERE username = '"+username.getText().toString()+"' AND userid = "+password.getText().toString();
                    ResultSet result = SQLConnect.getResultsFromSQL(query);
                    if(result != null && result.first()){
                        // Go to MovieList page
                        int user = result.getInt("userid");
                        Intent movieList = new Intent(getApplicationContext(), MovieList.class);
                        movieList.putExtra("userid", (int) result.getInt("userid"));
                        SQLConnect.closeConnection();
                        startActivity(movieList);
                    }else{
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    Log.e("SQL", e.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
            }
        });
    }
}