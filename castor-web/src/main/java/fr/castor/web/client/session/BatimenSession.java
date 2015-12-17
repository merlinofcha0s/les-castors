package fr.castor.web.client.session;

import fr.castor.web.app.security.Authentication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Classe chargée d'authentifier l'utilisateur et de garder en mémoire les
 * informations de son compte.
 * 
 * @author Casaucau Cyril
 * 
 */
public class BatimenSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = -3460138748198816904L;

    @Inject
    private Authentication authentication;

    private Roles roles;
    private String username = null;

    public BatimenSession(Request request) {
        super(request);
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

    @Override
    public boolean authenticate(String username, String password) {
        boolean isAuthenticated = authentication.authenticate(username, password);
        if(isAuthenticated) {
            this.username = username;
        }
        return isAuthenticated;
    }

    @Override
    public Roles getRoles() {
        return null;
    }

}
