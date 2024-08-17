package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Playlist;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class PlaylistsController extends BaseController {
    public static void init() {
        /* READ */
        get("/playlists", (req, resp) -> {
            List<Playlist> playlists = Playlist.all(Web.getCurrentPage(), Web.PAGE_SIZE);
            return renderTemplate("templates/playlists/index.vm",
                    "playlists", playlists);
        });

        get("/playlists/:id", (req, resp) -> {
            Playlist playlist = Playlist.find(asInt(req.params(":id")));
            return renderTemplate("templates/playlists/show.vm",
                    "playlist", playlist);
        });
    }
}
