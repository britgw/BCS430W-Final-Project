package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountRegister extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        // Get UI elements
        Button button = findViewById(R.id.registerConfirmButton);
        TextView username = findViewById(R.id.registerUsername);
        TextView password = findViewById(R.id.registerPassword);
        TextView password2 = findViewById(R.id.registerConfirmPassword);
        TextView firstName = findViewById(R.id.registerFirstName);
        TextView lastName = findViewById(R.id.registerLastName);
        TextView email = findViewById(R.id.registerEmail);

        button.setOnClickListener(v -> {
            if(password.getText().toString().equals(password2.getText().toString()) &&
                    !username.getText().toString().equals("") &&
                    !password.getText().toString().equals("") &&
                    !firstName.getText().toString().equals("") &&
                    !lastName.getText().toString().equals("") &&
                    !email.getText().toString().equals("")) {
                // TODO: Hash passwords
                String query = "INSERT INTO users (username, firstname, lastname, email, password) " +
                        "VALUES ('" +
                        username.getText().toString() + "', '" +
                        firstName.getText().toString() + "', '" +
                        lastName.getText().toString() + "', '" +
                        email.getText().toString() + "', '" +
                        password.getText().toString() + "')";
                SQLConnect con = new SQLConnect();
                ResultSet result = con.getResultsFromSQL(query);
                try {
                    //String response = result.getMetaData().getColumnName(0);
                    if(result.first()) {
                        String response = result.getString(1);
                        getIntent().putExtra("userid", Integer.parseInt(response));
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("SQL", response);

                    }else{
                        Log.e("SQL", "Statement returns nothing");
                    }
                }catch (Exception e){
                    Log.e("SQL", e.toString());
                }

                con.closeConnection();
                finish();
            }else{
                if(!username.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                else if(!password.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                else if(!firstName.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid First Name", Toast.LENGTH_SHORT).show();
                else if(!lastName.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid Last Name", Toast.LENGTH_SHORT).show();
                else if(!email.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                else if(password.getText().toString().equals(password2.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}