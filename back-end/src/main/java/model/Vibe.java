package model;

public class Vibe {
    private String id;
    private String userId;
    private User user;
    private String originTemplateId;
    private VibeTemplate originTemplate;
    private String name;
    private String description;
    private Features minFeatures;
    private Features maxFeatures;
    private VibeSeed[] vibeSeeds;
    
    public Vibe(String id, String userId, String originTemplateId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
    }
    
    public Vibe(String id, String userId, String originTemplateId, String name, String description, Integer minPopularity,
            Float minTempo, Double minValence, Double minLiveness, Double minAcousticness, Double minDanceability, Double minEnergy,
            Double minSpeechiness, Double minInstrumentalness, Integer maxPopularity, Float maxTempo, Double maxValence,
            Double maxLiveness, Double maxAcousticness, Double maxDanceability, Double maxEnergy, Double maxSpeechiness,
            Double maxInstrumentalness) {
        this.id = id;
        this.userId = userId;
        this.originTemplateId = originTemplateId;
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
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }
    
    public String getOriginTemplateId() {
        return this.originTemplateId;
    }
    
    public void setOriginTemplateId(String originTemplateId) {
        this.originTemplateId = originTemplateId;
    }
    
    public VibeTemplate getOriginTemplate() {
        return this.originTemplate;
    }
    
    public void setOriginTemplate(VibeTemplate originTemplate) {
        this.originTemplate = originTemplate;
        this.originTemplateId = originTemplate.getId();
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
    
    public VibeSeed[] getVibeSeeds() {
        return this.vibeSeeds;
    }
    
    public void setVibeSeeds(VibeSeed[] vibeSeeds) {
        this.vibeSeeds = vibeSeeds;
    }
    
    public Features getMinFeatures() {
        return this.minFeatures;
    }
    
    public Features getMaxFeatures() {
        return this.maxFeatures;
    }
}
