package fr.castor.web.client.session;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

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
        return true;
    }

    @Override
    public Roles getRoles() {
        return null;
    }

}
