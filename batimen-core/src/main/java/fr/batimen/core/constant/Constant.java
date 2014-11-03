package fr.batimen.core.constant;

/**
 * Contient toutes les constantes comunes aux deux application frontend /
 * backend de batimen.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Constant {

    private Constant() {

    }

    // Code retour service
    public static final Integer CODE_SERVICE_RETOUR_OK = 0;
    public static final Integer CODE_SERVICE_RETOUR_KO = 1;

    // Code retour propre aux annonces
    public static final Integer CODE_SERVICE_ANNONCE_RETOUR_DUPLICATE = 10;

    // Code Retour service propres aux client
    public static final Integer CODE_SERVICE_ANNONCE_RETOUR_DEJA_ACTIF = 10;
    public static final Integer CODE_SERVICE_ANNONCE_RETOUR_COMPTE_INEXISTANT = 11;

    // Roles accés WS (Glassfish AUTH)
    public static final String USERS_ROLE = "users";
    public static final String ADMIN_ROLE = "admins";

    // Accés User WS (Glassfish AUTH)
    public static final String BATIMEN_USERS_WS = "batimenuser";
    public static final String BATIMEN_PWD_WS = "Lolmdr06";

    // Timeout de connection au WS
    public static final int CONNECT_TIMEOUT = 10000;

    // Nom des pages web
    public static final String ACCUEIL_URL = "/accueil";
    public static final String AUTHENTIFICATION_URL = "/connexion";
    public static final String MES_DEVIS_URL = "/mesdevis";
    public static final String QUI_SOMMES_NOUS_URL = "/quisommesnous";
    public static final String CONTACT_URL = "/contact";
    public static final String CGU_URL = "/cgu";
    public static final String NOUVEAU_DEVIS_URL = "/nouveaudevis";
    public static final String ACTIVATION_URL = "/activation";
    public static final String PARTENAIRE_URL = "/nouveaupartenaire";

    // Email
    public static final String EMAIL_CASTOR_NOTIF = "notifications@lescastors.fr";
    public static final String EMAIL_CASTOR_CONTACT = "contact@lescastors.fr";
    public static final String EMAIL_FROM_NAME = "Les Castors";
    public static final String EMAIL_SENT = "sent";
    public static final String EMAIL_QUEUED = "queued";
    public static final String EMAIL_REJECTED = "rejected";
    public static final String EMAIL_INVALID = "invalid";

    // Template Email
    public static final String TEMPLATE_CONFIRMATION_ANNONCE = "confirmation_creation_annonce";
    public static final String TEMPLATE_ACTIVATION_COMPTE = "activation_compte";
    public static final String TEMPLATE_EMAIL_CONTACT = "contact_email";
    public static final String TEMPLATE_ACCUSE_RECEPTION = "accuse_reception_contact";

    // Creation annonce tags Email
    public static final String TAG_EMAIL_USERNAME = "username";
    public static final String TAG_EMAIL_TITRE = "titre";
    public static final String TAG_EMAIL_METIER = "metier";
    public static final String TAG_EMAIL_SOUS_CATEGORIE_METIER = "souscategoriemetier";
    public static final String TAG_EMAIL_TYPE_CONTACT = "typecontact";
    public static final String TAG_EMAIL_DELAI_INTERVENTION = "delaiintervention";
    public static final String TAG_EMAIL_ACTIVATION_LINK = "lienactivation";

    // Contact Email tags
    public static final String TAG_EMAIL_CONTACT_NAME = "CONTACTERNAME";
    public static final String TAG_EMAIL_CONTACT_EMAIL = "CONTACTEREMAIL";
    public static final String TAG_EMAIL_CONTACT_SUBJECT = "MESSAGESUBJECT";
    public static final String TAG_EMAIL_CONTACT_MESSAGE = "MESSAGECONTENT";

}
