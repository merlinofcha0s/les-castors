package fr.batimen.core.constant;

/**
 * Contient toutes les constantes comunes aux deux application frontend /
 * backend de batimen.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Constant {

	private Constant() {

	}

	// Code retour service
	public static final Integer CODE_SERVICE_RETOUR_OK = 0;
	public static final Integer CODE_SERVICE_RETOUR_KO = 1;

	// Roles accés WS
	public static final String USERS_ROLE = "users";
	public static final String ADMIN_ROLE = "admins";

	// Accés User WS
	public static final String BATIMEN_USERS_WS = "batimenuser";
	public static final String BATIMEN_PWD_WS = "Lolmdr06";

	// Timeout de connection au WS
	public static final int CONNECT_TIMEOUT = 10000;

	// Nom des pages web
	public static final String ACCUEIL_URL = "/accueil";
	public static final String AUTHENTIFICATION_URL = "/connexion";
	public static final String MON_COMPTE_URL = "/moncompte";
	public static final String QUI_SOMMES_NOUS_URL = "/quisommesnous";
	public static final String CONTACT_URL = "/contact";
	public static final String NOUVEAU_DEVIS_URL = "/nouveaudevis";

}
