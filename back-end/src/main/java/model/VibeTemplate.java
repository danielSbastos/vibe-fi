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
    
    
    public VibeTemplate(String id, String name, String description, Integer minPopularity, Float minTempo,
            Double minValence, Double minLiveness, Double minAcousticness, Double minDanceability, Double minEnergy,
            Double minSpeechiness, Double minInstrumentalness, Integer maxPopularity, Float maxTempo, Double maxValence,
            Double maxLiveness, Double maxAcousticness, Double maxDanceability, Double maxEnergy, Double maxSpeechiness,
            Double maxInstrumentalness) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minFeatures.popularity = minPopularity;
        this.minFeatures.tempo = minTempo;
        this.minFeatures.valence = minValence;
        this.minFeatures.liveness = minLiveness;
        this.minFeatures.acousticness = minAcousticness;
        this.minFeatures.danceability = minDanceability;
        this.minFeatures.energy = minEnergy;
        this.minFeatures.speechiness = minSpeechiness;
        this.minFeatures.instrumentalness = minInstrumentalness;
        this.maxFeatures.popularity = maxPopularity;
        this.maxFeatures.tempo = maxTempo;
        this.maxFeatures.valence = maxValence;
        this.maxFeatures.liveness = maxLiveness;
        this.maxFeatures.acousticness = maxAcousticness;
        this.maxFeatures.danceability = maxDanceability;
        this.maxFeatures.energy = maxEnergy;
        this.maxFeatures.speechiness = maxSpeechiness;
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
