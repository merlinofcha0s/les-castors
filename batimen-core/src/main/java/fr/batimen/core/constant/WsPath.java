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

    // Path Gestion Utilisateur
    public static final String GESTION_UTILISATEUR_SERVICE_PATH = "gestionUtilisateur";
    public static final String GESTION_UTILISATEUR_SERVICE_LOGIN = "byLogin";
    public static final String GESTION_UTILISATEUR_SERVICE_BY_EMAIL = "byEmail";
    public static final String GESTION_UTILISATEUR_SERVICE_ACTIVATION = "activation";
    public static final String GESTION_UTILISATEUR_SERVICE_HASH = "byHash";
    public static final String GESTION_UTILISATEUR_SERVICE_ROLES = "getRoles";
    public static final String GESTION_UTILISATEUR_SERVICE_UPDATE_INFO = "update";
    public static final String GESTION_UTILISATEUR_SERVICE_INFOS_MES_ANNONCES = "getInfoForMesAnnoncesByLogin";

    // Path Gestion Annonce
    public static final String GESTION_ANNONCE_SERVICE_PATH = "gestionAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_CREATION_ANNONCE = "creationAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_CREATION_ANNONCE_AVEC_IMAGES = "creationAnnonceAvecImages";
    public static final String GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_LOGIN = "getAnnonceByLogin";
    public static final String GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_ID = "getAnnonceByID";
    public static final String GESTION_ANNONCE_SERVICE_UPDATE_NB_CONSULTATION = "updateNbConsultation";
    public static final String GESTION_ANNONCE_SERVICE_SUPRESS_ANNONCE = "suppressionAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_SELECTION_UNE_ENTREPRISE = "selectionUneEntreprise";
    public static final String GESTION_ANNONCE_SERVICE_INSCRIPTION_UN_ARTISAN = "inscriptionUnArtisan";
    public static final String GESTION_ANNONCE_SERVICE_DESINSCRIPTION_UN_ARTISAN = "desinscriptionUnArtisan";
    public static final String GESTION_ANNONCE_SERVICE_NOTER_UN_ARTISAN = "noterUnArtisan";
    public static final String GESTION_ANNONCE_SERVICE_MODIFICATION_ANNONCE = "modifierAnnonce";
    public static final String GESTION_ANNONCE_SERVICE_AJOUT_PHOTO = "ajoutPhoto";
    public static final String GESTION_ANNONCE_SERVICE_RECUPERATION_PHOTO = "getPhotos";
    public static final String GESTION_ANNONCE_SERVICE_SUPPRESSION_PHOTO = "supprimerPhoto";

    // Path Gestion Partenaire
    public static final String GESTION_PARTENAIRE_SERVICE_PATH = "gestionPartenaire";
    public static final String GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE = "creationPartenaire";
    public static final String GESTION_PARTENAIRE_SERVICE_GET_ENTREPISE_INFORMATION = "getEntrepriseInformation";

    // Path Envoi mail de contact
    public static final String MAIL_SERVICE_PATH = "mailService";
    public static final String MAIL_SERVICE_SEND_CONTACT_MAIL = "sendContactMail";

    // Path Gestion client
    public static final String GESTION_CLIENT_SERVICE_PATH = "gestionClient";
    public static final String GESTION_CLIENT_SERVICE_INFOS_MON_PROFIL = "getInfoForMonProfil";
}
