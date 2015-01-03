package fr.batimen.web.app.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.client.service.UtilisateurService;

public class Authentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Authentication.class);

    private final Subject currentUser = SecurityUtils.getSubject();

    private static final String CLIENT_KEY = "client";

    public Boolean authenticate(String username, String password) {
        Boolean isOk = null;

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);

        try {
            currentUser.login(token);
            isOk = Boolean.TRUE;
        } catch (UnknownAccountException uae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Compte inconnu", uae);
            }
            isOk = Boolean.FALSE;
        } catch (IncorrectCredentialsException ice) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Credential incorrect", ice);
            }
            isOk = Boolean.FALSE;
        } catch (LockedAccountException lae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Compte bloqué", lae);
            }
            isOk = Boolean.FALSE;
        } catch (ExcessiveAttemptsException eae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Trop d'essai de connexion", eae);
            }
            isOk = Boolean.FALSE;
        } catch (AuthenticationException ae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme avec l'authentification", ae);
            }
            isOk = Boolean.FALSE;
        }

        if (isOk) {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setLogin(username);
            AuthenticatedWebSession.get().authenticate(username, "");
            currentUser.getSession(true).setAttribute(CLIENT_KEY, UtilisateurService.login(loginDTO));
        }
        return isOk;
    }

    public ClientDTO getCurrentUserInfo() {
        return (ClientDTO) currentUser.getSession().getAttribute(CLIENT_KEY);
    }

}
