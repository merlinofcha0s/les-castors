package fr.batimen.web.selenium.dataset;

import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.dto.enums.*;
import fr.batimen.dto.helper.CategorieLoader;

import static com.ninja_squad.dbsetup.Operations.insertInto;

public class AnnonceDataset {

    public static final Operation INSERT_ANNONCE_DATA = insertInto("annonce")
            .columns("id", "datecreation", "datemaj", "delaiintervention", "description", "etatannonce",
                    "categoriemetier", "souscategoriemetier", "nbconsultation", "typecontact", "hashID", "selHashID",
                    "typeTravaux", "adressechantier_id", "demandeur_fk", "entreprise_selectionnee_fk",
                    "avis_id")
            .values(200010, "2014-01-10", "2014-01-10", DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE,
                    "Construction compliqué qui necessite des connaissance en geologie", EtatAnnonce.ACTIVE,
                    CategorieLoader.ELECTRICITE_CODE, "Installation électrique", 0, TypeContact.EMAIL, "toto", "tata",
                    TypeTravaux.NEUF, 200005, 100001, null, null)
            .values(200011, "2014-01-10", "2014-01-10", DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE,
                    "Construction compliqué qui necessite des connaissance en geologie", EtatAnnonce.ACTIVE,
                    CategorieLoader.ELECTRICITE_CODE, "Installation électrique", 0, TypeContact.EMAIL, "lolmdr",
                    "tata", TypeTravaux.NEUF, 200014, 100001, 200009, 200013)
            .values(200012, "2014-01-10", "2014-01-10", DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE,
                    "Construction compliqué qui necessite des connaissance en geologie", EtatAnnonce.ACTIVE,
                    CategorieLoader.ELECTRICITE_CODE, "Installation électrique", 0, TypeContact.EMAIL, "lolxd", "titi",
                    TypeTravaux.NEUF, 200015, 100001, null, null)
            .values(200013, "2014-01-10", "2014-01-10", DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE,
                    "Construction compliqué qui necessite des connaissance en geologie", EtatAnnonce.DONNER_AVIS,
                    CategorieLoader.ELECTRICITE_CODE, "Installation électrique", 0, TypeContact.EMAIL, "lolmdrxD",
                    "titi", TypeTravaux.NEUF, 200016, 100001, 200009, null).build();

    public static final Operation INSERT_ARTISAN_DATA = insertInto("artisan")
            .columns("id", "civilite", "email", "nom", "prenom", "login", "password", "numeroTel", "dateInscription",
                    "isActive", "cleActivation", "entreprise_id")
            .values(200008, Civilite.MONSIEUR, "pebronArtisan@batimen.fr", "PebronArtisan", "Toto", "pebron",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125698",
                    "2014-01-10", true, "lolmdr07", 200009).build();

    public static final Operation INSERT_ENTREPRISE_DATA = insertInto("entreprise")
            .columns("id", "nomcomplet", "statutjuridique", "siret", "datecreation", "adresse_id", "isverifier")
            .values(200009, "Entreprise de toto", StatutJuridique.SARL, "43394298400017", "2014-03-23", 200006, true).build();

    public static final Operation INSERT_NOTIFICATION_DATA = insertInto("notification")
            .columns("id", "dateNotification", "typeNotification", "pourquinotification", "statutnotification",
                    "id_artisan", "id_client", "id_annonce")
            .values(200006, "2014-02-10", TypeNotification.INSCRIT_A_ANNONCE, 4, 0, 200008, 100001, 200010)
            .values(200007, "2014-02-10", TypeNotification.INSCRIT_A_ANNONCE, 4, 0, 200008, 100001, 200011).build();

    public static final Operation INSERT_ADRESSE_DATA = insertInto("adresse")
            .columns("id", "adresse", "codepostal", "complementadresse", "ville", "departement")
            .values(200005, "254 chemin du test", "06600", "Residence du test", "Test City", 06)
            .values(200006, "260 chemin des lol", "06500", "Residence des lol", "Test lol", 07)
            .values(200014, "254 chemin du test", "06600", "Residence du test", "Test City", 06)
            .values(200015, "270 chemin du mdr", "08800", "Residence du mdr", "Test mdr", 8)
            .values(200016, "280 chemin du lolmdrxD", "09800", "Residence du lolmdrxD", "Test lolmdrxD", 9).build();

    public static final Operation INSERT_AVIS_DATA = insertInto("avis")
            .columns("id", "commentaire", "dateavis", "score", "artisan_fk")
            .values(200012, "Ké buenos, Artisan très sympatique, travail bien fait", "2014-03-23 22:00:00.0", 4, 200008)
            .values(200013, "Artisan moins sympatique", "2014-12-01 22:00:00.0", 3, 200008).build();

    public static final Operation INSERT_ARTISAN_PERMISSION = insertInto("permission")
            .columns("typecompte", "artisan_fk").values(TypeCompte.ARTISAN, 200008).build();

    public static final Operation INSERT_ANNONCE_ARTISAN = insertInto("annonce_artisan")
            .columns("annonce_id", "artisans_id").values(200010, 200008).values(200011, 200008).values(200013, 200008)
            .build();

    public static final Operation INSERT_ANNONCE_IMAGE = insertInto("image")
            .columns("id", "url", "id_annonce")
            .values(10001,
                    "http://res.cloudinary.com/lescastors/image/upload/q_27/v1430908935/test/srwdbvzwlvhxoytsheha.jpg",
                    200011).build();
}
