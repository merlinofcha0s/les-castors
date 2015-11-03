package fr.castor.core.constant;

/**
 * Classe de constante pour les codes retour du webservice
 * 
 * @author Casaucau
 * 
 */
public class CodeRetourService {

    private CodeRetourService() {

    }

    // Code retour service
    public static final Integer RETOUR_OK = 0;
    public static final Integer RETOUR_KO = 1;

    // Code retour propre aux annonces
    public static final Integer ANNONCE_RETOUR_DUPLICATE = 10;
    public static final Integer ANNONCE_RETOUR_TROP_DE_PHOTOS = 15;

    // Inscription Annonce artisan
    public static final Integer ANNONCE_RETOUR_ARTISAN_DEJA_INSCRIT = 11;
    public static final Integer ANNONCE_RETOUR_QUOTA_DEVIS_ATTEINT = 12;
    public static final Integer ANNONCE_RETOUR_ARTISAN_INTROUVABLE = 13;
    public static final Integer ANNONCE_RETOUR_INTROUVABLE = 14;

    // Code Retour service propres aux clients
    public static final Integer ANNONCE_RETOUR_DEJA_ACTIF = 10;
    public static final Integer ANNONCE_RETOUR_COMPTE_INEXISTANT = 11;

}
