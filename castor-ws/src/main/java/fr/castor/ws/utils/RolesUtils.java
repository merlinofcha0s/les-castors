package fr.castor.ws.utils;

import java.io.Serializable;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.castor.dto.enums.TypeCompte;

/**
 * Classe utilitaire permettant de controler le type de role / compte de l'utlisateur
 *
 * @author Casaucau Cyril
 */
@Named
public class RolesUtils implements Serializable {

    private static final long serialVersionUID = 6271638697817865601L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesUtils.class);

    public Boolean checkIfAdmin(TypeCompte typeCompteToCheck) {
        return typeCompteToCheck.getRole().indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1;
    }

    public Boolean checkIfArtisan(TypeCompte typeCompteToCheck) {
        return typeCompteToCheck.getRole().indexOf(TypeCompte.ARTISAN.getRole()) != -1;
    }

    public Boolean checkIfClient(TypeCompte typeCompteToCheck) {
        return typeCompteToCheck.getRole().indexOf(TypeCompte.CLIENT.getRole()) != -1;
    }

    public Boolean checkIfAdminWithString(String roles) {
        return roles.indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1;
    }

    public Boolean checkIfClientWithString(String roles) {
        return roles.indexOf(TypeCompte.CLIENT.getRole()) != -1;
    }

    public Boolean checkIfArtisanWithString(String roles) {
        return roles.indexOf(TypeCompte.ARTISAN.getRole()) != -1;
    }

    public TypeCompte getTypeCompteByRole(String roles) {
        if (checkIfClientWithString(roles)) {
            return TypeCompte.CLIENT;
        } else if (checkIfArtisanWithString(roles)) {
            return TypeCompte.ARTISAN;
        } else if (checkIfAdminWithString(roles)) {
            return TypeCompte.ADMINISTRATEUR;
        } else {
            return TypeCompte.INCONNU;
        }
    }

}
