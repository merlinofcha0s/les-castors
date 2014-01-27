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
	public static final int LOGIN_RANGE_MIN = 3;
	public static final int LOGIN_RANGE_MAX = 25;
	public static final int PASSWORD_RANGE_MIN = 8;
	public static final int PASSWORD_RANGE_MAX = 40;

	// Creation annonce DTO
	public static final int CREATION_ANNONCE_TITRE_MIN = 3;
	public static final int CREATION_ANNONCE_TITRE_MAX = 45;
	public static final int CREATION_ANNONCE_DESCRIPTION_MIN = 3;
	public static final int CREATION_ANNONCE_DESCRIPTION_MAX = 500;
	public static final int CREATION_ANNONCE_DELAI_INTERVENTION_MAX = 15;
	public static final int CREATION_ANNONCE_NBDEVIS_MIN = 1;
	public static final int CREATION_ANNONCE_NBDEVIS_MAX = 10;
	public static final int CREATION_ANNONCE_ADRESSE_MIN = 3;
	public static final int CREATION_ANNONCE_ADRESSE_MAX = 255;
	public static final int CREATION_ANNONCE_COMPLEMENT_ADRESSE_MAX = 255;
	public static final int CREATION_ANNONCE_CODEPOSTAL_MAX = 5;
	public static final int CREATION_ANNONCE_VILLE_MAX = 45;
	public static final int CREATION_ANNONCE_NOM_MIN = 3;
	public static final int CREATION_ANNONCE_NOM_MAX = 20;
	public static final int CREATION_ANNONCE_PRENOM_MIN = 3;
	public static final int CREATION_ANNONCE_PRENOM_MAX = 20;
	// Regex
	public static final String CREATION_ANNONCE_CODE_POSTAL_REGEX = "^[0-9]{5}$";
	public static final String CREATION_ANNONCE_TELEPHONE_REGEX = "^0[1-9]([-. ]?[0-9]{2}){4}$";

}
