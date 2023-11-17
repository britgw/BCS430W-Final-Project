package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MovieReviewAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review_add);

        RatingBar ratingBar = findViewById(R.id.addReviewRatingBar);
        Button submitButton = findViewById(R.id.submitReview);
        TextView textBox = findViewById(R.id.reviewTextBox);

        int user = getIntent().getIntExtra("userid", 0);

        submitButton.setOnClickListener(l -> {
            String query = "INSERT IGNORE INTO moviereview (userid, movieid, description, rating) VALUES (" +
                    user + ", " +
                    getIntent().getStringExtra("movieid") + ", '" +
                    textBox.getText().toString() + "', " +
                    (int)ratingBar.getRating() + ")";
            SQLConnect con = new SQLConnect(query);
            con.closeConnection();
            Toast.makeText(getApplicationContext(), "Review Submitted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}