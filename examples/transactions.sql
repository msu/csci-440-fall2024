
INSERT INTO tracks
    (Name, AlbumId, MediaTypeId, GenreId,
     Composer, Milliseconds, Bytes, UnitPrice)
    VALUES
    ("Demo", 1, 1, 1, NULL, 2000, 300, .99)

UPDATE tracks
SET Bytes=(Bytes - 10)
WHERE TrackId = 1;

UPDATE tracks
SET Bytes=(Bytes + 10)
WHERE TrackId = 2;

ALTER TABLE albums ADD COLUMN Bytes INTEGER;

BEGIN TRANSACTION;

UPDATE albums
SET Bytes=(SELECT SUM(Bytes) FROM tracks WHERE tracks.AlbumId = 1)
WHERE AlbumId = 1;

COMMIT;

UPDATE tracks
SET Bytes=(Bytes + 10)
WHERE TrackId = 2;

COMMIT;



UPDATE artists
SET Name="DC/AC", Name=2
WHERE Name="AC/DC" AND ArtistId=1;


UPDATE artists
SET Name="DC/AC", Version=2
WHERE Version=1 AND ArtistId=1;

