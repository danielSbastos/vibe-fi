package model;

import java.util.UUID;

public class Vibe {
    private String id;
    private String userId;
    private User user;
    private String originTemplateId;
    private VibeTemplate originTemplate;
    private String name;
    private String description;
    private Features features;
    private VibeSeed[] vibeSeeds;

    public Vibe(String id, String userId, String originTemplateId, String name, String description) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.features = new Features();
    }

    public Vibe(String userId, String originTemplateId, String name, String description) {
        setId(UUID.randomUUID().toString());
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.features = new Features();
    }

    public Vibe(String userId, VibeTemplate originTemplate) {
        setId(UUID.randomUUID().toString());
        this.userId = userId;
        this.originTemplateId = originTemplate.getId();
        this.name = originTemplate.getName();
        this.description = originTemplate.getDescription();
        this.features = originTemplate.getFeatures();
    }

    public Vibe(String id, String userId, String originTemplateId, String name, String description, Features features) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.features = features;
    }

    public Vibe(String userId, String originTemplateId, String name, String description, Features features) {
        setId(UUID.randomUUID().toString());
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.features = features;
    }

    public Vibe(String id, String userId, String originTemplateId, String name, String description, Integer popularity,
            Double tempo, Double valence, Double liveness, Double acousticness, Double danceability, Double energy,
            Double speechiness, Double instrumentalness) {
        setId(id);
        this.userId = userId;
        this.originTemplateId = originTemplateId;
        this.name = name;
        this.description = description;
        this.features = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy,
                speechiness, instrumentalness);
    }

    public Vibe(String userId, String originTemplateId, String name, String description, Integer minPopularity,
            Integer popularity, Double tempo, Double valence, Double liveness, Double acousticness, Double danceability,
            Double energy, Double speechiness, Double instrumentalness) {
        setId(UUID.randomUUID().toString());
        this.userId = userId;
        this.originTemplateId = originTemplateId;
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

    public Features getFeatures() {
        return this.features;
    }
}
