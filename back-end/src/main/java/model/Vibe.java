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
    
    public Vibe(String id, String userId, String originTemplateId, String name) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = "";
        this.minFeatures = new Features();
        this.maxFeatures = new Features();
    }

    public Vibe(String id, String userId, String originTemplateId, String name, String description) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.minFeatures = new Features();
        this.maxFeatures = new Features();
    }

    public Vibe(String id, String userId, String originTemplateId, String name, String description,
            Integer minPopularity, Integer maxPopularity, Double minTempo, Double maxTempo, Double minValence,
            Double maxValence, Double minLiveness, Double maxLiveness, Double minAcousticness, Double maxAcousticness,
            Double minDanceability, Double maxDanceability, Double minEnergy, Double maxEnergy, Double minSpeechiness,
            Double maxSpeechiness, Double minInstrumentalness, Double maxInstrumentalness) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
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
