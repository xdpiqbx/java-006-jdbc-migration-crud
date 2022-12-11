package cw.migcrud.feature.human;

import cw.migcrud.feature.storage.DatabaseInitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class HumanDaoServiceTest {
    private Connection connection;
    private HumanDaoService daoService;

    @BeforeEach
    public void beforeEach() throws SQLException {
        String connectionUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        DatabaseInitService.initDb(connectionUrl);
        connection = DriverManager.getConnection(connectionUrl);
        daoService = new HumanDaoService(connection);
    }
    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }
    
    // Can create valid with all fields
    @Test
    public void testThatHumanCreateCorrectly() throws SQLException {
        List<Human> originalHumans = new ArrayList<>();

        Human fullValueHuman = new Human();
        fullValueHuman.setName("TestName");
        fullValueHuman.setBirthday(LocalDate.now());
        fullValueHuman.setGender(Human.Gender.male);

        Human nullBirthdayHuman = new Human();
        nullBirthdayHuman.setName("TestName");
        nullBirthdayHuman.setBirthday(null);
        nullBirthdayHuman.setGender(Human.Gender.male);

        Human nullGenderHuman = new Human();
        nullGenderHuman.setName("TestName");
        nullGenderHuman.setBirthday(LocalDate.now());
        nullGenderHuman.setGender(null);

        originalHumans.add(fullValueHuman);
        originalHumans.add(nullBirthdayHuman);
        originalHumans.add(nullBirthdayHuman);

        // Assert
        for (Human originalHuman : originalHumans) {
            long id = daoService.create(originalHuman);
            Human saved = daoService.getById(id);

            Assertions.assertEquals(id, saved.getId());
            Assertions.assertEquals(originalHuman.getName(), saved.getName());
            Assertions.assertEquals(originalHuman.getBirthday(), saved.getBirthday());
            Assertions.assertEquals(originalHuman.getGender(), saved.getGender());
        }

    }

    @Test
    void getAll() throws SQLException {
        Human expected = new Human();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.now());
        expected.setGender(Human.Gender.male);

        long id = daoService.create(expected);
        expected.setId(id);

        List<Human> expectedHumans = Collections.singletonList(expected);
        List<Human> actualHumans = daoService.getAll();

        Assertions.assertEquals();
    }
}