package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
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
            if(movies != null) {
                movies.first();
                list.setText(movies.getString(2));
            } else {
                list.setText("ERROR");
            }
            list.setText("");
            WebView webView = findViewById(R.id.webView);
            webView.loadUrl("http://70.107.82.4/home");
            SQLConnect.closeConnection();
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }
    }
}