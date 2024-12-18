package dk.igor.mytunes.be;

public class SongsInPlaylist {
    private int idSong;
    private int idPlaylist;

    public SongsInPlaylist(int idSong, int idPlaylist) {
        this.idSong = idSong;
        this.idPlaylist = idPlaylist;
    }

    public int getIdSong() {
        return idSong;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }


    @Override
    public String toString() {
        return "SongsInPlaylist{" +
                "idSong=" + idSong +
                ", idPlaylist=" + idPlaylist +
                '}';
    }
}
