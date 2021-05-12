package model;

public class User {

    private String id;
    private String name;
    private Features stats;

    public User(String id, String name) {
        setId(id);
        this.name = name;
        this.stats = new Features();
    }

    public User(String id, String name, Integer popularity, Double tempo, Double valence, Double liveness,
            Double acousticness, Double danceability, Double energy, Double speechiness, Double instrumentalness) {
        setId(id);
        this.name = name;
        this.stats = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness,
                instrumentalness);
    }

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id.trim();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Features getStats() {
        return this.stats;
    }

    public void setStats(Features stats) {
        this.stats = stats;
    }
}
