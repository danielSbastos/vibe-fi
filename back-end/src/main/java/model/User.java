package model;

public class User extends Features {

    private String id;
    private String name;


    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    

    public User(String id, String name, int popularity, float tempo, double valence, double liveness,
            double acousticness, double danceability, double energy, double speechiness, double instrumentalness) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.tempo = tempo;
        this.valence = valence;
        this.liveness = liveness;
        this.acousticness = acousticness;
        this.danceability = danceability;
        this.energy = energy;
        this.speechiness = speechiness;
        this.instrumentalness = instrumentalness;
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

}
