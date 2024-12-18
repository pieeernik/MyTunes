package dk.igor.mytunes.bll;

import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.dal.SongDAODB;

import java.util.List;

public class SongManager {
    private SongDAODB songDAODB;

//    public SongManager(SongDAODB songDAODB) {
//        this.songDAODB = songDAODB;
//    }

    public SongManager() {
        this.songDAODB = new SongDAODB();
    }

    public List<Song> getAll() {
        try {
            return songDAODB.getAllSongs();
        } catch (Exception e) {
            System.err.println("Error fetching songs: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

