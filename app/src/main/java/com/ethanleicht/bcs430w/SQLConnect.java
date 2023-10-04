package com.ethanleicht.bcs430w;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnect {
    static Connection con;

    // Connects to the SQL Server
    public Connection getCon(String username, String password) {
        // Connect to SQL server
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://70.107.82.4:3306/test?useSSL=false", username, password);
        }catch (Exception e){
            Log.e("SQL", e.toString());
            Log.e("SQL", "Unable to connect");
        }
        return connection;
    }

    // Query SQL server
    public static ResultSet getResultsFromSQL(String query, String username, String password){
        String result = "";
        try{
            SQLConnect connectionClass = new SQLConnect();
            con = connectionClass.getCon(username, password);
            if(con != null){
                Statement st = con.createStatement();
                ResultSet statement = st.executeQuery(query);
                return statement;
            } else {
                Log.e("SQL", "No connection");
            }
        }catch (Exception e){
            Log.e("SQL", e.toString());
        }
        return null;
    }
    public static void closeConnection(){
        try{
            con.close();
        }catch (Exception e){
            Log.e("SQL", e.toString());
        }
    }

}
