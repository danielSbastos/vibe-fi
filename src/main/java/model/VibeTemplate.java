package model;

import java.util.UUID;

public class VibeTemplate {
    private String id;
    private String name;
    private String description;
    private Features minFeatures;
    private Features maxFeatures;
    
    public VibeTemplate(String id, String name, String description) {
        setId(id);
        this.name = name;
        this.description = description;
        this.minFeatures = new Features();
        this.maxFeatures = new Features();
    }

    public VibeTemplate(String name, String description) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.minFeatures = new Features();
        this.maxFeatures = new Features();
    }

    public VibeTemplate(String id, String name, String description, Features minFeatures, Features maxFeatures) {
        setId(id);
        this.name = name;
        this.description = description;
        this.minFeatures = minFeatures;
        this.maxFeatures = maxFeatures;
    }

    public VibeTemplate(String name, String description, Features minFeatures, Features maxFeatures) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.minFeatures = minFeatures;
        this.maxFeatures = maxFeatures;
    }
    
    public VibeTemplate(String id, String name, String description, Integer minPopularity, Integer maxPopularity,
            Double minTempo, Double maxTempo, Double minValence, Double maxValence, Double minLiveness,
            Double maxLiveness, Double minAcousticness, Double maxAcousticness, Double minDanceability,
            Double maxDanceability, Double minEnergy, Double maxEnergy, Double minSpeechiness, Double maxSpeechiness,
            Double minInstrumentalness, Double maxInstrumentalness) {
        setId(id);
        this.name = name;
        this.description = description;
        this.minFeatures = new Features(minPopularity, minTempo, minValence, minLiveness, minAcousticness,
                minDanceability, minEnergy, minSpeechiness, minInstrumentalness);
        this.maxFeatures = new Features(maxPopularity, maxTempo, maxValence, maxLiveness, maxAcousticness,
                maxDanceability, maxEnergy, maxSpeechiness, maxInstrumentalness);
    }

    public VibeTemplate(String name, String description, Integer minPopularity, Integer maxPopularity,
            Double minTempo, Double maxTempo, Double minValence, Double maxValence, Double minLiveness,
            Double maxLiveness, Double minAcousticness, Double maxAcousticness, Double minDanceability,
            Double maxDanceability, Double minEnergy, Double maxEnergy, Double minSpeechiness, Double maxSpeechiness,
            Double minInstrumentalness, Double maxInstrumentalness) {
        setId(UUID.randomUUID().toString());
        this.name = name;
        this.description = description;
        this.minFeatures = new Features(minPopularity, minTempo, minValence, minLiveness, minAcousticness,
                minDanceability, minEnergy, minSpeechiness, minInstrumentalness);
        this.maxFeatures = new Features(maxPopularity, maxTempo, maxValence, maxLiveness, maxAcousticness,
                maxDanceability, maxEnergy, maxSpeechiness, maxInstrumentalness);
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

    public Features getMinFeatures() {
        return this.minFeatures;
    }

    public Features getMaxFeatures() {
        return this.maxFeatures;
    }
}
