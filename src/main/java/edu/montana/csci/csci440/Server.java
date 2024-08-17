package edu.montana.csci.csci440;

import edu.montana.csci.csci440.controller.*;
import edu.montana.csci.csci440.util.Web;

import static edu.montana.csci.csci440.controller.BaseController.renderTemplate;
import static spark.Spark.*;

class Server {

    public static void main(String[] args) {

        /* ========================================================================= */
        /* Poor Mans Rails Implementation                                            */
        /* ========================================================================= */
        Web.init();

        /* ========================================================================= */
        /* Root Path                                                                 */
        /* ========================================================================= */
        get("/", (req, resp) -> {
            Web.showMessage("Welcome to ChinookApp!");
            return renderTemplate("templates/index.vm");
        });

        /* ========================================================================= */
        /* Music
        /* ========================================================================= */
        ArtistController.init();
        AlbumsController.init();
        TracksController.init();
        PlaylistsController.init();

        /* ========================================================================= */
        /* Business
        /* ========================================================================= */
        EmployeesController.init();
        CustomersController.init();
        InvoicesController.init();


    }

}