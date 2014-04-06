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

	// Parametre Query Entity client
	public static final String PARAM_CLIENT_LOGIN = "login";
	public static final String PARAM_CLIENT_EMAIL = "email";
	// Entity Annonce
	public static final String ANNONCE_BY_LOGIN = "annonceByLogin";
	public static final String ANNONCE_BY_TITLE_AND_DESCRIPTION = "annonceByTitleAndDescription";

	// Parametre Query Entity annonce
	public static final String PARAM_ANNONCE_TITRE = "titre";
	public static final String PARAM_ANNONCE_DESCRIPTION = "description";
}
