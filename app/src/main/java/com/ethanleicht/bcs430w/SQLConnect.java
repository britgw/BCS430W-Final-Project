package com.ethanleicht.bcs430w;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnect {
    Connection con;
    String user, pw, ip, port, db;

    public Connection getCon() {
        ip = "192.168.1.60";
        db = "BCS430W";
        user = "admin";
        pw = "password";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionUrl = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionUrl = "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+db+";user="+user+";password"+pw+";";
            connection = DriverManager.getConnection(connectionUrl);
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }

        return connection;
    }

    public ResultSet getResultsFromSQL(String query){
        String result = "";
        try{
            SQLConnect connectionClass = new SQLConnect();
            con = connectionClass.getCon();
            if(con != null){
                // String query = "SELECT * FROM MOVIES";
                Statement st = con.createStatement();
                return st.executeQuery(query);
            }
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }
        return null;
    }
}
