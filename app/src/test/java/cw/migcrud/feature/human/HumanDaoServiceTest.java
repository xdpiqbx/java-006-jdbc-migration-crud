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
        daoService.clear();
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

        Assertions.assertEquals(expectedHumans, actualHumans);
    }

    @Test
    void update() throws SQLException {
        Human original = new Human();
        original.setName("TestName");
        original.setBirthday(LocalDate.now());
        original.setGender(Human.Gender.male);

        long id = daoService.create(original);
        original.setId(id);

        original.setName("New Name");
        original.setBirthday(LocalDate.now().plusDays(1));
        original.setGender(Human.Gender.female);
        daoService.update(original);

        Human updated = daoService.getById(id);
        Assertions.assertEquals(id, updated.getId());
        Assertions.assertEquals("New Name", updated.getName());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updated.getBirthday());
        Assertions.assertEquals(Human.Gender.female, updated.getGender());
    }

    @Test
    void deleteById() throws SQLException {
        Human original = new Human();
        original.setName("TestName");
        original.setBirthday(LocalDate.now());
        original.setGender(Human.Gender.male);

        long id = daoService.create(original);
        original.setId(id);

        daoService.deleteById(id);

        Assertions.assertNull(daoService.getById(id));
    }

    @Test
    void exists() throws SQLException {
        Human original = new Human();
        original.setName("TestName");
        original.setBirthday(LocalDate.now());
        original.setGender(Human.Gender.male);

        long id = daoService.create(original);
        original.setId(id);

        Assertions.assertTrue(daoService.exists(id));
        Assertions.assertFalse(daoService.exists(-1));
    }

    @Test
    void save() throws SQLException {
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);

        Assertions.assertTrue(daoService.exists(id));
    }

    @Test
    public void saveOnExistingUser() throws SQLException{
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);
        newHuman.setId(id);

        newHuman.setName("New Name");
        daoService.save(newHuman);

        Human updated = daoService.getById(id);
        Assertions.assertEquals("New Name", updated.getName());
    }

    @Test
    void searchOnEmpty() throws SQLException {
        Assertions.assertEquals(
            Collections.emptyList(),
            daoService.searchByName("name")
        );
    }
    @Test
    void searchOnFilledDB() throws SQLException {
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);

        List<Human> actual = daoService.searchByName("Test");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(id, actual.get(0).getId());
    }
}