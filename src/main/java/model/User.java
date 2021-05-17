package model;

import java.sql.Timestamp;

public class User {

    private String id;
    private String name;
    private String imageURL;
    private Timestamp lastUpdateDate;
    private Features stats;

    public User(String id, String name) {
        setId(id);
        this.name = name;
        this.stats = new Features();
    }

    public User(String id, String name, String imageURL) {
        setId(id);
        this.name = name;
        this.imageURL = imageURL;
        this.stats = new Features();
    }

    public User(String id, String name, String imageURL, Timestamp lastUpdateDate, Features stats) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.lastUpdateDate = lastUpdateDate;
        this.stats = stats;
    }

    public User(String id, String name, String imageURL, Timestamp lastUpdateDate, Integer popularity, Double tempo,
            Double valence, Double liveness, Double acousticness, Double danceability, Double energy,
            Double speechiness, Double instrumentalness) {
        setId(id);
        this.name = name;
        this.imageURL = imageURL;
        this.lastUpdateDate = lastUpdateDate;
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

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Timestamp getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Features getStats() {
        return this.stats;
    }

    public void setStats(Features stats) {
        this.stats = stats;
    }

}
