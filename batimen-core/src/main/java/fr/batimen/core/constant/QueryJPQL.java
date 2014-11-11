package fr.batimen.core.constant;

/**
 * Contient le nom des requetes JPQL dans le but d'aidé la partie WS
 * 
 * @author Casaucau Cyril
 * 
 */
public class QueryJPQL {

    // Entity User
    public static final String CLIENT_LOGIN = "login";
    public static final String CLIENT_BY_EMAIL = "clientByLoginAndEmail";
    public static final String CLIENT_HASH_BY_LOGIN = "hashByLogin";
    public static final String CLIENT_STATUT_BY_LOGIN = "statutByLogin";
    public static final String CLIENT_BY_ACTIVATION_KEY = "clientByActivationKey";

    // Parametre Query Entity client
    public static final String PARAM_CLIENT_LOGIN = "login";
    public static final String PARAM_CLIENT_EMAIL = "email";
    public static final String PARAM_ACTIVATION_KEY = "cleActivation";

    // Entity Annonce
    public static final String ANNONCE_BY_LOGIN = "annonceByLogin";
    public static final String ANNONCE_BY_TITLE_AND_DESCRIPTION = "annonceByTitleAndDescription";
    public static final String ANNONCE_BY_LOGIN_FETCH_ARTISAN = "annonceByLoginFetchArtisan";

    // Parametre Query Entity annonce
    public static final String PARAM_ANNONCE_TITRE = "titre";
    public static final String PARAM_ANNONCE_DESCRIPTION = "description";

    // Entity artisan
    public static final String ARTISAN_BY_EMAIL = "artisanByEmail";
    public static final String ARTISAN_BY_LOGIN = "artisanByLogin";
    public static final String ARTISAN_HASH_BY_LOGIN = "hashForArtisan";
    public static final String ARTISAN_STATUT_BY_LOGIN = "statutForArtisan";
    public static final String ARTISAN_BY_ACTIVATION_KEY = "artisanByActivationKey";

    // Parametre Query Entity artisan
    public static final String PARAM_ARTISAN_EMAIL = "email";

    // Entity entreprise
    public static final String ENTREPRISE_BY_SIRET = "entrepriseBySiret";

    // Parametre Query Entity entreprise
    public static final String PARAM_ENTEPRISE_SIRET = "siret";

    // Entity permission
    public static final String PERMISSION_CLIENT_BY_LOGIN = "permissionClientByLogin";
    public static final String PERMISSION_ARTISAN_BY_LOGIN = "permissionArtisanByLogin";

}
