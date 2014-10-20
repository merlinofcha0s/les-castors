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
    public static final String HASH_BY_LOGIN = "hashByLogin";
    public static final String CLIENT_BY_ACTIVATION_KEY = "clientByActivationKey";

    // Parametre Query Entity client
    public static final String PARAM_CLIENT_LOGIN = "login";
    public static final String PARAM_CLIENT_EMAIL = "email";
    public static final String PARAM_CLIENT_ACTIVATION_KEY = "cleActivation";

    // Entity Annonce
    public static final String ANNONCE_BY_LOGIN = "annonceByLogin";
    public static final String ANNONCE_BY_TITLE_AND_DESCRIPTION = "annonceByTitleAndDescription";

    // Parametre Query Entity annonce
    public static final String PARAM_ANNONCE_TITRE = "titre";
    public static final String PARAM_ANNONCE_DESCRIPTION = "description";

    // Entity artisan
    public static final String ARTISAN_BY_EMAIL = "artisanByEmail";

    // Parametre Query Entity artisan
    public static final String PARAM_ARTISAN_EMAIL = "email";

    // Entity entreprise
    public static final String ENTREPRISE_BY_SIRET = "entrepriseBySiret";

    // Parametre Query Entity entreprise
    public static final String PARAM_ENTEPRISE_SIRET = "siret";
}
