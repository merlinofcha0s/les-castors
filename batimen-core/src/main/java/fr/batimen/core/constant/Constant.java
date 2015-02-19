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

    public static final Integer NB_MAX_ARTISAN_PAR_ANNONCE = 5;
    public static final Integer NB_MAX_ARTISAN_APRES_REINIT_PAR_ANNONCE = 10;
    public static final Integer TEMPS_PEREMPTION_ANNONCE = 90;

    // Roles accés WS (Glassfish AUTH)
    public static final String USERS_ROLE = "users";
    public static final String ADMIN_ROLE = "admins";

    // Accés User WS (Glassfish AUTH)
    public static final String BATIMEN_USERS_WS = "batimenuser";
    public static final String BATIMEN_PWD_WS = "Lolmdr06";

    // Timeout de connection au WS
    public static final int CONNECT_TIMEOUT = 10000;

}
