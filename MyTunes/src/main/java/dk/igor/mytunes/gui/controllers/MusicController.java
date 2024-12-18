package dk.igor.mytunes.gui.controllers;

import dk.igor.mytunes.be.Playlist;
import dk.igor.mytunes.be.Song;
import dk.igor.mytunes.be.SongsInPlaylist;
import dk.igor.mytunes.bll.MusicManager;
import dk.igor.mytunes.dal.PlaylistDAODB;
import dk.igor.mytunes.dal.SongDAODB;
import dk.igor.mytunes.dal.SongInPlaylistDAODB;
import dk.igor.mytunes.gui.models.PlayerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicController implements Initializable {
    private final PlayerModel playerModel = new PlayerModel();
    private final MusicManager musicManager = new MusicManager();
    private FilteredList<Song> filteredSongs;

    @FXML
    public ListView<Song> songsInPlaylistListView;

    @FXML
    public ListView<Song> songListView;

    @FXML
    public ListView<Playlist> playlistListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        songListView.setItems(playerModel.getSongObservableList());
        playlistListView.setItems(playerModel.getPlaylistObservableList());
        filteredSongs = new FilteredList<>(playerModel.getSongObservableList(), p -> true);
        songListView.setItems(filteredSongs);

        playlistListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                ObservableList<Song> songs = FXCollections.observableArrayList();
                songs.setAll(playerModel.getSongsInPlaylist(playlistListView.getSelectionModel().getSelectedItem()));
                songsInPlaylistListView.setItems(songs);
            }
        });
    }

    @FXML
    private Label currentlyPlayingLabel;

    @FXML
    public void playSong(Song song) {
        String songName = song.getTitle();
        currentlyPlayingLabel.setText(songName + " ... Is Playing");
    }

    @FXML
    private void onClickPlayPauseMusic() {}

    @FXML
    private void onClickSkipMusic() {}

    @FXML
    private void onClickPlayPreviousSong() {}

    @FXML
    private TextField filterField;

    @FXML
    private void onFilterButtonClick() {
        String filterText = filterField.getText().toLowerCase().trim();

        filteredSongs.setPredicate(song -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            return song.getTitle().toLowerCase().contains(filterText) ||
                    song.getArtist().toLowerCase().contains(filterText) ||
                    song.getCategory().toLowerCase().contains(filterText);
        });
        songListView.refresh();
    }

    @FXML
    private void onNewPlaylistButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Playlist");
        dialog.setHeaderText("Create a New Playlist");
        dialog.setContentText("Enter playlist name:");

        dialog.showAndWait().ifPresent(playlistName -> {
            if (!playlistName.trim().isEmpty()) {
                Playlist newPlaylist = new Playlist(playlistName, "", "", 0);

                try {
                    PlaylistDAODB playlistDAODB = new PlaylistDAODB();
                    playlistDAODB.add(newPlaylist);
                    playerModel.addPlaylist(newPlaylist);
                } catch (Exception e) {
                    showError("Error saving playlist: " + e.getMessage());
                }

            } else {
                showError("Playlist name cannot be empty!");
            }
        });
    }

    @FXML
    private void onEditPlaylistButtonClick() {
        Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();

        if (selectedPlaylist == null) {
            showError("No playlist selected to edit!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedPlaylist.getName());
        dialog.setTitle("Edit Playlist");
        dialog.setHeaderText("Edit Playlist Name");
        dialog.setContentText("Enter new name:");


        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.trim().isEmpty()) {

                try {
                    selectedPlaylist.setName(newName);
                    PlaylistDAODB playlistDAODB = new PlaylistDAODB();
                    playlistDAODB.update(selectedPlaylist);
                    playlistListView.refresh();


                } catch (Exception e) {
                    showError("Error updating playlist: " + e.getMessage());
                }

            } else {
                showError("Playlist name cannot be empty!");
            }
        });
    }

    @FXML
    private void onDeletePlaylistButtonClick() {
        Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            showError("No playlist selected to delete!");
            return;
        }
        try {
            PlaylistDAODB playlistDAODB = new PlaylistDAODB();
            playlistDAODB.delete(selectedPlaylist);
            playerModel.getPlaylistObservableList().remove(selectedPlaylist);
            playlistListView.refresh();
            playlistListView.getSelectionModel().clearSelection();
            songsInPlaylistListView.refresh();
        } catch (Exception e) {
            showError("Error deleting playlist: " + e.getMessage());
        }

    }
    @FXML
    private void onNewSongButtonClick() {
        Dialog<Song> dialog = new Dialog<>();
        dialog.setTitle("New Song");
        dialog.setHeaderText("Add a New Song");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField artistField = new TextField();
        artistField.setPromptText("Artist");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Pop", "Rock", "Jazz", "Classical", "Hip-Hop", "Rap", "Electronic", "Country", "R&B", "Blues", "Metal", "Reggae");
        categoryComboBox.setPromptText("Select Category");
        TextField timeField = new TextField();
        timeField.setPromptText("Time (e.g., 3:45)");
        Label fileLabel = new Label("No file selected");
        Button fileChooserButton = new Button("Choose MP3...");

        final String[] selectedFilePath = {null};
        fileChooserButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            fileChooser.setTitle("Select MP3 File");
            java.io.File file = fileChooser.showOpenDialog(fileChooserButton.getScene().getWindow());
            if (file != null) {
                selectedFilePath[0] = file.getAbsolutePath();
                fileLabel.setText(file.getName());
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Artist:"), 0, 1);
        grid.add(artistField, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);

        grid.add(new Label("Time:"), 0, 3);
        grid.add(timeField, 1, 3);

        grid.add(fileChooserButton, 0, 4);
        grid.add(fileLabel, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String title = titleField.getText().trim();
                String artist = artistField.getText().trim();
                String category = categoryComboBox.getValue();
                String time = timeField.getText().trim();
                String filePath = selectedFilePath[0];

                if (title.isEmpty() || artist.isEmpty() || category == null || time.isEmpty() || filePath == null) {
                    showError("All fields must be filled, and a file must be selected.");
                    return null;
                }

                return new Song(title, artist, category, time, filePath);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(song -> {
            try {
                SongDAODB songDAODB = new SongDAODB();
                songDAODB.add(song);
                playerModel.addSong(song);
            } catch (Exception e) {
                showError("Error saving song: " + e.getMessage());
            }
        });
    }


    @FXML
    private void onEditSongButtonClick() {
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            showError("No song selected to edit!");
            return;
        }

        Dialog<Song> dialog = new Dialog<>();
        dialog.setTitle("Edit Song");
        dialog.setHeaderText("Edit Song Details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(selectedSong.getTitle());
        TextField artistField = new TextField(selectedSong.getArtist());
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Pop", "Rock", "Jazz", "Classical", "Hip-Hop", "Rap", "Electronic", "Country", "R&B", "Blues", "Metal", "Reggae");
        categoryComboBox.setValue(selectedSong.getCategory());
        TextField filePathField = new TextField(selectedSong.getFilePath());
        TextField timeField = new TextField(selectedSong.getTime());

        gridPane.add(new Label("Title:"), 0, 0);
        gridPane.add(titleField, 1, 0);
        gridPane.add(new Label("Artist:"), 0, 1);
        gridPane.add(artistField, 1, 1);
        gridPane.add(new Label("Category:"), 0, 2);
        gridPane.add(categoryComboBox, 1, 2);
        gridPane.add(new Label("File Path:"), 0, 3);
        gridPane.add(filePathField, 1, 3);
        gridPane.add(new Label("Time:"), 0, 4);
        gridPane.add(timeField, 1, 4);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String title = titleField.getText().trim();
                String artist = artistField.getText().trim();
                String category = categoryComboBox.getValue().trim();
                String time = timeField.getText().trim();
                String filePath = filePathField.getText().trim();


                if (title.isEmpty() || artist.isEmpty() || category.isEmpty() || filePath.isEmpty() || time.isEmpty()) {
                    showError("Invalid input! Please fill out all fields.");
                    return null;
                }

                return new Song(title, artist, category, time, filePath, selectedSong.getId());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(editedSong -> {

            try {
                SongDAODB songDAODB = new SongDAODB();
                songDAODB.update(editedSong);

                int index = songListView.getItems().indexOf(selectedSong);
                if (index >= 0) {
                    songListView.getItems().set(index, editedSong);
                }

                songListView.refresh();
            } catch (Exception e) {
                showError("Error updating the song: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onDeleteSongButtonClick() {
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();
        System.out.println(songListView.getSelectionModel().getSelectedItem());
        if (selectedSong == null) {
            showError("No song selected to delete!");
            return;
        }
        try {
            SongDAODB songDAODB = new SongDAODB();
            songDAODB.delete(selectedSong);
            playerModel.getSongObservableList().remove(selectedSong);
            songListView.refresh();
        } catch (Exception e) {
            showError("Error deleting song: " + e.getMessage());
        }
    }

    @FXML
    private void onAddSongToPlaylistButtonClick() {
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            showError("No song selected to add to the playlist!");
            return;
        }
        if (selectedPlaylist == null) {
            showError("No playlist selected!");
            return;
        }

        try {
            boolean songAlreadyInPlaylist = playerModel.getSongsInPlaylist(selectedPlaylist).stream()
                    .anyMatch(song -> song.getId() == selectedSong.getId());

            if (songAlreadyInPlaylist) {
                showError("This song is already in the selected playlist!");
                return;
            }

            SongsInPlaylist newEntry = new SongsInPlaylist(selectedSong.getId(), selectedPlaylist.getId());
            SongInPlaylistDAODB songInPlaylistDAODB = new SongInPlaylistDAODB();
            songInPlaylistDAODB.add(newEntry);

            ObservableList<Song> updatedSongs = FXCollections.observableArrayList();
            updatedSongs.setAll(playerModel.getSongsInPlaylist(selectedPlaylist));
            songsInPlaylistListView.setItems(updatedSongs);
            songsInPlaylistListView.refresh();

            showSuccess("Song added to playlist successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error adding song to playlist: " + e.getMessage());
        }
    }



    @FXML
    private void onDeleteSongsInPlaylistButtonClick() {
        Song selectedSong = songsInPlaylistListView.getSelectionModel().getSelectedItem();
        if (selectedSong == null) {
            showError("No song selected to delete from the playlist!");
            return;
        }
        try {
            Playlist currentPlaylist = playlistListView.getSelectionModel().getSelectedItem();
            if (currentPlaylist == null) {
                showError("No playlist selected!");
                return;
            }

            SongInPlaylistDAODB songInPlaylistDAODB = new SongInPlaylistDAODB();
            SongsInPlaylist songsInPlaylistToDelete = new SongsInPlaylist(selectedSong.getId(), currentPlaylist.getId());
            songInPlaylistDAODB.delete(songsInPlaylistToDelete);
            songsInPlaylistListView.getItems().remove(selectedSong);
            songsInPlaylistListView.refresh();

            showSuccess("Song deleted from the playlist successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error deleting song from playlist: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}


