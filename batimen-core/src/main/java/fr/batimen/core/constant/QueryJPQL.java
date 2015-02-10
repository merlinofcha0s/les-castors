package fr.batimen.core.constant;

/**
 * Contient le nom des requetes JPQL dans le but d'aidé la partie WS
 * 
 * @author Casaucau Cyril
 * 
 */
public class QueryJPQL {

    private QueryJPQL() {

    }

    // Entity User
    public static final String CLIENT_LOGIN = "login";
    public static final String CLIENT_BY_EMAIL = "clientByLoginAndEmail";
    public static final String CLIENT_HASH_BY_LOGIN = "hashByLogin";
    public static final String CLIENT_STATUT_BY_LOGIN = "statutByLogin";
    public static final String CLIENT_BY_ACTIVATION_KEY = "clientByActivationKey";

    // Parametre Query Entity client
    public static final String PARAM_CLIENT_LOGIN = "login";
    public static final String PARAM_CLIENT_EMAIL = "email";
    public static final String PARAM_TYPE_COMPTE = "typeCompte";
    public static final String PARAM_ACTIVATION_KEY = "cleActivation";

    // Entity Annonce
    public static final String ANNONCE_BY_LOGIN = "annonceByLogin";
    public static final String ANNONCE_BY_TITLE_AND_DESCRIPTION = "annonceByTitleAndDescription";
    public static final String ANNONCE_BY_LOGIN_FETCH_ARTISAN = "annonceByLoginFetchArtisan";
    public static final String NB_ANNONCE_BY_LOGIN = "nbAnnonceByLogin";
    public static final String ANNONCE_BY_ID_FETCH_ARTISAN_ENTREPRISE_CLIENT_ADRESSE = "annonceByIDFetchArtisanEntrepriseClientAdresse";
    public static final String ANNONCE_BY_ID = "annonceByID";
    public static final String ANNONCE_UPDATE_NB_CONSULTATION = "annonceUpdateNbConsultation";
    public static final String ANNONCE_SUPRESS_ANNONCE_FOR_CLIENT = "annonceSuppressionByClient";
    public static final String ANNONCE_SUPRESS_ANNONCE_FOR_ADMIN = "annonceSuppressionByAdmin";
    public static final String ANNONCE_SELECTION_ENTREPRISE_FOR_CLIENT = "annonceSelectionEntrepriseForClient";
    public static final String ANNONCE_SELECTION_ENTREPRISE_FOR_ADMIN = "annonceSelectionEntrepriseForAdmin";

    // Parametre Query Entity annonce
    public static final String PARAM_ANNONCE_TITRE = "titre";
    public static final String PARAM_ANNONCE_DESCRIPTION = "description";
    public static final String PARAM_ANNONCE_ID = "hashID";
    public static final String PARAM_ANNONCE_NB_CONSULTATION = "nbConsultation";

    // Entity artisan
    public static final String ARTISAN_BY_EMAIL = "artisanByEmail";
    public static final String ARTISAN_BY_LOGIN = "artisanByLogin";
    public static final String ARTISAN_HASH_BY_LOGIN = "hashForArtisan";
    public static final String ARTISAN_STATUT_BY_LOGIN = "statutForArtisan";
    public static final String ARTISAN_BY_ACTIVATION_KEY = "artisanByActivationKey";

    // Parametre Query Entity artisan
    public static final String PARAM_ARTISAN_EMAIL = "email";
    public static final String PARAM_ARTISAN_LOGIN_CHOISI = "artisanLoginChoisi";

    // Entity entreprise
    public static final String ENTREPRISE_BY_SIRET = "entrepriseBySiret";
    public static final String ENTREPRISE_BY_ARTISAN = "entrepriseByArtisan";

    // Parametre Query Entity entreprise
    public static final String PARAM_ENTEPRISE_SIRET = "siret";
    public static final String PARAM_ENTREPRISE_NOM_COMPLET = "entrepriseNomComplet";
    public static final String PARAM_ENTREPRISE_ENTITY = "entrepriseEntity";

    // Entity permission
    public static final String PERMISSION_CLIENT_BY_LOGIN = "permissionClientByLogin";
    public static final String PERMISSION_ARTISAN_BY_LOGIN = "permissionArtisanByLogin";

    // Entity notification
    public static final String NOTIFICATION_BY_CLIENT_LOGIN = "notificationClientByLogin";
    public static final String NOTIFICATION_BY_ARTISAN_LOGIN = "notificationArtisanByLogin";

    // Entity notation
    public static final String NOTATION_BY_CLIENT_LOGIN = "notationByClientLogin";

    // Entity adresse
    public static final String ADRESSE_BY_NOM_COMPLET_ENTREPRISE = "adresseByNomCompletEntreprise";

}
