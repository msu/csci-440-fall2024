package edu.montana.csci.csci440.homeworks.hwk2;

import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework2 extends DBTest {

    @Test
    /*
     * Write a query below that returns the employee who has been at the company the longest.
     * It should use a sub-query to achieve this.
     */
    void selectEmployeeWhoHasBeenAtTheCompanyTheLongest(){
        String query = """
          SELECT * FROM employees
          """;
        List<Map<String, Object>> results = exec(query);
        assertEquals(1, results.size());
        assertEquals("jane@chinookcorp.com", results.get(0).get("Email"));
    }

    @Test
    /*
     * Write a query in the string below that returns the person who has the most employees
     * reporting to them.  It should use a sub-query to achieve this (hint: GROUP BY + ORDER BY)
     */
    void selectEmployeeWhoHasMostReports(){
        String query = """
          SELECT * FROM employees WHERE EmployeeId=(SELECT ReportsTo FROM employees LIMIT 1)
          """;
        List<Map<String, Object>> results = exec(query);
        assertEquals(1, results.size());
        assertEquals("nancy@chinookcorp.com", results.get(0).get("Email"));
    }

    @Test
    /*
     * Write a query that returns the genres with a count of the number of sales (invoice items) they are
     * responsible for, from most to least.  The result should have three columns:
     *
     * - GenreId
     * - Name (the name of the Genre)
     * - Sales (the total number of invoice items associated with that genre)
     */
    void bestSellingGenres(){
        String query = """
          SELECT * FROM genres
          """;
        List<Map<String, Object>> results = exec(query);
        assertEquals(24, results.size());

        // most sold
        assertEquals("Rock", results.get(0).get("Name"));
        assertEquals(1, results.get(0).get("GenreId"));
        assertEquals(835, results.get(0).get("Sales"));
    }


}
