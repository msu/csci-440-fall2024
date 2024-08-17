package edu.montana.csci.csci440.demo;

import spark.Spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LemonadeApp {
    public static void main(String[] args) {
        Spark.get("/", (request, response) -> {
            response.header("Content-Type", "text/text");
            try(Connection connection = DriverManager.getConnection("jdbc:sqlite:db/lemonade.db");
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM Stand")) {
                ResultSet resultSet = ps.executeQuery();
                StringBuilder sb = new StringBuilder("LEMONADE STANDS:\n\n");
                while (resultSet.next()) {
                    sb.append("\t - ")
                            .append(resultSet.getString("Name"))
                            .append(": ")
                            .append(resultSet.getString("Location"))
                            .append("\n");
                }

                return sb.toString();
            }
        });
    }
}
