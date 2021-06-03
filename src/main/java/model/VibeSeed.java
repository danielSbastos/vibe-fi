package model;

import util.exceptions.InvalidSeedTypeValueException;

public class VibeSeed {
    private String vibeId;
    private Vibe vibe;
    private String identifier;
    private String type;
    private String description;

    public VibeSeed(String identifier, String type) throws InvalidSeedTypeValueException {
        setIdentifier(identifier);
        setType(type);
    }

    public VibeSeed(String vibeId, String identifier, String type) throws InvalidSeedTypeValueException {
        setVibeId(vibeId);
        setIdentifier(identifier);
        setType(type);
    }
    
    public VibeSeed(String vibeId, String identifier, String type, String description) throws InvalidSeedTypeValueException {
        setVibeId(vibeId);
        setIdentifier(identifier);
        setType(type);
        setDescription(description);
    }

    public String getVibeId() {
        return this.vibeId;
    }

    public void setVibeId(String vibeId) {
        this.vibeId = vibeId.trim();
    }

    public Vibe getVibe() {
        return this.vibe;
    }

    public void setVibe(Vibe vibe) {
        this.vibe = vibe;
        this.vibeId = vibe.getId();
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier.trim();
    }

    public String getType() {
        return this.type;
    }
    
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setType(String type) throws InvalidSeedTypeValueException {
        type = type.trim().toLowerCase();
        if (type.equals("track") || type.equals("artist") || type.equals("genre")) {
            this.type = type;
        } else {
            throw new InvalidSeedTypeValueException(type + " is not a valid seed type.");
        }
    }
}
