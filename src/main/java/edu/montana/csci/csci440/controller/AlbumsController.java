package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Album;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class AlbumsController extends BaseController {
    public static void init(){
        /* CREATE */
        get("/albums/new", (req, resp) -> {
            Album album = new Album();
            return renderTemplate("templates/albums/new.vm", "album", album);
        });

        post("/albums/new", (req, resp) -> {
            Album album = new Album();
            // TODO populate the album
            if (album.create()) {
                Web.showMessage("Created A Album!");
                return Web.redirect("/albums/" + album.getAlbumId());
            } else {
                Web.showErrorMessage("Could Not Create A Album!");
                return renderTemplate("templates/albums/new.vm",
                        "album", album);
            }
        });

        /* READ */
        get("/albums", (req, resp) -> {
            List<Album> albums = Album.all(Web.getCurrentPage(), Web.PAGE_SIZE);
            return renderTemplate("templates/albums/index.vm",
                    "albums", albums);
        });

        get("/albums/:id", (req, resp) -> {
            Album album = Album.find(asInt(req.params(":id")));
            return renderTemplate("templates/albums/show.vm", "album", album);
        });

        /* UPDATE */
        get("/albums/:id/edit", (req, resp) -> {
            Album album = Album.find(asInt(req.params(":id")));
            return renderTemplate("templates/albums/edit.vm", "album", album);
        });

        post("/albums/:id", (req, resp) -> {
            Album album = Album.find(asInt(req.params(":id")));

            // TODO update the album fields

            if (album.update()) {
                Web.showMessage("Updated Album!");
                return Web.redirect("/albums/" + album.getAlbumId());
            } else {
                Web.showErrorMessage("Could Not Update Album!");
                return renderTemplate("templates/albums/edit.vm",
                        "album", album);
            }
        });

        /* DELETE */
        get("/albums/:id/delete", (req, resp) -> {
            Album album = Album.find(asInt(req.params(":id")));
            album.delete();
            Web.showMessage("Deleted Album " + album.getTitle());
            return Web.redirect("/albums");
        });
    }
}
