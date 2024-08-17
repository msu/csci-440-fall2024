package edu.montana.csci.csci440.demo;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.*;

public class JDBC {

    public static void main(String[] args) throws Exception {

        joinExample();
    }

    private static void bestCloseExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";
        // create a connection to the database
        try(Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement()) {

            // let's move $10 from account 1 to account 2
            statement.execute("UPDATE account SET balance=30 WHERE id=1");

            conn.commit();
        }

    }
    private static void betterCloseExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";
        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        try {
            Statement statement = conn.createStatement();

            // let's move $10 from account 1 to account 2
            statement.execute("UPDATE account SET balance=30 WHERE id=1");

            conn.commit();
        } finally {
            conn.close();
        }

    }

    private static void badCloseExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";
        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        // let's move $10 from account 1 to account 2
        statement.execute("UPDATE account SET balance=30 WHERE id=1");

        conn.commit();

        conn.close();
    }

    private static void badTransactionExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";
        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        // let's move $10 from account 1 to account 2
        statement.execute("UPDATE account SET balance=30 WHERE id=1");

        statement.execute("UPDATE account SET balance=20 WHERE id=2");

        conn.commit();
    }
    private static void basicSelectExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";

        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        ResultSet resultSet =
                statement.executeQuery("SELECT * FROM tracks");

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("name"));
            System.out.println(resultSet.getInt("trackid"));
        }
    }
    private static void badColumnRetrieval() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";

        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        ResultSet resultSet =
                statement.executeQuery("SELECT * FROM tracks");

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("name"));
        }
    }

    private static void joinExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";

        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        ResultSet resultSet =
                statement.executeQuery(
                        "SELECT tracks.name, artists.name as ArtistName\n" +
                                "FROM tracks\n" +
                                "JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                                "JOIN artists on albums.ArtistId = artists.ArtistId");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("ArtistName") +
                    ": " +
                    resultSet.getString("name"));
        }
    }

    private static void groupByExample() throws SQLException {
        String url = "jdbc:sqlite:db/chinook.db";

        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        Statement statement = conn.createStatement();

        ResultSet resultSet =
                statement.executeQuery(
                        "SELECT artists.name, COUNT(*) as TrackCount\n" +
                                "FROM tracks\n" +
                                "JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                                "JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                                "GROUP BY artists.ArtistId");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("name") +
                    ": " +
                    resultSet.getString("TrackCount"));
        }
    }

    private static void groupByExample2() throws Exception {
        String url = "jdbc:sqlite:db/chinook.db";

        // create a connection to the database
        Connection conn = DriverManager.getConnection(url);

        System.out.print("Enter An Artist Name: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String artistName = reader.readLine().trim();

        String sql = "SELECT artists.name, COUNT(*) as TrackCount\n" +
                "FROM tracks\n" +
                "JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                "JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                "GROUP BY artists.ArtistId\n" +
                "HAVING artists.name LIKE ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,  "%" + artistName);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("name") +
                    ": " +
                    resultSet.getString("TrackCount"));
        }
    }
}
