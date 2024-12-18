package dk.igor.mytunes.bll;
import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.be.SongsInPlaylist;
import dk.igor.mytunes.dal.SongInPlaylistDAODB;

import java.util.List;

public class SongsInPlaylistManager {
    private SongInPlaylistDAODB songInPlaylistDAODB;

    public SongsInPlaylistManager() {
        songInPlaylistDAODB = new SongInPlaylistDAODB();
    }

    public List<SongsInPlaylist> getAll() {
        return songInPlaylistDAODB.getAllSongsInPlaylist();
    }


    public List<Song> getAllSongsForPlaylist(Playlist playlist) {
        return songInPlaylistDAODB.getAllSongsForPlaylist(playlist);
    }
}

