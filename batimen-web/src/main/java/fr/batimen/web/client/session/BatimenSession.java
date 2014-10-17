package fr.batimen.web.client.session;

import java.util.Locale;

import org.apache.wicket.protocol.http.WebSession;
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
public class BatimenSession extends WebSession {

    private static final long serialVersionUID = -3460138748198816904L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BatimenSession.class);

    private ClientDTO user;

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

}
