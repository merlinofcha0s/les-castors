CREATE TABLE Adresse (
  id                BIGSERIAL    NOT NULL,
  adresse           VARCHAR(255) NOT NULL,
  codePostal        VARCHAR(5)   NOT NULL,
  complementAdresse VARCHAR(255),
  ville             VARCHAR(45)  NOT NULL,
  departement       INT4         NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Annonce (
  id                         BIGSERIAL    NOT NULL,
  dateCreation               TIMESTAMP,
  dateMAJ                    TIMESTAMP,
  delaiIntervention          INT4         NOT NULL,
  description                VARCHAR(500) NOT NULL,
  etatAnnonce                INT4         NOT NULL,
  typeTravaux                INT4         NOT NULL,
  nbConsultation             INT4         NOT NULL,
  typeContact                INT4         NOT NULL,
  hashID                     VARCHAR(255),
  selHashID                  VARCHAR(255),
  adresseChantier_id         INT8,
  demandeur_fk               INT8,
  avis_id                    INT8,
  entreprise_selectionnee_fk INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Artisan (
  id              BIGSERIAL    NOT NULL,
  civilite        INT4         NOT NULL,
  dateInscription TIMESTAMP    NOT NULL,
  email           VARCHAR(128) NOT NULL,
  login           VARCHAR(25)  NOT NULL,
  nom             VARCHAR(20)  NOT NULL,
  numeroTel       VARCHAR(10)  NOT NULL,
  password        VARCHAR(80)  NOT NULL,
  prenom          VARCHAR(20)  NOT NULL,
  cleActivation   VARCHAR(255),
  isActive        BOOLEAN      NOT NULL,
  entreprise_id   INT8,
  PRIMARY KEY (id)
);

CREATE TABLE CategorieMetier (
  id              BIGSERIAL NOT NULL,
  categorieMetier INT2      NOT NULL,
  entreprise_fk INT8,
  motcle_fk INT8 NULL,
  PRIMARY KEY (id)
);

CREATE TABLE MotCle (
  id           BIGSERIAL NOT NULL,
  motCle       VARCHAR(25),
  annonce_fk   INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Entreprise (
  id              BIGSERIAL   NOT NULL,
  logo            VARCHAR(255),
  nbEmployees     INT4,
  nomComplet      VARCHAR(26) NOT NULL,
  specialite      VARCHAR(25),
  statutJuridique INT4        NOT NULL,
  siret           VARCHAR(14) NOT NULL,
  dateCreation    DATE        NOT NULL,
  isVerifier      BOOLEAN     NOT NULL,
  adresse_id      INT8,
  paiement_id     INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Avis (
  id          BIGSERIAL    NOT NULL,
  commentaire VARCHAR(500) NOT NULL,
  score       FLOAT8       NOT NULL,
  dateAvis    TIMESTAMP    NOT NULL,
  artisan_fk  INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Paiement (
  id                    BIGSERIAL    NOT NULL,
  codeSecurite          VARCHAR(255) NOT NULL,
  dateExpiration        DATE         NOT NULL,
  numeroCarte           VARCHAR(255) NOT NULL,
  adresseFacturation_id INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Client (
  id              BIGSERIAL    NOT NULL,
  dateInscription TIMESTAMP    NOT NULL,
  email           VARCHAR(128) NOT NULL,
  login           VARCHAR(25)  NOT NULL,
  nom             VARCHAR(20),
  numeroTel       VARCHAR(10),
  password        VARCHAR(80)  NOT NULL,
  prenom          VARCHAR(20),
  isActive        BOOLEAN      NOT NULL,
  cleActivation   VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE Permission (
  id         BIGSERIAL NOT NULL,
  typeCompte INT4      NOT NULL,
  client_fk  INT8,
  artisan_fk INT8,
  PRIMARY KEY (id)
);

CREATE TABLE annonce_artisan (
  Annonce_id  INT8 NOT NULL,
  artisans_id INT8 NOT NULL
);

CREATE TABLE Notification (
  id                  BIGSERIAL NOT NULL,
  dateNotification    TIMESTAMP NOT NULL,
  typeNotification    INT4      NOT NULL,
  pourQuiNotification INT4      NOT NULL,
  statutNotification  INT4      NOT NULL,
  id_artisan          INT8,
  id_client           INT8,
  id_annonce          INT8,
  PRIMARY KEY (id)
);

CREATE TABLE Image (
  id            BIGSERIAL    NOT NULL,
  url           VARCHAR(255) NOT NULL,
  id_annonce    INT8,
  id_entreprise INT8,
  PRIMARY KEY (id)
);

ALTER TABLE Annonce
ADD CONSTRAINT annonce_adresse
FOREIGN KEY (adresseChantier_id)
REFERENCES Adresse ON DELETE CASCADE;

ALTER TABLE Annonce
ADD CONSTRAINT annonce_client
FOREIGN KEY (demandeur_fk)
REFERENCES Client;

ALTER TABLE Annonce
ADD CONSTRAINT annonce_avis
FOREIGN KEY (avis_id)
REFERENCES Avis ON DELETE CASCADE;

ALTER TABLE Annonce
ADD CONSTRAINT annonce_entreprise
FOREIGN KEY (entreprise_selectionnee_fk)
REFERENCES Entreprise;

ALTER TABLE Artisan
ADD CONSTRAINT artisan_entreprise
FOREIGN KEY (entreprise_id)
REFERENCES Entreprise ON DELETE CASCADE;

ALTER TABLE CategorieMetier
ADD CONSTRAINT categorie_metier_entreprise
FOREIGN KEY (entreprise_fk)
REFERENCES Entreprise;

ALTER TABLE CategorieMetier
ADD CONSTRAINT categorie_metier_motcle
FOREIGN KEY (motcle_fk)
REFERENCES MotCle;

ALTER TABLE MotCle
ADD CONSTRAINT mot_cle_annonce
FOREIGN KEY (annonce_fk)
REFERENCES Annonce ON DELETE CASCADE;

ALTER TABLE Entreprise
ADD CONSTRAINT entreprise_adresse
FOREIGN KEY (adresse_id)
REFERENCES Adresse ON DELETE CASCADE;

ALTER TABLE Entreprise
ADD CONSTRAINT entreprise_paiement
FOREIGN KEY (paiement_id)
REFERENCES Paiement ON DELETE CASCADE;

ALTER TABLE Avis
ADD CONSTRAINT avis_artisan
FOREIGN KEY (artisan_fk)
REFERENCES Artisan;

ALTER TABLE Paiement
ADD CONSTRAINT paiement_adresse
FOREIGN KEY (adresseFacturation_id)
REFERENCES Adresse ON DELETE CASCADE;

ALTER TABLE Permission
ADD CONSTRAINT artisan_permission
FOREIGN KEY (artisan_fk)
REFERENCES Artisan;

ALTER TABLE Permission
ADD CONSTRAINT client_permission
FOREIGN KEY (client_fk)
REFERENCES Client;

ALTER TABLE annonce_artisan
ADD CONSTRAINT annonce_artisan
FOREIGN KEY (artisans_id)
REFERENCES Artisan;

ALTER TABLE annonce_artisan
ADD CONSTRAINT artisan_annonce
FOREIGN KEY (Annonce_id)
REFERENCES Annonce;

ALTER TABLE Notification
ADD CONSTRAINT notification_artisan2
FOREIGN KEY (id_artisan)
REFERENCES Artisan;

ALTER TABLE Notification
ADD CONSTRAINT notification_client
FOREIGN KEY (id_client)
REFERENCES Client;

ALTER TABLE Notification
ADD CONSTRAINT notification_annonce
FOREIGN KEY (id_annonce)
REFERENCES Annonce ON DELETE CASCADE;

ALTER TABLE Image
ADD CONSTRAINT image_annonce
FOREIGN KEY (id_annonce)
REFERENCES Annonce ON DELETE CASCADE;

ALTER TABLE Image
ADD CONSTRAINT image_entreprise
FOREIGN KEY (id_entreprise)
REFERENCES Entreprise ON DELETE CASCADE;