CREATE SCHEMA IF NOT EXISTS vibeFi;

DROP TABLE IF EXISTS vibeFi.Usuario ;

CREATE TABLE IF NOT EXISTS vibeFi.Usuario (
  idSpotify CHAR(22) NOT NULL,
  Nome VARCHAR(80) NOT NULL,
  grauPopular INT NULL,
  grauFeliz DOUBLE PRECISION NULL,
  bpmMedio FLOAT NULL,
  grauAcustico DOUBLE PRECISION NULL,
  grauDancavel DOUBLE PRECISION NULL,
  PRIMARY KEY (idSpotify));

CREATE UNIQUE INDEX idSpotifyUser_UNIQUE ON vibeFi.Usuario (idSpotify ASC);


DROP TABLE IF EXISTS vibeFi.VibeBase ;

CREATE TABLE IF NOT EXISTS vibeFi.VibeBase (
  idVibe CHAR(22) NOT NULL,
  Nome VARCHAR(80) NOT NULL,
  descricao VARCHAR(240) NULL,
  grauPopular INT NULL,
  grauFeliz DOUBLE PRECISION NULL,
  bpmMedio FLOAT NULL,
  grauAcustico DOUBLE PRECISION NULL,
  grauDancavel DOUBLE PRECISION NULL,
  PRIMARY KEY (idVibe));

CREATE UNIQUE INDEX idVibeBase_UNIQUE ON vibeFi.VibeBase (idVibe ASC);


DROP TABLE IF EXISTS vibeFi.VibeUsuario ;

CREATE TABLE IF NOT EXISTS vibeFi.VibeUsuario (
  idVibe CHAR(22) NOT NULL,
  idUsuario CHAR(22) NOT NULL,
  Nome VARCHAR(80) NOT NULL,
  descricao VARCHAR(240) NULL,
  Origem CHAR(22) NULL,
  grauPopular INT NULL,
  grauFeliz DOUBLE PRECISION NULL,
  bpmMedio FLOAT NULL,
  grauAcustico DOUBLE PRECISION NULL,
  grauDancavel DOUBLE PRECISION NULL,
  PRIMARY KEY (idVibe),
  CONSTRAINT fk_VibeUsuario_VibeBase
    FOREIGN KEY (Origem)
    REFERENCES vibeFi.VibeBase (idVibe)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_VibeUsuario_Usuario
    FOREIGN KEY (idUsuario)
    REFERENCES vibeFi.Usuario (idSpotify)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE UNIQUE INDEX idVibeUsuario_UNIQUE ON vibeFi.VibeUsuario (idVibe ASC);

CREATE INDEX fk_VibeUsuario_VibeBase_idx ON vibeFi.VibeUsuario (Origem ASC);

CREATE INDEX fk_VibeUsuario_Usuario_idx ON vibeFi.VibeUsuario (idUsuario ASC);


DROP TABLE IF EXISTS vibeFi.vibeSeed ;

DROP TYPE IF EXISTS SeedType;
CREATE TYPE SeedType AS ENUM ('track', 'artist', 'genre');
CREATE TABLE IF NOT EXISTS vibeFi.vibeSeed (
  vibe CHAR(22) NOT NULL,
  seedIdentifier CHAR(22) NOT NULL,
  tipo SeedType NOT NULL,
  PRIMARY KEY (vibe, seedIdentifier),
  CONSTRAINT fk_vibeSeed_VibeUsuario1
    FOREIGN KEY (vibe)
    REFERENCES vibeFi.VibeUsuario (idVibe)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
