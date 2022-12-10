package cw.migcrud.feature.prefs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Prefs {
    public static final String DEFAULT_PREFS_FILENAME = "prefs.json";
    public static final String DB_URL = "dbUrl";
    public static final String INIT_DB_SQL_FILE_PATH = "initDbSql";
    private Map<String, Object> prefs = new HashMap<>();
    public Prefs(){
        this(DEFAULT_PREFS_FILENAME);
    }
    public Prefs(String filename){
        try {
            String json = String.join("\n", Files.readAllLines(Paths.get(filename)));
            //TODO handle correct type
            Type typeToken = TypeToken.getParameterized(Map.class, String.class, Object.class).getType();
            prefs = new Gson().fromJson(json, typeToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key){
        return getPref(key).toString();
    }

    public Object getPref(String key){
        return prefs.get(key);
    }

    public static void main(String[] args) {
        Prefs prefs = new Prefs();
        System.out.println(prefs.getString(Prefs.DB_URL));
    }
}
