package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.ResultSet;

public class AccountView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);

        // Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        myToolbar.setTitle("My Profile");
        setSupportActionBar(myToolbar);

        TextView usernameView = findViewById(R.id.pfUsername);

        int user = getIntent().getIntExtra("userid", 0);

        try{
            // Get username and other user info
            String query = "SELECT * FROM users WHERE userid = " + user;
            ResultSet result = SQLConnect.getResultsFromSQL(query);
            if(result.first()) {
                usernameView.setText(result.getString("username"));
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
        // Handle item selection.
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