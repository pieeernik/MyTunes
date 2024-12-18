package dk.igor.mytunes.dal;

import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.be.SongsInPlaylist;
import dk.igor.mytunes.dal.db.DBConnection;
import dk.igor.mytunes.exceptions.PlayerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongInPlaylistDAODB {
    private DBConnection con = new DBConnection();

    public List<SongsInPlaylist> getAllSongsInPlaylist() throws PlayerException {
        List<SongsInPlaylist> songsInPlaylist = new ArrayList<>();
        try (Connection c = con.DBConnection()) {
            String sql = "SELECT * FROM songsInPlaylist";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idSong = rs.getInt("idSong");
                int idPlaylist = rs.getInt("idPlaylist");
                songsInPlaylist.add(new SongsInPlaylist(idSong, idPlaylist));
            }
        } catch (SQLException e) {
            throw new PlayerException(e);
        }
        return songsInPlaylist;
    }
    public List<Song> getAllSongsForPlaylist(Playlist playlist) throws PlayerException {
        List<Song> songs = new ArrayList<>();
        try (Connection c = con.DBConnection()) {
            String sql = "SELECT songs.id,songs.title, songs.artist, songs.time, songs.category, songs.filePath from songsInPlaylist join dbo.songs on id = songsInPlaylist.idSong WHERE songsInPlaylist.idPlaylist = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, playlist.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String time= rs.getString("time");
                String category = rs.getString("category");
                String filePath = rs.getString("filePath");

                songs.add(new Song(title,artist,time,category,filePath,id));
            }

            return songs;
        } catch (SQLException e) {
            throw new PlayerException(e);
        }
    }

    public void add(SongsInPlaylist songInPlaylist) throws SQLException {
        String sql = "INSERT INTO SongsInPlaylist (idSong, idPlaylist) VALUES (?, ?)";
        try (Connection c = con.DBConnection();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, songInPlaylist.getIdSong());
            pstmt.setInt(2, songInPlaylist.getIdPlaylist());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting song into playlist: " + e.getMessage(), e);
        }
    }

    public SongsInPlaylist delete(SongsInPlaylist songsInPlaylist) throws PlayerException {
        try (Connection c = con.DBConnection()) {
            c.setAutoCommit(false);
            String sql = "DELETE FROM songsInPlaylist WHERE idSong = ? AND idPlaylist = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, songsInPlaylist.getIdSong());
            stmt.setInt(2, songsInPlaylist.getIdPlaylist());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new PlayerException("No rows were deleted. Record may not exist.");
            }
            c.commit();
        } catch (SQLException e) {
            throw new PlayerException(e);
        }
        return songsInPlaylist;
    }
}

