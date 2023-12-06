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
        int user = getIntent().getIntExtra("userid", 0);

        try{
            // Get username and other user info
            String query = "SELECT * FROM users WHERE userid = " + friend;
            SQLConnect con = new SQLConnect();
            ResultSet result = con.getResultsFromSQL(query);
            if(result.first()) {
                username.setText(result.getString("username"));
                bio.setText(result.getString("bio"));
                pfp.setImageBitmap(AccountView.getProfilePicture(friend));
                watchlist.setLayoutManager(new GridLayoutManager(this, 3));
                query = "SELECT * FROM watchlist WHERE userid = " + friend;
                SQLConnect con2 = new SQLConnect();
                ResultSet result2 = con2.getResultsFromSQL(query);

                ArrayList<Movie> movieResults = new ArrayList<Movie>();
                while(result2.next()){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    // Get Movies from search query
                    JSONObject movies = Movie.readJsonFromUrl(BASE_URL+result2.getInt("movie")+API_KEY);

                    String id = movies.getString("id");
                    String title = movies.getString("title");
                    String desc = movies.getString("overview");
                    String img_url = IMG_URL + movies.getString("backdrop_path");
                    Movie m = new Movie(id, title, desc, img_url);
                    movieResults.add(m);
                }
                con2.closeConnection();
                movieAdapter.setData(movieResults);
                movieAdapter.setOnItemClickListener(position -> {
                    Movie m = movieResults.get(position);

                    Intent movieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                    movieDetails.putExtra("movieid", m.getId());
                    startActivity(movieDetails);
                });
                watchlist.setAdapter(movieAdapter);
            }
            con.closeConnection();

        }catch (Exception e){
            Log.e("MOVIEDB", "Invalid user");
            Log.e("MOVIEDB", e.toString());
        }

        // Set button to say add or remove
        try {
            String query = "SELECT * FROM friendship";
            SQLConnect con3 = new SQLConnect();
            ResultSet rs = con3.getResultsFromSQL(query);
            String userid = String.valueOf(user);
            String friendid = String.valueOf(friend);
            while (rs.next()) {
                if(rs.getString("user1").equals(userid) && rs.getString("user2").equals(friendid)){
                    addFriend.setText(R.string.remove_friend);
                }
            }
            con3.closeConnection();
        }catch (Exception e){
            Log.e("SQL", e.toString());
        }

        addFriend.setOnClickListener(l -> {
            // Set button to add or remove
            boolean friends = false;
            try {
                String query = "SELECT * FROM friendship";
                SQLConnect con4 = new SQLConnect();
                ResultSet rs = con4.getResultsFromSQL(query);
                String userid = String.valueOf(user);
                String friendid = String.valueOf(friend);
                while (rs.next()) {
                    String user1 = rs.getString("user1");
                    String user2 = rs.getString("user2");
                    if(user1.equals(userid) && user2.equals(friendid)){
                        friends = true;
                        addFriend.setText("Remove Friend");
                    }
                }
                con4.closeConnection();
            }catch (Exception e){
                Log.e("SQL", e.toString());
            }
            try{
                String inquery = "INSERT IGNORE INTO friendship (user1, user2) VALUES (" +
                        getIntent().getIntExtra("userid", 0) + ", " +
                        getIntent().getIntExtra("friendid", 0) + ");";
                addFriend.setText(R.string.remove_friend);
                if(friends) {
                    inquery = "DELETE FROM friendship WHERE " +
                            "user1 = " +
                            getIntent().getIntExtra("userid", 0) +
                            " AND user2 = " +
                            getIntent().getIntExtra("friendid", 0);
                    addFriend.setText(R.string.add_friend);
                }
                SQLConnect con = new SQLConnect();
                con.getResultsFromSQL(inquery);
                con.closeConnection();
            }catch (Exception e){
                Log.e("DB", e.toString());
            }
        });
    }
}