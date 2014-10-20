package fr.batimen.core.constant;

/**
 * Classe qui contient toutes les constantes qui permettent de construire les
 * URLS d'accés aux services du ws
 * 
 * @author Casaucau Cyril
 * 
 */
public class WsPath {

    private WsPath() {

    }

    // Path Gestion Client
    public static final String GESTION_CLIENT_SERVICE_PATH = "gestionClient";
    public static final String GESTION_CLIENT_SERVICE_LOGIN = "login";
    public static final String GESTION_CLIENT_SERVICE_BY_EMAIL = "email";
    public static final String GESTION_CLIENT_SERVICE_ACTIVATION = "activation";
    public static final String GESTION_CLIENT_SERVICE_HASH = "hash";

    // Path Gestion Annonce
    public static final String GESTION_ANNONCE_SERVICE_PATH = "gestionAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_CREATION_ANNONCE = "creationAnnonce";

    // Path Gestion Partenaire
    public static final String GESTION_PARTENAIRE_SERVICE_PATH = "gestionPartenaire";
    public static final String GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE = "creationPartenaire";
}
