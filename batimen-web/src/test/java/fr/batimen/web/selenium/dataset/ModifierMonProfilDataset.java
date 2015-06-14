package fr.batimen.web.selenium.dataset;

import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.dto.helper.CategorieLoader;

import static com.ninja_squad.dbsetup.Operations.insertInto;

/**
 * Created by Casaucau on 25/05/2015.
 */
public class ModifierMonProfilDataset {

    public static final Operation INSERT_ANNONCE_DATA = insertInto("annonce")
            .columns("id", "datecreation", "datemaj", "delaiintervention", "description", "etatannonce",
                    "categoriemetier", "souscategoriemetier", "nbconsultation", "typecontact", "hashID", "selHashID",
                    "typeTravaux", "adressechantier_id", "demandeur_fk", "entreprise_selectionnee_fk",
                    "notationannonce_id")
            .values(200010, "2014-01-10", "2014-01-10", "0",
                    "Construction compliqué qui necessite des connaissance en geologie", "0", 0,
                    "Installation électrique", 0, 0, "toto", "tata", 0, 200005, 100001, 200009, 200012)
            .values(200011, "2014-01-10", "2014-01-10", "0",
                    "Construction compliqué qui necessite des connaissance en geologie", "0", 0,
                    "Installation électrique", 0, 0, "toto", "tata", 0, 200014, 100001, 200009, 200013).build();

    public static final Operation INSERT_ARTISAN_DATA = insertInto("artisan")
            .columns("civilite", "email", "nom", "prenom", "login", "password", "id", "numeroTel", "dateInscription",
                    "isActive", "cleActivation", "entreprise_id")
            .values(0, "pebronArtisan@batimen.fr", "PebronArtisan", "Toto", "pebron",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", 200008,
                    "0614125698", "2014-01-10", true, "lolmdr07", 200009).build();

    public static final Operation INSERT_ENTREPRISE_DATA = insertInto("entreprise")
            .columns("id", "nomcomplet", "statutjuridique", "siret", "datecreation", "adresse_id", "isverifier")
            .values(200009, "Entreprise de toto", 0, "43394298400017", "2014-03-23", 200005, true).build();

    public static final Operation INSERT_NOTIFICATION_DATA = insertInto("notification")
            .columns("id", "dateNotification", "typeNotification", "pourquinotification", "statutnotification",
                    "id_artisan", "id_client", "id_annonce")
            .values(200006, "2014-02-10", 0, 4, 0, 200008, 100001, 200010)
            .values(200007, "2014-02-10", TypeNotification.A_CHOISI_ENTREPRISE, TypeCompte.ARTISAN, StatutNotification.VU, 200008, 100001, 200011).build();

    public static final Operation INSERT_ADRESSE_DATA = insertInto("adresse")
            .columns("id", "adresse", "codepostal", "complementadresse", "ville", "departement")
            .values(200005, "254 chemin du test", "06600", "Residence du test", "Test City", 06)
            .values(200014, "254 chemin du test", "06600", "Residence du test", "Test City", 06).build();

    public static final Operation INSERT_NOTATION_DATA = insertInto("notation")
            .columns("id", "commentaire", "dateNotation", "score", "artisan_fk")
            .values(200012, "Ké buenos, Artisan très sympatique, travail bien fait", "2014-03-23 22:00:00.0", 4, 200008)
            .values(200013, "Artisan moins sympatique", "2014-12-01 22:00:00.0", 3, 200008).build();

    public static final Operation INSERT_ARTISAN_PERMISSION = insertInto("permission").columns("typecompte", "artisan_fk")
            .values(TypeCompte.ARTISAN, 200008).build();
    public static final Operation INSERT_ANNONCE_ARTISAN = insertInto("annonce_artisan")
            .columns("annonce_id", "artisans_id").values(200011, 200008).build();

    public static final Operation INSERT_CATEGORIE_ENTREPRISE = insertInto("categorieMetier")
            .columns("id", "categorieMetier", "entreprise_fk").values(200000, CategorieLoader.ELECTRICITE_CODE, 200009).build();


}
