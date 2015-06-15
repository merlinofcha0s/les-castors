package fr.batimen.dto.constant;

/**
 * Classes qui contient les constantes qui permettent la validations des dtos
 * 
 * @author Casaucau Cyril
 * 
 */
public class ValidatorConstant {

    private ValidatorConstant() {

    }

    // Login DTO
    public static final int CLIENT_LOGIN_RANGE_MIN = 3;
    public static final int CLIENT_LOGIN_RANGE_MAX = 25;
    public static final int PASSWORD_RANGE_MIN = 6;
    public static final int PASSWORD_RANGE_MAX = 81;

    // Creation annonce DTO
    public static final int CREATION_ANNONCE_TITRE_MIN = 3;
    public static final int CREATION_ANNONCE_TITRE_MAX = 45;
    public static final int ANNONCE_DESCRIPTION_MIN = 3;
    public static final int ANNONCE_DESCRIPTION_MAX = 500;
    public static final int CREATION_ANNONCE_DELAI_INTERVENTION_MAX = 15;
    public static final int CREATION_ANNONCE_NBDEVIS_MIN = 1;
    public static final int CREATION_ANNONCE_NBDEVIS_MAX = 10;
    // Adresse DTO
    public static final int ADRESSE_MIN = 3;
    public static final int ADRESSE_MAX = 255;
    public static final int COMPLEMENT_ADRESSE_MAX = 255;
    public static final int CODEPOSTAL_MAX = 5;
    public static final int VILLE_MAX = 45;
    public static final int DEPARTEMENT_MIN = 1;
    public static final int DEPARTEMENT_MAX = 100;
    // Client DTO
    public static final int CLIENT_NOM_MIN = 3;
    public static final int CLIENT_NOM_MAX = 20;
    public static final int CLIENT_PRENOM_MIN = 3;
    public static final int CLIENT_PRENOM_MAX = 20;

    // Regex
    public static final String CODE_POSTAL_REGEX = "^[0-9]{5}$";
    public static final String TELEPHONE_REGEX = "^0[1-9]([-. ]?[0-9]{2}){4}$";

    // Entreprise DTO
    public static final int ENTREPRISE_SPECIALITE_MIN = 3;
    public static final int ENTREPRISE_SPECIALITE_MAX = 25;
    public static final int ENTREPRISE_NOM_COMPLET_MIN = 3;
    public static final int ENTREPRISE_NOM_COMPLET_MAX = 26;
    public static final String ENTREPRISE_SIRET_REGEXP = "^([0-9]{14})$";

    // AvisDTO
    public static final int NOTATION_MIN_COMMENTAIRE = 2;
    public static final int NOTATION_MAX_COMMENTAIRE = 500;

}
