package dk.igor.mytunes.gui.models;

import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.be.SongsInPlaylist;
import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.bll.PlaylistManager;
import dk.igor.mytunes.bll.SongsInPlaylistManager;
import dk.igor.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerModel {
    private final SongsInPlaylistManager songsInPlaylistManager;
    private final SongManager songManager;
    private final PlaylistManager playlistManager;

    private ObservableList<SongsInPlaylist> songsInPlaylistObservableList = FXCollections.observableArrayList();
    private ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    private ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private Map<Playlist, ObservableList<Song>> library;

    public PlayerModel() {
        songsInPlaylistManager = new SongsInPlaylistManager();
        songManager = new SongManager();
        playlistManager = new PlaylistManager();
        songsInPlaylistObservableList.setAll(songsInPlaylistManager.getAll());
        songObservableList.setAll(songManager.getAll());
        playlistObservableList.setAll(playlistManager.getAll());
        library = new HashMap<>();
    }


    public ObservableList<Playlist> getPlaylistObservableList() { return playlistObservableList;}
//    public ObservableList<SongsInPlaylist> getSongsInPlaylistObservableList() { return songsInPlaylistObservableList; }
    public ObservableList<Song> getSongObservableList() {
        return songObservableList;
    }

//    public void addSongsInPlaylist(SongsInPlaylist songsInPlaylist) {songsInPlaylistObservableList.add(songsInPlaylist);}

    public void addPlaylist(Playlist playlist) {
        playlistObservableList.add(playlist);
    }
    public void addSong(Song song) { songObservableList.add(song); }
    public List<Song> getSongsInPlaylist(Playlist playlist) {
        return songsInPlaylistManager.getAllSongsForPlaylist(playlist);
    }
}
