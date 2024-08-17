package edu.montana.csci.csci440.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintStudentClasses {
    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args) {

        long l = System.nanoTime();
        printStudentClassesFaster();
        System.out.println("=======================================================");
        System.out.println(" Printing took : " + (System.nanoTime() - l) + " ns");
        System.out.println("=======================================================");

    }

    private static void printStudentClasses() {
        List<Map<String, String>> students = loadCSV("students.csv");
        List<Map<String, String>> classes = loadCSV("classes.csv");
        List<Map<String, String>> student_classes = loadCSV("student_classes.csv");

        for (Map<String, String> student_class : student_classes) {
            String studentId = student_class.get("student");

            Map<String, String> matchingStudentInfo = null;
            for (Map<String, String> student : students) {
                if (student.get("id").equals(studentId)) {
                    matchingStudentInfo = student;
                    break;
                }
            }

            String classId = student_class.get("class");
            Map<String, String> matchingClassInfo = null;
            for (Map<String, String> clazz : classes) {
                if (clazz.get("id").equals(classId)) {
                    matchingClassInfo = clazz;
                    break;
                }
            }

            System.out.println("Student " + matchingStudentInfo.get("firstname") + " " + matchingStudentInfo.get("lastname") + " is taking " + matchingClassInfo.get("name"));
        }
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
















    private static void printStudentClassesFaster() {
        List<Map<String, String>> students = loadCSV("students.csv");

        List<Map<String, String>> classes = loadCSV("classes.csv");

        List<Map<String, String>> student_classes = loadCSV("student_classes.csv");

        Map<String, Map<String, String>> studentsById = new HashMap<>();
        for (Map<String, String> student : students) {
            studentsById.put(student.get("id"), student);
        }

        Map<String, Map<String, String>> classesById = new HashMap<>();
        for (Map<String, String> clazz : classes) {
            classesById.put(clazz.get("id"), clazz);
        }

        for (Map<String, String> student_class : student_classes) {
            String studentId = student_class.get("student");
            Map<String, String> matchingStudentInfo = studentsById.get(studentId);

            String classId = student_class.get("class");
            Map<String, String> matchingClassInfo = classesById.get(classId);
            System.out.println("Student " + matchingStudentInfo.get("firstname") + " " + matchingStudentInfo.get("lastname") + " is taking " + matchingClassInfo.get("name"));
        }
    }



}
