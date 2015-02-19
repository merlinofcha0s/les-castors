package fr.batimen.core.constant;

public class CodeRetourService {

    private CodeRetourService() {

    }

    // Code retour service
    public static final Integer RETOUR_OK = 0;
    public static final Integer RETOUR_KO = 1;

    // Code retour propre aux annonces
    public static final Integer ANNONCE_RETOUR_DUPLICATE = 10;

    // Code Retour service propres aux client
    public static final Integer ANNONCE_RETOUR_DEJA_ACTIF = 10;
    public static final Integer ANNONCE_RETOUR_COMPTE_INEXISTANT = 11;

    // Service inscription annonce
    public static final Integer LIMIT_INSCRIPTION_ATTEINTE = 10;

}
