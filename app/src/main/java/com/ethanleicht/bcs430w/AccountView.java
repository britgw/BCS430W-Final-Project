package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;

public class AccountView extends AppCompatActivity {
    public Bitmap getProfilePicture(int userid){
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

        int user = getIntent().getIntExtra("userid", 0);

        try{
            // Get username and other user info
            String query = "SELECT * FROM users WHERE userid = " + user;
            ResultSet result = SQLConnect.getResultsFromSQL(query);
            if(result.first()) {
                usernameView.setText(result.getString("username"));
                bioView.setText(result.getString("bio"));
                pfp.setImageBitmap(getProfilePicture(user));
                SQLConnect.closeConnection();
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
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}