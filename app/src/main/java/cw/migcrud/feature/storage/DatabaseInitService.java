package cw.migcrud.feature.storage;

import cw.migcrud.feature.prefs.Prefs;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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


//    public void initDb(Storage storage){
//        String initDbFilename = new Prefs().getString(Prefs.INIT_DB_SQL_FILE_PATH);
//        try {
//            String sql = String.join("\n", Files.readAllLines(Paths.get(initDbFilename)));
//            storage.executeUpdate(sql);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }