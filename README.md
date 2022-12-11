# java-006-jdbc-migration-crud

---

## Міграції. Фреймворк Flyway

[Flyway Core (maven)](https://mvnrepository.com/artifact/org.flywaydb/flyway-core)

[Flyway Documentation](https://flywaydb.org/documentation/)

```java
public class DatabaseInitService {
    public void initDb(Storage storage){
        String connectionUrl = new Prefs().getString(Prefs.DB_URL);
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, null, null)
                .load();
        // Start the migration
        flyway.migrate();
    }
}
```

---

DAO - Data Access Object
DTO - Data Transfer Object