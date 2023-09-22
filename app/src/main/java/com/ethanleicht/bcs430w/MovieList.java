package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

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
            // TODO: make this actually search
            ResultSet movies = SQLConnect.getResultsFromSQL("SELECT * FROM MOVIES");
            // TODO: list all movies
            list.setText(movies.getString(1));
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }

    }
}