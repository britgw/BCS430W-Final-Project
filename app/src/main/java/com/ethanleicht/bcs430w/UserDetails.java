package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDetails extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        ImageView pfp = findViewById(R.id.userDetailsPfp);
        TextView username = findViewById(R.id.userDetailsUsername);
        TextView bio = findViewById(R.id.userDetailsBio);
        Button addFriend = findViewById(R.id.addFriendButton);
        RecyclerView watchlist = findViewById(R.id.userWatchlistView);

        MovieAdapter movieAdapter = new MovieAdapter(new ArrayList<Movie>());
        int friend = getIntent().getIntExtra("friendid", 0);

        try{
            // Get username and other user info
            String query = "SELECT * FROM users WHERE userid = " + friend;
            ResultSet result = SQLConnect.getResultsFromSQL(query);
            if(result.first()) {
                username.setText(result.getString("username"));
                bio.setText(result.getString("bio"));
                pfp.setImageBitmap(AccountView.getProfilePicture(friend));
                SQLConnect.closeConnection();
                watchlist.setLayoutManager(new GridLayoutManager(this, 3));
                query = "SELECT * FROM watchlist WHERE userid = " + friend;
                result = SQLConnect.getResultsFromSQL(query);

                ArrayList<Movie> movieResults = new ArrayList<Movie>();
                while(result.next()){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    // Get Movies from search query
                    JSONObject movies = Movie.readJsonFromUrl(BASE_URL+result.getInt("movie")+API_KEY);

                    String id = movies.getString("id");
                    String title = movies.getString("title");
                    String desc = movies.getString("overview");
                    String img_url = IMG_URL + movies.getString("backdrop_path");
                    Movie m = new Movie(id, title, desc, img_url);
                    movieResults.add(m);
                }
                movieAdapter.setData(movieResults);
                movieAdapter.setOnItemClickListener(position -> {
                    Movie m = movieResults.get(position);

                    Intent movieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                    movieDetails.putExtra("movieid", m.getId());
                    startActivity(movieDetails);
                });
                watchlist.setAdapter(movieAdapter);
            }
        }catch (Exception e){
            Log.e("MOVIEDB", "Invalid user");
            Log.e("MOVIEDB", e.toString());
        }



        addFriend.setOnClickListener(l -> {
            try{
                String inquery = "INSERT IGNORE INTO friendship (user1, user2) VALUES (" +
                        getIntent().getIntExtra("userid", 0) + ", " +
                        getIntent().getIntExtra("friendid", 0) + ");";
                ResultSet inresult = SQLConnect.getResultsFromSQL(inquery);
                SQLConnect.closeConnection();
            }catch (Exception e){
                Log.e("DB", e.toString());
            }
        });
    }
}