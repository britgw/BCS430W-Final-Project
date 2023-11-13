package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountView extends AppCompatActivity {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    public static Bitmap getProfilePicture(int userid){
        try{
            URL urlConnection = new URL("http://108.14.0.126/BCS430w/"+userid+".png");
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            return bmp;
        }catch (Exception e){
            try{
                URL urlConnection = new URL("http://108.14.0.126/BCS430w/"+userid+".jpg");
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(input);
                return bmp;
            }catch (Exception ex){
                Log.e("MOVIEDB", "No Image");
                Log.e("MOVIEDB", e.toString());
                Log.e("MOVIEDB", ex.toString());
                return null;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);

        // Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        myToolbar.setTitle("My Profile");
        setSupportActionBar(myToolbar);

        TextView usernameView = findViewById(R.id.pfUsername);
        TextView bioView = findViewById(R.id.pfBio);
        ImageView pfp = findViewById(R.id.pfPicture);
        RecyclerView watchlist = findViewById(R.id.currentUserWatchlist);

        MovieAdapter movieAdapter = new MovieAdapter(new ArrayList<Movie>());

        int user = getIntent().getIntExtra("userid", 0);

        try{
            // Get username and other user info
            String query = "SELECT * FROM users WHERE userid = " + user;
            SQLConnect con = new SQLConnect();
            ResultSet result = con.getResultsFromSQL(query);
            if(result.first()) {
                usernameView.setText(result.getString("username"));
                bioView.setText(result.getString("bio"));
                pfp.setImageBitmap(getProfilePicture(user));
                con.closeConnection();
                watchlist.setLayoutManager(new GridLayoutManager(this, 3));
                query = "SELECT * FROM watchlist WHERE userid = " + user;
                result = con.getResultsFromSQL(query);

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
                con.closeConnection();
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Add more pages: watchlist, add friends, edit profile, etc.
        if(item.getItemId() == R.id.friend_list) {
            Intent friendList = new Intent(getApplicationContext(), FriendList.class);
            int user = getIntent().getIntExtra("userid", 0);
            friendList.putExtra("userid", user);
            startActivity(friendList);
            return true;
        }else if(item.getItemId() == R.id.addFriend) {
            Intent addFriend = new Intent(getApplicationContext(), AddFriend.class);
            int user = getIntent().getIntExtra("userid", 0);
            addFriend.putExtra("userid", user);
            startActivity(addFriend);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}