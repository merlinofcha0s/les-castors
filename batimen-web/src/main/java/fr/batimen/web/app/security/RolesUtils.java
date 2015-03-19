package fr.batimen.web.app.security;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.enums.TypeCompte;

public class RolesUtils implements Serializable {

    private static final long serialVersionUID = 6271638697817865601L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesUtils.class);

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

    public Boolean checkClientAndAdminRoles() {
        if (checkRoles(TypeCompte.CLIENT) || checkRoles(TypeCompte.ADMINISTRATEUR)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
