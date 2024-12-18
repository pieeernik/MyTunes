package dk.igor.mytunes.be;


public class Playlist {
    private String name;
    private String song;
    private String time;
    private Integer id;


    public Playlist(String name, String song, String time) {
        this(name, song, time,null);
    }

    public Playlist(String name, String song, String time, Integer id) {
        this.name = name;
        this.song = song;
        this.time = time;
        this.id = id;
    }

    public String getName() { return name; }
    public String getSong() { return song; }
    public String getTime() { return time; }
    public int getId() { return id; }

    public int setId(int id) { this.id = id; return id;}
    public String setName(String name) { return this.name = name; }

    @Override
    public String toString() {
        return name;
    }
}



