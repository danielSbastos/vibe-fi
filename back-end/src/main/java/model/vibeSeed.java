package model;

import util.exceptions.InvalidSeedTypeValueException;

public class VibeSeed {
    private String vibeId;
    private Vibe vibe;
    private String identifier;
    private String type;

    public VibeSeed(String identifier, String type) throws InvalidSeedTypeValueException {
        setIdentifier(identifier);
        setType(type);
    }

    public VibeSeed(String vibeId, String identifier, String type) throws InvalidSeedTypeValueException {
        setVibeId(vibeId);
        setIdentifier(identifier);
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
