package cw.migcrud.feature.human;

import cw.migcrud.feature.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HumanServiceV2 {
    private Connection conn;
    private final PreparedStatement insertSt;
    private final PreparedStatement selectByIdSt;
    private final PreparedStatement selectAllSt;
    private final PreparedStatement renameSt;

    public HumanServiceV2(Storage storage) throws SQLException {
        conn = storage.getConnection();

        insertSt = conn.prepareStatement(
                "INSERT INTO human (name, birthday) VALUES (?, ?)"
        );
        selectByIdSt = conn.prepareStatement(
                "SELECT name, birthday FROM human WHERE id = ?"
        );
        selectAllSt = conn.prepareStatement(
                "SELECT id FROM human"
        );
        renameSt = conn.prepareStatement(
                "UPDATE human SET name=? WHERE name=?"
        );
    }
    public boolean printHumanInfo(long id){
        try {
            selectByIdSt.setLong(1, id);
        } catch (SQLException e) {
            return false;
        }

        try (ResultSet rs = selectByIdSt.executeQuery()){
            if(!rs.next()){
//                System.out.println("Human with id: ["+id+"] not found");
                return false;
            }
            String name = rs.getString("name");
            String birthday = rs.getString("birthday");
//            System.out.println("name: "+ name + ", birthday: " + birthday);
            return true;
        } catch (SQLException e) {
            return true;
        }
    }

    public void createNewHumans(String[] names, LocalDate[] birthdays) throws SQLException {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            LocalDate birthday = birthdays[i];
            insertSt.setString(1, name);
            insertSt.setString(2, birthday.toString());
            insertSt.addBatch();
        }
        insertSt.executeBatch();
    }

    public boolean createNawHuman(String name, LocalDate birthday) {
        try {
            insertSt.setString(1, name);
            insertSt.setString(2, birthday.toString());
            return insertSt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Long> getIds(){
        List<Long> result = new ArrayList<>();
        try(ResultSet rs = selectAllSt.executeQuery()) {
            while (rs.next()){
                result.add(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    // ========= Transaction
    public void rename(Map<String, String> renameMap) throws SQLException {
        conn.setAutoCommit(false);
        for (Map.Entry<String, String> keyVal : renameMap.entrySet()) {
            renameSt.setString(1, keyVal.getKey());
            renameSt.setString(2, keyVal.getKey());
            renameSt.addBatch();
        }
        try {
            renameSt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }finally {
            conn.setAutoCommit(true);
        }
    }
}
