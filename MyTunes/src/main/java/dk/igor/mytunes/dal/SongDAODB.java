package dk.igor.mytunes.dal;

import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.dal.db.DBConnection;
import dk.igor.mytunes.exceptions.PlayerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAODB {
    private DBConnection con = new DBConnection();

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try (Connection c = con.DBConnection()) {
            String sql = "SELECT * FROM songs";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                String time = rs.getString("time");
                String filePath = rs.getString("filePath");
                int id = rs.getInt("id");

                songs.add(new Song(title, artist, category, time, filePath, id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching playlists", e);
        }
        return songs;
    }

    public Song add(Song song) throws PlayerException {
        String sql = "INSERT INTO songs (title, artist, category, time, filePath) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = con.DBConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setString(4, song.getTime());
            stmt.setString(5, song.getFilePath());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    song.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        } catch (SQLException e) {
            throw new PlayerException(e);
        }
        return song;
    }


    public Song update(Song song) throws PlayerException {
        try (Connection c = con.DBConnection()) {
            String sql = "UPDATE songs SET title = ?, artist = ?, category = ?, time = ?, filePath = ? WHERE id = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setString(4, song.getTime());
            stmt.setString(5, song.getFilePath());
            stmt.setInt(6, song.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating song: " + e.getMessage(), e);
        }
        return song;
    }


    public Song delete(Song song) throws PlayerException {
        try {
            Connection c = con.DBConnection();
            String sql = "DELETE FROM songs WHERE id = ?";
            System.out.println(song.getId());
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, song.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return song;
    }

}


