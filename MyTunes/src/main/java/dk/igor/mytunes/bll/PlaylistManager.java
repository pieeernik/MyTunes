package dk.igor.mytunes.bll;

import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.dal.PlaylistDAODB;
import java.util.List;


public class PlaylistManager {
    private PlaylistDAODB playlistDAODB;

    public PlaylistManager() {
        playlistDAODB = new PlaylistDAODB();
    }

    public List<Playlist> getAll() {
        return playlistDAODB.getAllPlaylists();
    }
}



