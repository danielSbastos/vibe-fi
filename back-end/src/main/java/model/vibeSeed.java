package model;

import util.exceptions.InvalidSeedTypeValueException;

public class vibeSeed {
    private String vibeId;
    private Vibe vibe;
    private String identifier;
    private String type;
    
    
    public vibeSeed(String identifier, String type) throws InvalidSeedTypeValueException {
        this.identifier = identifier;
        setType(type);
    }
    

    public String getVibeId() {
        return this.vibeId;
    }

    public void setVibeId(String vibeId) {
        this.vibeId = vibeId;
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
        this.identifier = identifier;
    }
    
    public String getType() {
        return this.type;
    }

    public void setType(String type) throws InvalidSeedTypeValueException {
        if (type == "track" || type == "artist" || type == "genre") {
            this.type = type;
        } else {
            throw new InvalidSeedTypeValueException(type + " is not a valid seed type.");
        }
    }
    
}
