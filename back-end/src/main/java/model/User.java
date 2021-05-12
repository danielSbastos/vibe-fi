package model;

public class User {

    private String id;
    private String name;
    private Features stats;


    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.stats = new Features();
    }
    
    
    public User(String id, String name, Integer popularity, Double tempo, Double valence, Double liveness,
            Double acousticness, Double danceability, Double energy, Double speechiness, Double instrumentalness) {
        this.id = id;
        this.name = name;
        this.stats.popularity = popularity;
        this.stats.tempo = tempo;
        this.stats.valence = valence;
        this.stats.liveness = liveness;
        this.stats.acousticness = acousticness;
        this.stats.danceability = danceability;
        this.stats.energy = energy;
        this.stats.speechiness = speechiness;
        this.stats.instrumentalness = instrumentalness;
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
