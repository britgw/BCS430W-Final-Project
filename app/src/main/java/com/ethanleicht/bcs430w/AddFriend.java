package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddFriend extends AppCompatActivity {

    protected void updateSearchResults(){
        TextView search = findViewById(R.id.friendSearchBar);

        RecyclerView userList = findViewById(R.id.friendSearchView);
        userList.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> pfps = new ArrayList<String>();
        UserAdapter userAdapter = new UserAdapter(new ArrayList<String>(),new ArrayList<String>());

        search.setOnKeyListener((l, k, j) -> {
            try{
                String query = "SELECT username, userid, pfp FROM users " +
                        "WHERE username LIKE '%" + search.getText().toString() + "%' " +
                        "AND userid != " + getIntent().getIntExtra("userid", 0);
                SQLConnect con = new SQLConnect();
                ResultSet result = con.getResultsFromSQL(query);
                while (result.next()) {
                    usernames.add(result.getString("username"));
                    if (result.getBlob("pfp") != null) {
                        String imageUrl = result.getString("userid");
                        pfps.add(imageUrl);
                    }
                }
                con.closeConnection();
                ArrayList<String> usernameList = new ArrayList<String>();
                for(String u: usernames){
                    if(!usernameList.contains(u))
                        usernameList.add(u);
                }
                userAdapter.setData(usernameList, pfps);
                userAdapter.setOnItemClickListener(position -> {
                    try {
                        String getquery = "SELECT userid FROM users " +
                                "WHERE username = '" + usernames.get(position) + "';";
                        ResultSet getresult = con.getResultsFromSQL(getquery);
                        if (getresult.first()) {
                            int userid = getresult.getInt("userid");
                            con.closeConnection();
                            Intent friend = new Intent(getApplicationContext(), UserDetails.class);
                            friend.putExtra("userid", getIntent().getIntExtra("userid", 0));
                            friend.putExtra("friendid", userid);
                            startActivity(friend);
                        }
                    }catch(Exception e){
                        Log.e("UserDB", e.toString());
                    }
                });
                userList.setAdapter(userAdapter);
            }catch (Exception e){
                Log.e("SQL", "can't find friends");
                Log.e("SQL", e.toString());
            }
            return true;
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        TextView search = findViewById(R.id.friendSearchBar);
        search.setText("");

        updateSearchResults();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateSearchResults();
    }
}