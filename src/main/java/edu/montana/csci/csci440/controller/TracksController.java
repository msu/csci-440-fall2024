package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Track;
import edu.montana.csci.csci440.util.Web;

import java.math.BigDecimal;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class TracksController extends BaseController {
    public static void init() {
        /* CREATE */
        get("/tracks/new", (req, resp) -> {
            Track track = new Track();
            return renderTemplate("templates/tracks/new.vm", "album", track);
        });

        post("/tracks/new", (req, resp) -> {
            Track track = new Track();

            // TODO update the track fields

            if (track.create()) {
                Web.showMessage("Created A Track!");
                return Web.redirect("/tracks/" + track.getTrackId());
            } else {
                Web.showErrorMessage("Could Not Create A Track!");
                return renderTemplate("templates/tracks/new.vm",
                        "track", track);
            }
        });

        /* READ */
        get("/tracks", (req, resp) -> {
            String search = req.queryParams("q");
            String orderBy = req.queryParams("o");
            List<Track> tracks;
            if (search != null) {
                tracks = Track.search(Web.getCurrentPage(), Web.PAGE_SIZE, orderBy, search);
            } else {
                tracks = Track.all(Web.getCurrentPage(), Web.PAGE_SIZE, orderBy);
            }
            // TODO - implement cache of count w/ Redis
            long totalTracks = Track.count();
            return renderTemplate("templates/tracks/index.vm",
                    "tracks", tracks, "totalTracks", totalTracks);
        });

        get("/tracks/search", (req, resp) -> {
            String search = req.queryParams("q");
            List<Track> tracks;
            tracks = Track.advancedSearch(Web.getCurrentPage(), Web.PAGE_SIZE,
                    search,
                    asInt(req.queryParams("ArtistId")),
                    asInt(req.queryParams("AlbumId")),
                    asInt(req.queryParams("max")),
                    asInt(req.queryParams("min")));
            return renderTemplate("templates/tracks/search.vm",
                    "tracks", tracks);
        });

        get("/tracks/:id", (req, resp) -> {
            Track track = Track.find(asInt(req.params(":id")));
            return renderTemplate("templates/tracks/show.vm",
                    "track", track);
        });

        /* UPDATE */
        get("/tracks/:id/edit", (req, resp) -> {
            Track track = Track.find(asInt(req.params(":id")));
            return renderTemplate("templates/tracks/edit.vm",
                    "track", track);
        });

        post("/tracks/:id", (req, resp) -> {
            Track track = Track.find(asInt(req.params(":id")));
            track.setName(req.queryParams("Name"));
            track.setMilliseconds(Long.parseLong(req.queryParams("Milliseconds")));
            track.setBytes(Long.parseLong(req.queryParams("Bytes")));
            track.setUnitPrice(new BigDecimal(req.queryParams("UnitPrice")));
            if (track.update()) {
                Web.showMessage("Updated Track!");
                return Web.redirect("/tracks/" + track.getTrackId());
            } else {
                Web.showErrorMessage("Could Not Update Track!");
                return renderTemplate("templates/tracks/edit.vm",
                        "track", track);
            }
        });

        /* DELETE */
        get("/tracks/:id/delete", (req, resp) -> {
            Track track = Track.find(asInt(req.params(":id")));
            track.delete();
            Web.showMessage("Deleted Track " + track.getName());
            return Web.redirect("/tracks");
        });
    }
}
