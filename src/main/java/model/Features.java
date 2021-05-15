package model;

public class Features {
    protected Integer popularity;
    protected Double tempo;
    protected Double valence;
    protected Double liveness;
    protected Double acousticness;
    protected Double danceability;
    protected Double energy;
    protected Double speechiness;
    protected Double instrumentalness;

    public Features() {
        this.popularity = null;
        this.tempo = null;
        this.valence = null;
        this.liveness = null;
        this.acousticness = null;
        this.danceability = null;
        this.energy = null;
        this.speechiness = null;
        this.instrumentalness = null;
    }

    public Features(Integer popularity, Double tempo, Double valence, Double liveness, Double acousticness,
            Double danceability, Double energy, Double speechiness, Double instrumentalness) {
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

    public Integer getPopularity() {
        return this.popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Double getTempo() {
        return this.tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Double getValence() {
        return this.valence;
    }

    public void setValence(Double valence) {
        this.valence = valence;
    }

    public Double getLiveness() {
        return this.liveness;
    }

    public void setLiveness(Double liveness) {
        this.liveness = liveness;
    }

    public Double getAcousticness() {
        return this.acousticness;
    }

    public void setAcousticness(Double acousticness) {
        this.acousticness = acousticness;
    }

    public Double getDanceability() {
        return this.danceability;
    }

    public void setDanceability(Double danceability) {
        this.danceability = danceability;
    }

    public Double getEnergy() {
        return this.energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getSpeechiness() {
        return this.speechiness;
    }

    public void setSpeechiness(Double speechiness) {
        this.speechiness = speechiness;
    }

    public Double getInstrumentalness() {
        return this.instrumentalness;
    }

    public void setInstrumentalness(Double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

}
