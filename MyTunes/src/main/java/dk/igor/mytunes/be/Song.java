package dk.igor.mytunes.be;

public class Song {
    public String title;
    public String artist;
    public String category;
    public String filePath;
    public String time;
    private Integer id;


    public Song(String title, String artist, String category, String time, String filePath) {
        this(title, artist, category, time, filePath, null);
    }

    public Song(String title, String artist, String category, String time, String filePath, Integer id) {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.filePath = filePath;
        this.id = id;
    }

    public String getTitle() { return title;}
    public String getArtist() { return artist;}
    public String getTime() { return time;}
    public String getCategory() { return category; }
    public String getFilePath() { return filePath;}
    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return "Title = " + title + ", Artist = " + artist + ", Category = " + category + ", FilePath = " + filePath + ", Time = " + time;
    }
}

