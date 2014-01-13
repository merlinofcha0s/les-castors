create table Adresse (
        id int8 not null,
        adresse varchar(255) not null,
        codePostal varchar(5) not null,
        complementAdresse varchar(255) not null,
        ville varchar(45) not null,
        primary key (id)
    );
    
create table Annonce (
        id int8 not null,
        active boolean not null,
        dateCreation timestamp,
        dateMAJ timestamp,
        delaiIntervention varchar(15) not null,
        description varchar(500) not null,
        etatAnnonce int4 not null,
        metier int4 not null,
        nbConsultation int4 not null,
        nbDevis int4 not null,
        photo varchar(255) not null,
        titre varchar(45) not null,
        typeContact varchar(45) not null,
        adresseChantier_id int8,
        demandeur_fk int8,
        primary key (id)
    );
    
    create table Artisan (
        id int8 not null,
        activitePrincipale varchar(255) not null,
        civilite int4 not null,
        dateInscription timestamp not null,
        domaineActivite varchar(255) not null,
        email varchar(128) not null,
        login varchar(25) not null,
        metier int4 not null,
        nbAnnonce int4 not null,
        nom varchar(20) not null,
        numeroTel varchar(10) not null,
        password varchar(80) not null,
        prenom varchar(20) not null,
        siret int4 not null,
        entreprise_id int8,
        primary key (id)
    );
    
    create table Entreprise (
        id int8 not null,
        capitalSociale int4 not null,
        logo varchar(255) not null,
        nbEmployees int4 not null,
        nomComplet varchar(40) not null,
        statutJuridique int4 not null,
        adresse_id int8,
        paiement_id int8,
        primary key (id)
    );
    
    create table Notation (
        id int8 not null,
        commentaire varchar(500) not null,
        score float8 not null,
        annonce_id int8,
        artisan_fk int8,
        primary key (id)
    );
    
    create table Paiement (
        id int8 not null,
        codeSecurite varchar(255) not null,
        dateExpiration date not null,
        numeroCarte varchar(255) not null,
        adresseFacturation_id int8,
        primary key (id)
    );
    
     create table Users (
        id int8 not null,
        civilite int4,
        dateInscription timestamp not null,
        email varchar(128) not null,
        login varchar(25) not null,
        nom varchar(20),
        numeroTel varchar(10) not null,
        password varchar(80) not null,
        prenom varchar(20),
        primary key (id)
    );
    
    alter table Annonce 
        add constraint FK_annonce_adresse 
        foreign key (adresseChantier_id) 
        references Adresse;
        
    alter table Annonce 
        add constraint FK_annonce_user 
        foreign key (demandeur_fk) 
        references Users;
        
    alter table Artisan 
        add constraint FK_artisan_entreprise 
        foreign key (entreprise_id) 
        references Entreprise;
        
    alter table Entreprise 
        add constraint FK_entreprise_adresse
        foreign key (adresse_id) 
        references Adresse;
        
    alter table Entreprise 
        add constraint FK_entreprise_paiement 
        foreign key (paiement_id) 
        references Paiement;
        
    alter table Notation 
        add constraint FK_notation_annonce 
        foreign key (annonce_id) 
        references Annonce;
        
    alter table Notation 
        add constraint FK_notation_artisan
        foreign key (artisan_fk) 
        references Artisan;
        
    alter table Paiement 
        add constraint FK_notation_adresseFacturation
        foreign key (adresseFacturation_id) 
        references Adresse;
        
     