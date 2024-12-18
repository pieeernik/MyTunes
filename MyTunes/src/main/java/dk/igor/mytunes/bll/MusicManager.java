package dk.igor.mytunes.bll;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    private MediaPlayer mediaPlayer;

    public void playMusic(String filePath) {
        try {
            if (mediaPlayer != null) {
                if (filePath == null) {
                    mediaPlayer.play();
                    return;
                } else {
                    mediaPlayer.stop();
                }
            }

            if (filePath != null) {
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to play the media. File path: " + filePath, e);
        }
    }


    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        } else {
            throw new IllegalStateException("No media is currently playing to pause.");
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        } else {
            throw new IllegalStateException("No media is currently playing to stop.");
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isPaused() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }

}
