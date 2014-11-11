package fr.batimen.core.constant;

/**
 * Classe qui contient toutes les constantes qui permettent de construire les
 * URLS d'accés aux services du ws
 * 
 * @author Casaucau Cyril
 * 
 */
public class WsPath {

    // Path Gestion Utilisateur
    public static final String GESTION_UTILISATEUR_SERVICE_PATH = "gestionUtilisateur";
    public static final String GESTION_UTILISATEUR_SERVICE_LOGIN = "login";
    public static final String GESTION_UTILISATEUR_SERVICE_BY_EMAIL = "email";
    public static final String GESTION_UTILISATEUR_SERVICE_ACTIVATION = "activation";
    public static final String GESTION_UTILISATEUR_SERVICE_HASH = "hash";
    public static final String GESTION_UTILISATEUR_SERVICE_ROLES = "roles";
    public static final String GESTION_UTILISATEUR_SERVICE_NOTIFICATION = "getNotificationByLogin";

    // public static final String GESTION_CLIENT_SERVICE_PATH = "gestionClient";

    // Path Gestion Annonce
    public static final String GESTION_ANNONCE_SERVICE_PATH = "gestionAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_CREATION_ANNONCE = "creationAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_GET_ANNONCE_BY_LOGIN = "getAnnonceByLogin";

    // Path Gestion Partenaire
    public static final String GESTION_PARTENAIRE_SERVICE_PATH = "gestionPartenaire";
    public static final String GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE = "creationPartenaire";

    // Path Envoi mail de contact
    public static final String MAIL_SERVICE_PATH = "mailService";
    public static final String MAIL_SERVICE_SEND_CONTACT_MAIL = "sendContactMail";

    // Path Gestion client
    public static final String GESTION_CLIENT_SERVICE_PATH = "gestionClient";
    public static final String GESTION_CLIENT_SERVICE_INFOS_MES_ANNONCES = "getInfoForMesAnnoncesByLogin";
}
