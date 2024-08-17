package edu.montana.csci.csci440;

import edu.montana.csci.csci440.model.Track;
import edu.montana.csci.csci440.util.DB;
import org.junit.jupiter.api.AfterEach;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DBTest {
    @AfterEach
    public void resetDB() {
        try {
            log("Resetting DB After Test...");
            DB.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Jedis jedis = new Jedis();
            jedis.del(Track.REDIS_CACHE_KEY);
        } catch (Exception e) {
            log("No redis found to reset");
        }
    }

    private static void log(String x) {
        System.out.println(x);
    }

    public boolean executeDDL(String ddl) {
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement()) {
            return stmt.execute(ddl);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public List<Map<String, Object>> exec(String sqlString) {
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet results = stmt.executeQuery(sqlString);
            List<Map<String, Object>> resultList = new LinkedList<>();
            while (results.next()) {
                ResultSetMetaData metaData = results.getMetaData();
                int columns = metaData.getColumnCount();
                Map<String, Object> hashMap = new HashMap<>();
                for (int i = 0; i < columns; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    hashMap.put(columnName, results.getObject(i + 1));
                }
                resultList.add(hashMap);
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public boolean executeUpdate(String sqlString) {
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement()) {
            return stmt.execute(sqlString);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
