package dk.igor.mytunes.dal;

import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.dal.db.DBConnection;
import dk.igor.mytunes.exceptions.PlayerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAODB {
    private DBConnection con = new DBConnection();

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection c = con.DBConnection()) {
            String sql = "SELECT * FROM playlists";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String song = rs.getString("song");
                String time = rs.getString("time");
                int id = rs.getInt("id");
                playlists.add(new Playlist(name, song, time, id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching playlists", e);
        }
        return playlists;
    }

    public Playlist add(Playlist playlist) throws PlayerException {
            String sql = "INSERT INTO playlists (name) VALUES (?)";
        try (Connection c = con.DBConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, playlist.getName());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    playlist.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        } catch (SQLException e) {
            throw new PlayerException(e);
        }
        return playlist;
    }


        public Playlist update (Playlist playlist) throws PlayerException {
        try {
            Connection c = con.DBConnection();
            String sql = "UPDATE playlists SET name = ? WHERE id = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playlist;
        }

        public Playlist delete (Playlist playlist) throws PlayerException {
        try {
            Connection c = con.DBConnection();
            String sql = "DELETE FROM playlists WHERE id = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, playlist.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playlist;
        }
}
