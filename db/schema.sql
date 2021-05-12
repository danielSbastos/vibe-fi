CREATE SCHEMA IF NOT EXISTS vibeFi;

DROP TABLE IF EXISTS vibeFi.vibeSeed;
DROP TABLE IF EXISTS vibeFi.Vibe;
DROP TABLE IF EXISTS vibeFi.Template;
DROP TABLE IF EXISTS vibeFi.User;
DROP TYPE IF EXISTS SeedType;

CREATE TABLE IF NOT EXISTS vibeFi.User (
  idSpotify CHAR(22) UNIQUE NOT NULL,
  name VARCHAR(80) NOT NULL,
  popularity INT NULL,
  tempo DOUBLE PRECISION NULL,
  valence DOUBLE PRECISION NULL,
  liveness DOUBLE PRECISION NULL,
  acousticness DOUBLE PRECISION NULL,
  danceability DOUBLE PRECISION NULL,
  energy DOUBLE PRECISION NULL,
  speechiness DOUBLE PRECISION NULL,
  instrumentalness DOUBLE PRECISION NULL,
  PRIMARY KEY (idSpotify));
CREATE UNIQUE INDEX idSpotifyUser_UNIQUE ON vibeFi.User (idSpotify ASC);


CREATE TABLE IF NOT EXISTS vibeFi.Template (
  idTemplate CHAR(22) UNIQUE NOT NULL,
  name VARCHAR(80) NOT NULL,
  description VARCHAR(240) NULL,
  minPopularity INT NULL,
  maxPopularity INT NULL,
  minTempo DOUBLE PRECISION NULL,
  maxTempo DOUBLE PRECISION NULL,
  minValence DOUBLE PRECISION NULL,
  maxValence DOUBLE PRECISION NULL,
  minLiveness DOUBLE PRECISION NULL,
  maxLiveness DOUBLE PRECISION NULL,
  minAcousticness DOUBLE PRECISION NULL,
  maxAcousticness DOUBLE PRECISION NULL,
  minDanceability DOUBLE PRECISION NULL,
  maxDanceability DOUBLE PRECISION NULL,
  minEnergy DOUBLE PRECISION NULL,
  maxEnergy DOUBLE PRECISION NULL,
  minSpeechiness DOUBLE PRECISION NULL,
  maxSpeechiness DOUBLE PRECISION NULL,
  minInstrumentalness DOUBLE PRECISION NULL,
  maxInstrumentalness DOUBLE PRECISION NULL,
  PRIMARY KEY (idTemplate));
CREATE UNIQUE INDEX idTemplate_UNIQUE ON vibeFi.Template (idTemplate ASC);


CREATE TABLE IF NOT EXISTS vibeFi.Vibe (
  idVibe CHAR(22) UNIQUE NOT NULL,
  idUser CHAR(22) NOT NULL,
  originTemplate CHAR(22) NULL,
  name VARCHAR(80) NOT NULL,
  description VARCHAR(240) NULL,
  minPopularity INT NULL,
  maxPopularity INT NULL,
  minTempo DOUBLE PRECISION NULL,
  maxTempo DOUBLE PRECISION NULL,
  minValence DOUBLE PRECISION NULL,
  maxValence DOUBLE PRECISION NULL,
  minLiveness DOUBLE PRECISION NULL,
  maxLiveness DOUBLE PRECISION NULL,
  minAcousticness DOUBLE PRECISION NULL,
  maxAcousticness DOUBLE PRECISION NULL,
  minDanceability DOUBLE PRECISION NULL,
  maxDanceability DOUBLE PRECISION NULL,
  minEnergy DOUBLE PRECISION NULL,
  maxEnergy DOUBLE PRECISION NULL,
  minSpeechiness DOUBLE PRECISION NULL,
  maxSpeechiness DOUBLE PRECISION NULL,
  minInstrumentalness DOUBLE PRECISION NULL,
  maxInstrumentalness DOUBLE PRECISION NULL,
  PRIMARY KEY (idVibe),
  CONSTRAINT fk_Vibe_Template
    FOREIGN KEY (originTemplate)
    REFERENCES vibeFi.Template (idTemplate)
    ON DELETE SET NULL,
  CONSTRAINT fk_Vibe_User
    FOREIGN KEY (idUser)
    REFERENCES vibeFi.User (idSpotify)
    ON DELETE CASCADE);
CREATE UNIQUE INDEX idVibe_UNIQUE ON vibeFi.Vibe (idVibe ASC);
CREATE INDEX fk_Vibe_User_idx ON vibeFi.Vibe (idUser ASC);


CREATE TYPE SeedType AS ENUM ('track', 'artist', 'genre');
CREATE TABLE IF NOT EXISTS vibeFi.vibeSeed (
  vibe CHAR(22) NOT NULL,
  seedIdentifier CHAR(22) NOT NULL,
  tipo SeedType NOT NULL,
  PRIMARY KEY (vibe, seedIdentifier),
  CONSTRAINT fk_vibeSeed_Vibe1
    FOREIGN KEY (vibe)
    REFERENCES vibeFi.Vibe (idVibe)
    ON DELETE CASCADE);