package com.ethanleicht.bcs430w;

import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SQLConnect {
    private Connection con;
    private ResultSet result;
    private static String ip = "";


    public static ArrayList<Movie> getWatchlist(int userid){
        ArrayList<Movie> list = new ArrayList<Movie>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String checkUser = "http://"+ip+"/BCS430w/GetWatchlist.php" +
                    "?user=" + userid;
            URL url = new URL(checkUser);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while(in.ready()){
                Movie m = Movie.getMovieById(in.readLine());
                list.add(m);
            }
            in.close();
            con.disconnect();
        }catch (Exception e){
            Log.e("MOVIEDB", e.toString());
        }
        return list;
    }
    public static int GetUserId(String username, String password){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String checkUser = "http://"+ip+"/BCS430w/GetUserID.php" +
                    "?username=" + username +
                    "&password=" + password;
            URL url = new URL(checkUser);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            int userid = Integer.parseInt(in.readLine());
            in.close();
            con.disconnect();
            return userid;
        }catch (Exception e){
            Log.e("MOVIEDB", e.toString());
        }
        return 0;
    }

    // Connects to the SQL Server
    public Connection getCon() {
        // Connect to SQL server
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/rustdb?useSSL=false", "Guest", "");
        }catch (Exception e){
            Log.e("SQL", e.toString());
            Log.e("SQL", "Unable to connect");
        }
        return connection;
    }

    // Query SQL server
    public ResultSet getResultsFromSQL(String query){
        try{
            SQLConnect connectionClass = new SQLConnect();
            con = connectionClass.getCon();
            if(con != null){
                Statement st = con.createStatement();
                if(query.toUpperCase().startsWith("SELECT")) {
                    result = st.executeQuery(query);
                    return st.executeQuery(query);
                }else{
                    st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                    result = st.getGeneratedKeys();
                    return st.getGeneratedKeys();
                    /*if(result == 0){
                        return st.executeQuery("SELECT 'Database updated' AS 'Response'");
                    }else{
                        return st.executeQuery("SELECT 'Nothing changed' AS 'Response'");
                    }*/
                }
            } else {
                Log.e("SQL", "No connection");
            }
        }catch (Exception e){
            Log.e("SQL", e.toString());
            Log.e("SQL", e.getMessage());
        }
        return null;
    }
    public ResultSet getResult(){
        return result;
    }
    public SQLConnect(String query){
        getResultsFromSQL(query);
    }
    public SQLConnect(){    }
    public void closeConnection(){
        try{
            con.close();
        }catch (Exception e){
            Log.e("SQL", e.toString());
        }
    }
}
