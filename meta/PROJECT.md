# ChinookApp

You will be implementing a web application against a SQL store, found in `/db/chinook.db`

The web application will be an open-ended project, but must include the following:

* All entities in the chinookdb must be surfaced in the web application
* The Employee entity must have all CRUD operations surfaced
* A tree UI of the Employee hierarchy
* Track search with at least two inputs must be implemented
* At least one report-like page must exist that makes use of a `GROUP BY` clause
* Implement at least one Redis cache

## Project Rubric

The project will be largely graded via automated tests.  The grading will be broken down along the following lines:

* 75% - The automated test suite (if the test suite passes, you are guaranteed to get a C on the project)
* 15% - A recorded demo, demonstrating the following functionality
   * Navigating the core entities in the database
   * CRUD operations on the Employee entity
   * Track search
   * Paging implemented in the Track main view
   * A Group By based report page
* 10% - Code quality and satisfaction of the Redis cache, determined by manual inspection

## Demo Script

Below is the script and expected behavior of the web application.  You are expected to record a video and upload it to 
YouTube or  another alternative.  If you do not have any screen recording experience, [OBS](https://obsproject.com/) is 
a cross-platform screen recording software package that can be used.

### Steps

1. Artists
    1. Demonstrate paging on `/artist` (navigate to page 2 and back)
    1. Create a new artist called `Robert Parker`
2. Albums
    1. Demonstrate paging on `/albums`
    1. Create a new album named `Crystal City` with `Robert Parker` set as the artist
    1. Delete the album
3. Tracks
    1. Demonstrate paging on `/tracks` (navigate to page 2 and back)
    2. Demonstrate sorting
    3. Demonstrate search (and possibly active search for extra credit)
    4. Demonstrate advanced search with track name 'A' and artists "AC/DC"
    5. Create a new track named `Example` with `Supernatural` set as the album, and random values for other fields
    6. Delete the track
4. Employees
    1. Demonstrate the employee tree
    2. Demonstrate the sales report
5. Customers
   1. Demonstrate paging on `/customers` (navigate to page 2 and back)
   1. Demonstrate invoices on `/customers/1`
6. Invoices
   1. Demonstrate paging on `/invoices` (navigate to page 2 and back)
   1. Demonstrate invoice items on `/invoices/1` (should include track name, album name, artist name)
