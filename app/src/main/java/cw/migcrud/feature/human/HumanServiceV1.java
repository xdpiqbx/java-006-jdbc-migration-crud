package cw.migcrud.feature.human;

import cw.migcrud.feature.storage.Storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class HumanServiceV1 {
    private Storage storage;
    public HumanServiceV1(Storage storage){
        this.storage = storage;
    }

    public void printHumanInfo(long id){
        try(Statement st = storage.getConnection().createStatement()){
            String sql = "SELECT name, birthday FROM human WHERE id = " + id;
            try(ResultSet rs = st.executeQuery(sql)){
                if(rs.next()){
                    String name = rs.getString("name");
                    String birthday = rs.getString("birthday");
//                    System.out.println("name: "+name+" birthday: "+birthday);
                }else{
                    System.out.println("Human not found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void printHumanIds(){
        try(Statement st = storage.getConnection().createStatement()){
            try(ResultSet rs = st.executeQuery("SELECT id FROM human")){
                while(rs.next()){
                    long id = rs.getLong("id");
                    System.out.println("ID: "+id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewHuman(String name, LocalDate birthday){
        String insertSQL = String.format(
            "INSERT INTO human (name, birthday) VALUES ('%s', '%s')",
            name, birthday
        );
        storage.executeUpdate(insertSQL);
    }
}
