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
        typeTravaux int4 not null,
        categorieMetier int2 not null,
        sousCategorieMetier varchar(40) not null,
        nbConsultation int4 not null,
        photo varchar(255),
        typeContact int4 not null,
        hashID varchar(255),
        selHashID varchar(255),
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
        cleActivation varchar(255),
        isActive boolean not null,
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
        primary key (id)
    );
    
    create table Permission (
        id  bigserial not null,
        typeCompte int4 not null,
        client_fk int8,
        artisan_fk int8,
        primary key (id)
    );
    
    create table annonce_artisan (
        Annonce_id int8 not null,
        artisans_id int8 not null,
        Artisan_id int8 not null,
        annonces_id int8 not null
    );
    
    create table Notification (
        id  bigserial not null,
        dateNotification timestamp not null,
        typeNotification int4 not null,
        pourQuiNotification int4 not null,
        statutNotification int4 not null,
        id_artisan int8,
        id_client int8,
        id_annonce int8,
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
        
    alter table Permission
        add constraint artisan_permission 
        foreign key (artisan_fk) 
        references Artisan;
        
    alter table Permission
        add constraint client_permission 
        foreign key (client_fk) 
        references Client;
        
    alter table annonce_artisan 
        add constraint annonce_artisan 
        foreign key (artisans_id) 
        references Artisan;
        
    alter table annonce_artisan 
        add constraint artisan_annonce 
        foreign key (Annonce_id) 
        references Annonce;
        
    alter table annonce_artisan 
        add constraint annonce_artisan2 
        foreign key (annonces_id) 
        references Annonce;
        
    alter table annonce_artisan 
        add constraint artisan_annonce2
        foreign key (Artisan_id) 
        references Artisan;
        
    alter table Notification 
        add constraint notification_artisan2
        foreign key (id_artisan) 
        references Artisan;
        
    alter table Notification 
        add constraint notification_client
        foreign key (id_client) 
        references Client;
        
    alter table Notification 
        add constraint notification_annonce
        foreign key (id_annonce) 
        references Annonce;
        
     