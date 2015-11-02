package fr.batimen.ws.utils;

/**
 * Created by Casaucau on 09/06/2015.
 */
public class ClientUtils {

    private ClientUtils(){

    }

    /**
     * Methode utilitaire permettant de choisir si on doit afficher le nom / prénom ou le login
     *
     * @param nom
     * @param prenom
     * @param login
     * @param nomDestinataire
     */
    public static void chooseNomClient(String nom, String prenom, String login, StringBuilder nomDestinataire) {
        if (nom != null && prenom != null && !nom.isEmpty() && !prenom.isEmpty()) {
            nomDestinataire.append(nom);
            nomDestinataire.append(" ");
            nomDestinataire.append(prenom);
        } else {
            nomDestinataire.append(login);
        }
    }
}
