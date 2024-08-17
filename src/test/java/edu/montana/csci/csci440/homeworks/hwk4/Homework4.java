package edu.montana.csci.csci440.homeworks.hwk4;

import edu.montana.csci.csci440.DBTest;
import edu.montana.csci.csci440.model.Track;
import edu.montana.csci.csci440.util.DB;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework4 extends DBTest {

    @Test
    /*
     * Use a transaction to safely move 10 milliseconds from one track to another.
     *
     * You will need to use the JDBC transaction API, outlined here:
     *
     *   https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html
     *
     */
    public void useATransactionToSafelyMoveMillisecondsFromOneTrackToAnother() throws SQLException {

        Track track1 = Track.find(1);
        Long track1InitialTime = track1.getMilliseconds();
        Track track2 = Track.find(2);
        Long track2InitialTime = track2.getMilliseconds();
        int milliseconds = 10;

        transferMilliseconds(track1, track2, milliseconds, false);

        // refresh tracks from db
        track1 = Track.find(1);
        track2 = Track.find(2);
        assertEquals(track1.getMilliseconds(), track1InitialTime - milliseconds);
        assertEquals(track2.getMilliseconds(), track2InitialTime + milliseconds);
    }

    private void transferMilliseconds(Track from, Track to, int milliseconds, boolean throwException) throws SQLException {
        try(Connection connection = DB.connect()){
            connection.setAutoCommit(false);

            PreparedStatement subtract = connection.prepareStatement("TODO");
            subtract.setLong(1, 0);
            subtract.setLong(2, 0);
            subtract.execute();

            if(throwException) {
                throw new RuntimeException();
            }

            PreparedStatement add = connection.prepareStatement("TODO");
            add.setLong(1, 0);
            add.setLong(2, 0);
            add.execute();

            // commit with the connection
        }
    }


    @Test
    /*
     * This calls the method above but forces an exception, which should cause the transaction to roll back, so
     * no changes should be made to the tracks
     */
    public void useATransactionToSafelyMoveMillisecondsFromOneTrackToAnotherWithException() throws SQLException {

        Track track1 = Track.find(1);
        Long track1InitialTime = track1.getMilliseconds();
        Track track2 = Track.find(2);
        Long track2InitialTime = track2.getMilliseconds();
        int milliseconds = 10;

        try {
            transferMilliseconds(track1, track2, milliseconds, true);
        } catch (Exception e) {
            // ignore
        }

        // refresh tracks from db
        track1 = Track.find(1);
        track2 = Track.find(2);
        assertEquals(track1.getMilliseconds(), track1InitialTime);
        assertEquals(track2.getMilliseconds(), track2InitialTime);
    }

    @Test
    /*
     * Return all invoices from the year 2011.   You can use the non-standard `stftime()` function in SQLite
     */
    public void allInvoicesIn2011() {
        String query = """
          SELECT * from invoices
          """;
        List<Map<String, Object>> results = exec(query);
        assertEquals(83, results.size());
    }


}
