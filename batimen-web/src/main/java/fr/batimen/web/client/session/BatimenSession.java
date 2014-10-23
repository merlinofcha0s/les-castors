package fr.batimen.web.client.session;

import java.util.Locale;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // TODO Auto-generated method stub
        return null;
    }

}
