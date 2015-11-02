package fr.batimen.web.app.security;

import java.io.Serializable;
import java.util.List;

import fr.batimen.dto.PermissionDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.enums.TypeCompte;

/**
 * Classe utilitaire qui permet de vérifier le type de compte d'un utilisateur.
 *
 * @author Cyril Casaucau
 */
public class RolesUtils implements Serializable {

    private static final long serialVersionUID = 6271638697817865601L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesUtils.class);

    /**
     * Check si l'utilisateur connecté possede le role donné en parametre
     *
     * @param typeCompte Le type de compte que l'on veut tester
     * @return True si l'utilisateur possede le role
     * @see TypeCompte
     */
    public Boolean checkRoles(TypeCompte typeCompte) {
        try {
            SecurityUtils.getSubject().checkRoles(typeCompte.getRole());
            return Boolean.TRUE;
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(
                        SecurityUtils.getSubject().getPrincipal() + " ne possede pas le role : " + typeCompte.getRole(),
                        ae);
            }
            return Boolean.FALSE;
        }
    }

    /**
     * Check si c'est un client ou un admin
     *
     * @return true si c'est un client ou un admin
     */
    public Boolean checkClientAndAdminRoles() {
        if (checkRoles(TypeCompte.CLIENT) || checkRoles(TypeCompte.ADMINISTRATEUR)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
