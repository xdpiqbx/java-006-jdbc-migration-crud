package cw.migcrud.feature.human;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HumanDaoService {
// @RequiredArgsConstructor
//    public HumanDaoService(Connection connewction) {
//        this.connewction = connewction;
//    }
    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;
    private PreparedStatement selectMaxIdSt;

    public HumanDaoService(Connection connection) throws SQLException {
        this.createSt = connection.prepareStatement(
            "INSERT INTO human (name, birthday, gender) VALUES (?, ?, ?)"
        );
        this.getByIdSt = connection.prepareStatement(
            "SELECT name, birthday, gender FROM human WHERE id = ?"
        );
        this.selectMaxIdSt = connection.prepareStatement(
            "SELECT MAX(id) AS maxId FROM human"
        );
    }

    public long create(Human human) throws SQLException {
        createSt.setString(1, human.getName());
        createSt.setString(2, human.getBirthday() == null ? null : human.getBirthday().toString());
        createSt.setString(3, human.getGender() == null ? null : human.getGender().name());
        createSt.executeUpdate();

        long id;
        try(ResultSet rs = selectMaxIdSt.executeQuery()){
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }

    public Human getById(long id) throws SQLException {
        getByIdSt.setLong(1, id);
        try(ResultSet rs = getByIdSt.executeQuery()){
            if(!rs.next()){
                return null;
            }
            Human result = new Human();
            result.setId(id);

            result.setName(rs.getString("name"));

            String birthday = rs.getString("birthday");
            if(birthday != null){
                result.setBirthday(LocalDate.parse(birthday));
            }

            String gender = rs.getString("gender");
            if(gender != null){
                result.setGender(Human.Gender.valueOf(gender));
            }
            return result;
        }
    }

    public List<Human> getAll(){
        return null;
    }
}
