package model;

public class Vibe extends Features {
    private String id;
    private String userId;
    private User user;
    private String originTemplateId;
    private VibeTemplate originTemplate;
    private String name;
    private String description;
    private vibeSeed[] vibeSeeds;
    

    public Vibe(String id, String userId, String originTemplateId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
    }
    

    public Vibe(String id, String userId, String originTemplateId, String name, String description, int popularity,
            float tempo, double valence, double liveness, double acousticness, double danceability, double energy,
            double speechiness, double instrumentalness) {
        this.id = id;
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
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

    public vibeSeed[] getVibeSeeds() {
        return this.vibeSeeds;
    }

    public void setVibeSeeds(vibeSeed[] vibeSeeds) {
        this.vibeSeeds = vibeSeeds;
    }

}
