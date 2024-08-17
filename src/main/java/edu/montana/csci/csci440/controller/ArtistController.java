package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Artist;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class ArtistController extends BaseController {
    public static void init() {
        /* CREATE */
        get("/artists/new", (req, resp) -> {
            Artist artist = new Artist();
            return renderTemplate("templates/artists/new.vm", "artist", artist);
        });

        post("/artists/new", (req, resp) -> {
            Artist artist = new Artist();
            artist.setName(req.queryParams("Name"));
            if (artist.create()) {
                Web.showMessage("Created An Artist!");
                return Web.redirect("/artists/" + artist.getArtistId());
            } else {
                Web.showErrorMessage("Could Not Create An Artist!");
                return renderTemplate("templates/artists/new.vm",
                        "artist", artist);
            }
        });

        /* READ */
        get("/artists", (req, resp) -> {
            List<Artist> artists = Artist.all(Web.getCurrentPage(), Web.PAGE_SIZE);
            return renderTemplate("templates/artists/index.vm",
                    "artists", artists);
        });

        get("/artists/:id", (req, resp) -> {
            Artist artist = Artist.find(asInt(req.params(":id")));
            return renderTemplate("templates/artists/show.vm",
                    "artist", artist);
        });

        /* UPDATE */
        get("/artists/:id/edit", (req, resp) -> {
            Artist artist = Artist.find(asInt(req.params(":id")));
            return renderTemplate("templates/artists/edit.vm",
                    "artist", artist);
        });

        post("/artists/:id", (req, resp) -> {
            Artist artist = Artist.find(asInt(req.params(":id")));
            artist.setName(req.queryParams("Name"));
            if (artist.update()) {
                Web.showMessage("Updated Artist!");
                return Web.redirect("/artists/" + artist.getArtistId());
            } else {
                Web.showErrorMessage("Could Not Update Artist!");
                return renderTemplate("templates/artists/edit.vm",
                        "artist", artist);
            }
        });

        /* DELETE */
        get("/artists/:id/delete", (req, resp) -> {
            Artist artist = Artist.find(asInt(req.params(":id")));
            artist.delete();
            Web.showMessage("Deleted Artist " + artist.getName());
            return Web.redirect("/artists");
        });
    }
}
