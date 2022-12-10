package cw.migcrud.feature.storage;

// Singleton

import cw.migcrud.feature.prefs.Prefs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Storage {
    private static final Storage INSTANCE = new Storage();
    private Connection connection;
    private Storage() {
        try{
            String connectionUrl = new Prefs().getString(Prefs.DB_URL);
            connection = DriverManager.getConnection(connectionUrl);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static Storage getInstance(){
        return INSTANCE;
    }

    public int executeUpdate(String sql){
        try(Statement st = connection.createStatement()) {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Connection getConnection(){
        return connection;
    }
    public void close(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

