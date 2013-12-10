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

	// Roles accés WS
	public static final String USERS_ROLE = "users";
	public static final String ADMIN_ROLE = "admins";

	// Accés User WS
	public static final String BATIMEN_USERS_WS = "batimenuser";
	public static final String BATIMEN_PWD_WS = "Lolmdr06";

	// Timeout de connection au WS
	public static final int CONNECT_TIMEOUT = 10000;

}
