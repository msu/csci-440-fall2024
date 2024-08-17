package edu.montana.csci.csci440.csvdb;

import edu.montana.csci.csci440.demo.PrintStudentClasses;

import java.io.*;
import java.util.*;

public class CSVDB {

    private static final String COMMA_DELIMITER = ",";

    static Map<String, List<Map<String, String>>> TABLES = new HashMap<>();

    public static void main(String[] args) {
        print("Welcome to CSVDB!\n");
        loadCSVs();
        var in = new Scanner(System.in);
        while (true) {
            print("(? for help) > ");
            String command = in.nextLine();
            if ("".equals(command) || "quit".equals(command)) {
                break;
            } else if ("help".equals(command) || "?".equals(command)) {
                print("Commands:\n  ? - show help\n  quit - quit\n  SELECT FROM <table_name> - show table\n\n");
            } else if (command.startsWith("SELECT FROM ")) {
                String table = command.substring("SELECT FROM ".length()).strip();
                List<Map<String, String>> rows = TABLES.get(table);
                if (rows == null) {
                    print("No table named: " + table + " found!\n\n");

                } else {
                    print("Table: " + table + "\n\n");
                    for (var row : rows) {
                        print(row + "\n");
                    }
                }
            }
        }
    }

    private static void print(String prompt) {
        System.out.print(prompt);
    }

    private static void loadCSVs() {
        print("Detecting tables...\n");
        File file = new File("./src/test/resources");
        for (File fileInWorkingDir : file.listFiles()) {
            String name = fileInWorkingDir.getName();
            if (name.endsWith(".csv")) {
                String tableName = name.split("\\.")[0];
                print("Loading table " + tableName + " from " + name + ".csv\n");
                TABLES.put(tableName, loadCSV(name));
            }
        }
        print("Tables loaded...\n\n");
    }

    private static List<Map<String, String>> loadCSV(String fileName) {
        List<Map<String, String>> records = new ArrayList<>();
        InputStream stream = PrintStudentClasses.class.getResourceAsStream("/" + fileName);
        InputStreamReader reader = new InputStreamReader(stream);
        try (BufferedReader br = new BufferedReader(reader)) {
            String line = br.readLine();
            String[] headers = line.split(COMMA_DELIMITER);
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    String key = headers[i];
                    map.put(key, value);
                }
                records.add(map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}
