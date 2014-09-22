create table Adresse (
        id  bigserial not null,
        adresse varchar(255) not null,
        codePostal varchar(5) not null,
        complementAdresse varchar(255),
        ville varchar(45) not null,
        departement int4 not null,
        primary key (id)
    );
    
create table Annonce (
        id  bigserial not null,
        dateCreation timestamp,
        dateMAJ timestamp,
        delaiIntervention int4 not null,
        description varchar(500) not null,
        etatAnnonce int4 not null,
        categorieMetier varchar(40),
        sousCategorieMetier varchar(40),
        nbConsultation int4 not null,
        photo varchar(255),
        typeContact int4 not null,
        adresseChantier_id int8,
        demandeur_fk int8,
        notationAnnonce_id int8,
        primary key (id)
    );
    
    create table Artisan (
        id  bigserial not null,
        civilite int4 not null,
        dateInscription timestamp not null,
        email varchar(128) not null,
        login varchar(25) not null,
        nom varchar(20) not null,
        numeroTel varchar(10) not null,
        password varchar(80) not null,
        prenom varchar(20) not null,
        isActive boolean not null,
        cleActivation varchar(255),
        typeCompte int4 not null,
        entreprise_id int8,
        primary key (id)
    );
    
    create table CategorieMetier (
        id  bigserial not null,
        categorieMetier int2 not null,
        entreprise_fk int8,
        primary key (id)
    );
    
    create table Entreprise (
        id  bigserial not null,
        logo varchar(255),
        nbEmployees int4,
        nomComplet varchar(40) not null,
        specialite varchar(50),
        statutJuridique int4 not null,
        siret varchar(14) not null,
        dateCreation date not null,
        adresse_id int8,
        paiement_id int8,
        primary key (id)
    );
    
    create table Notation (
        id  bigserial not null,
        commentaire varchar(500) not null,
        score float8 not null,
        artisan_fk int8,
        primary key (id)
    );
    
    create table Paiement (
        id  bigserial not null,
        codeSecurite varchar(255) not null,
        dateExpiration date not null,
        numeroCarte varchar(255) not null,
        adresseFacturation_id int8,
        primary key (id)
    );
    
     create table Client (
        id  bigserial not null,
        dateInscription timestamp not null,
        email varchar(128) not null,
        login varchar(25) not null,
        nom varchar(20),
        numeroTel varchar(10) not null,
        password varchar(80) not null,
        prenom varchar(20),
        isActive boolean not null,
        cleActivation varchar(255),
        typeCompte int4 not null,
        primary key (id)
    );
    
    alter table Annonce 
        add constraint annonce_adresse
        foreign key (adresseChantier_id) 
        references Adresse;

    alter table Annonce 
        add constraint annonce_client 
        foreign key (demandeur_fk) 
        references Client;

    alter table Annonce 
        add constraint annonce_notation 
        foreign key (notationAnnonce_id) 
        references Notation;

    alter table Artisan 
        add constraint artisan_entreprise 
        foreign key (entreprise_id) 
        references Entreprise;

    alter table CategorieMetier 
        add constraint categorie_metier_entreprise 
        foreign key (entreprise_fk) 
        references Entreprise;

    alter table Entreprise 
        add constraint entreprise_adresse
        foreign key (adresse_id) 
        references Adresse;

    alter table Entreprise 
        add constraint entreprise_paiement 
        foreign key (paiement_id) 
        references Paiement;

    alter table Notation 
        add constraint notation_artisan
        foreign key (artisan_fk) 
        references Artisan;

    alter table Paiement 
        add constraint paiement_adresse 
        foreign key (adresseFacturation_id) 
        references Adresse;
        
     