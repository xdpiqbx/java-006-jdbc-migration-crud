package cw.migcrud.feature.human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HumanDaoService {
// @RequiredArgsConstructor
//    public HumanDaoService(Connection connewction) {
//        this.connewction = connewction;
//    }
    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement getAllSt;
    private PreparedStatement updateSt;
    private PreparedStatement deleteByIdSt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement clearSt;
    private PreparedStatement searchSt;

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
        this.getAllSt = connection.prepareStatement(
            "SELECT id, name, birthday, gender FROM human"
        );
        this.updateSt = connection.prepareStatement(
            "UPDATE human SET name = ?, birthday = ?, gender = ? WHERE id = ?"
        );
        this.deleteByIdSt = connection.prepareStatement(
            "DELETE FROM human WHERE id = ?"
        );
        this.existsByIdSt = connection.prepareStatement(
            "SELECT COUNT(*) > 0 as humanExists FROM human WHERE id = ?"
        );
        this.clearSt = connection.prepareStatement(
            "DELETE FROM human"
        );
        this.searchSt = connection.prepareStatement(
            "SELECT id, name, birthday, gender FROM human WHERE name LIKE ?"
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
    public List<Human> getAll() throws SQLException {
        return getHumans(getAllSt);
    }
    public void update(Human human) throws SQLException {
        // "UPDATE human SET name = ?, birthday = ?, gender = ? WHERE id = ?"
        updateSt.setString(1, human.getName());
        updateSt.setString(2, human.getBirthday().toString());
        updateSt.setString(3, human.getGender().name());
        updateSt.setLong(4, human.getId());

        updateSt.executeUpdate();
    }

    public void deleteById(long id) throws SQLException {
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }

    public boolean exists(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try(ResultSet rs = existsByIdSt.executeQuery()){
            rs.next();
            return rs.getBoolean("humanExists");
        }
    }

    public long save(Human human) throws SQLException {
        if(exists(human.getId())){
            update(human);
            return human.getId();
        }
        return create(human);
    }

    public void clear() throws SQLException {
        clearSt.executeUpdate();
    }

    public List<Human> searchByName(String query) throws SQLException {
        searchSt.setString(1, "%"+query+"%");
        return getHumans(searchSt);
    }

    private List<Human> getHumans(PreparedStatement searchSt) throws SQLException {
        try(ResultSet rs = searchSt.executeQuery()){
            List<Human> result = new ArrayList<>();
            while(rs.next()){
                Human human = new Human();
                human.setId(rs.getLong("id"));
                human.setName(rs.getString("name"));
                String birthday = rs.getString("birthday");
                if(birthday != null){
                    human.setBirthday(LocalDate.parse(birthday));
                }
                String gender = rs.getString("gender");
                if(gender != null){
                    human.setGender(Human.Gender.valueOf(gender));
                }
                result.add(human);
            }
            return result;
        }
    }
}
