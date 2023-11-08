package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import buttons and text inputs
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button guestButton = (Button) findViewById(R.id.guestButton);
        TextView username = (TextView) findViewById(R.id.usernameInput);
        TextView password = (TextView) findViewById(R.id.passwordInput);

        guestButton.setOnClickListener(v -> {
            Intent movieList = new Intent(getApplicationContext(), MovieList.class);
            startActivity(movieList);
        });
        registerButton.setOnClickListener(v -> {
            Intent registerView = new Intent(getApplicationContext(), AccountRegister.class);
            startActivity(registerView);
        });

        loginButton.setOnClickListener(v -> {
            // Check login information
            if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                try{
                    // Validate username and password   TODO: Decrypt passwords with PHP code
                    String checkUsername = "username = '"+username.getText().toString()+"'";
                    String checkPassword = "userid = "+password.getText().toString();
                    // Get Password from PHP
                    /*try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        String checkUser = "http://108.14.0.126/BCS430w/GetUserID.php" +
                                "?username=" + username.getText().toString() +
                                "&password="+password.getText().toString();
                        URL url = new URL(checkUser);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        checkPassword = "userid = " + in.readLine();
                        in.close();
                        con.disconnect();
                    }catch (Exception e){
                        Log.e("PHP", e.toString());
                        //checkPassword = "false";
                    }*/

                    String query = "SELECT * FROM users WHERE "+checkUsername+" AND "+checkPassword;
                    ResultSet result = SQLConnect.getResultsFromSQL(query);

                    if(result != null && result.first()){
                        // Go to MovieList page
                        int user = result.getInt("userid");
                        Intent movieList = new Intent(getApplicationContext(), MovieList.class);
                        movieList.putExtra("userid", result.getInt("userid"));
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