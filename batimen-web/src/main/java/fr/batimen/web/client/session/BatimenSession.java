package fr.batimen.web.client.session;

import java.util.Locale;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;

/**
 * Classe chargée d'authentifier l'utilisateur et de garder en mémoire les
 * informations de son compte.
 * 
 * @author Casaucau Cyril
 * 
 */
public class BatimenSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = -3460138748198816904L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BatimenSession.class);

    private ClientDTO user;

    public BatimenSession(Request request) {
        super(request);
    }

    public ClientDTO getSessionUser() {
        return user;
    }

    public void putUserInSession(ClientDTO user) {
        this.user = user;
    }

    @Override
    public boolean authenticate(String username, String password) {

        Boolean isOk = null;

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);

        Subject currentUser = SecurityUtils.getSubject();

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

        return isOk;
    }

    @Override
    public Roles getRoles() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Session#getLocale()
     */
    @Override
    public Locale getLocale() {
        return Locale.FRENCH;
    }

}
