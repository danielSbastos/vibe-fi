package model;

public class VibeTemplate {
    private String id;
    private String name;
    private String description;
    private Features minFeatures;
    private Features maxFeatures;

    public VibeTemplate(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public VibeTemplate(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public VibeTemplate(String id, String name, String description, Integer minPopularity, Double minTempo,
            Double minValence, Double minLiveness, Double minAcousticness, Double minDanceability, Double minEnergy,
            Double minSpeechiness, Double minInstrumentalness, Integer maxPopularity, Double maxTempo,
            Double maxValence, Double maxLiveness, Double maxAcousticness, Double maxDanceability, Double maxEnergy,
            Double maxSpeechiness, Double maxInstrumentalness) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minFeatures.popularity = minPopularity;
        this.maxFeatures.popularity = maxPopularity;
        this.minFeatures.tempo = minTempo;
        this.maxFeatures.tempo = maxTempo;
        this.minFeatures.valence = minValence;
        this.maxFeatures.valence = maxValence;
        this.minFeatures.liveness = minLiveness;
        this.maxFeatures.liveness = maxLiveness;
        this.minFeatures.acousticness = minAcousticness;
        this.maxFeatures.acousticness = maxAcousticness;
        this.minFeatures.danceability = minDanceability;
        this.maxFeatures.danceability = maxDanceability;
        this.minFeatures.energy = minEnergy;
        this.maxFeatures.energy = maxEnergy;
        this.minFeatures.speechiness = minSpeechiness;
        this.maxFeatures.speechiness = maxSpeechiness;
        this.minFeatures.instrumentalness = minInstrumentalness;
        this.maxFeatures.instrumentalness = maxInstrumentalness;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Features getMinFeatures() {
        return this.minFeatures;
    }

    public Features getMaxFeatures() {
        return this.maxFeatures;
    }
}
