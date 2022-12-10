package cw.migcrud;

import cw.migcrud.feature.human.HumanGenerator;
import cw.migcrud.feature.human.HumanServiceV2;
import cw.migcrud.feature.storage.DatabaseInitService;
import cw.migcrud.feature.storage.Storage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws SQLException {

        Storage storage = Storage.getInstance();
        new DatabaseInitService().initDb(storage);
        HumanServiceV2 humanServiceV2 = new HumanServiceV2(storage);
//
        int count = 100000;
//
        HumanGenerator generator = new HumanGenerator();
        String[] names = generator.generateNames(count);
        LocalDate[] dates = generator.generateDates(count);
//
//        humanServiceV2.createNewHumans(names, dates);

//        Map<String, String> renameMap = new HashMap<>();
//        renameMap.put("John", "Ivan");
//        renameMap.put("Jennifer", "Olga");
//        renameMap.put("Bill", "Bogdan");

//
//        int chunkSize = 10;
//        String[][] nameChunks = splitArr(names, chunkSize);
//        LocalDate[][] datesChunks = splitArr(dates, chunkSize);
//
//        long start = System.currentTimeMillis();


//        for (int i = 0; i < nameChunks.length; i++) {
//            final String[] nameChunk = nameChunks[i];
//            final LocalDate[] datesChunk = datesChunks[i];
//            humanServiceV2.createNewHumans(nameChunk, datesChunk);
//        }

        for (int i = 0; i < count; i++) {
            humanServiceV2.createNawHuman(names[i], dates[i]);
        }

//        long duration = System.currentTimeMillis() - start;
//        System.out.println("duration = " + duration);

//        HumanServiceV1 humanServiceV1 = new HumanServiceV1(storage);
//        HumanServiceV2 humanServiceV2 = new HumanServiceV2(storage);

//        for (int i = 0; i < 100000; i++) {
//            humanServiceV2.printHumanInfo(1);
//            humanServiceV1.printHumanInfo(1);
//        }

//        boolean res = humanServiceV2.createNawHuman("Bogun", LocalDate.now().minusYears(40));
//        System.out.println("res = " + res);

//        List<Long> ids = humanServiceV2.getIds();
//        ids.forEach(it -> humanServiceV2.printHumanInfo(it));

//        HumanServiceV1 humanService = new HumanServiceV1(storage);
//        humanService.createNewHuman("Valerii", LocalDate.now());
//        humanService.createNewHuman("Volodymir", LocalDate.now());
//        humanService.createNewHuman("Ares", LocalDate.now());
//        humanService.printHumanInfo(3);
//        humanService.printHumanIds();



//        String insertSQL = String.format(
//                "INSERT INTO human (name, birthday) VALUES ('%s', '%s')",
//                "Musk", LocalDate.now()
//        );

//        storage.executeUpdate(insertSQL);
//
//        String selectSQL = "SELECT id, name, birthday FROM human WHERE id = 1";
//        Statement st = storage.getConnection().createStatement();
//        ResultSet rs = st.executeQuery(selectSQL);
//        if(rs.next()){
//            long id = rs.getLong("id");
//            String name = rs.getString("name");
//            LocalDate birthday = LocalDate.parse(rs.getString("birthday"));
//            System.out.println("id = " + id);
//            System.out.println("name = " + name);
//            System.out.println("birthday = " + birthday);
//        }else{
//            System.out.println("Human id not found");
//        }
//        rs.close();
//        st.close();
    }
    static String[][] splitArr(String[] arr, int chunkSize){
        int chunkCount = arr.length / chunkSize;
        String[][] result = new String[chunkCount][chunkSize];
        int index = 0;
        for (String[] currentChunk : result) {
            for (int j = 0; j < chunkSize; j++) {
                currentChunk[j] = arr[index++];
            }
        }
        return result;
    }
    static LocalDate[][] splitArr(LocalDate[] arr, int chunkSize){
        int chunkCount = arr.length / chunkSize;
        LocalDate[][] result = new LocalDate[chunkCount][chunkSize];
        int index = 0;
        for (LocalDate[] currentChunk : result) {
            for (int j = 0; j < chunkSize; j++) {
                currentChunk[j] = arr[index++];
            }
        }
        return result;
    }
}
