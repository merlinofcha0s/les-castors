package fr.castor.core.constant;

/**
 * Contient toutes les constantes comunes aux deux application frontend /
 * backend de batimen.
 *
 * @author Casaucau Cyril
 */
public class Constant {

    // Roles accés WS (Wildfly AUTH)
    public static final String USERS_ROLE = "users";
    // Timeout de connection au WS
    public static final int CONNECT_TIMEOUT = 10000;

    private Constant() {

    }
}