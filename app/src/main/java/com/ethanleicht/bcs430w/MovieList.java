package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.ResultSet;

public class MovieList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        TextView list = findViewById(R.id.list);

        try {
            Intent login = getIntent();
            String username = login.getStringExtra("username");
            String password = login.getStringExtra("password");
            ResultSet movies = SQLConnect.getResultsFromSQL("SELECT * FROM MOVIES;", username, password);
            // TODO: list all movies
            if(movies != null)
                list.setText(movies.getString(1));
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }

    }
}