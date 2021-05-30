package model;

import java.util.UUID;

public class VibeTemplate {
    private String id;
    private String name;
    private String description;
    private Features features;

    public VibeTemplate(String id, String name, String description) {
        setId(id);
        this.name = name;
        this.description = description;
        this.features = new Features();
    }

    public VibeTemplate(String name, String description) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.features = new Features();

    }

    public VibeTemplate(String id, String name, String description, Features features) {
        setId(id);
        this.name = name;
        this.description = description;
        this.features = features;
    }

    public VibeTemplate(String name, String description, Features features) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.features = features;

    }

    public VibeTemplate(String id, String name, String description, Integer popularity, Double tempo, Double valence,
            Double liveness, Double acousticness, Double danceability, Double energy, Double speechiness,
            Double instrumentalness) {
        setId(id);
        this.name = name;
        this.description = description;
        this.features = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy,
                speechiness, instrumentalness);
    }

    public VibeTemplate(String name, String description, Integer popularity, Double tempo, Double valence,
            Double liveness, Double acousticness, Double danceability, Double energy, Double speechiness,
            Double instrumentalness) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.features = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy,
                speechiness, instrumentalness);
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Features getFeatures() {
        return this.features;
    }
}
