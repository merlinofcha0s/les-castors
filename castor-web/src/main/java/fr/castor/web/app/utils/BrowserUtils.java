package fr.castor.web.app.utils;

import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;

/**
 * Created by Casaucau Cyril on 12/04/2015.
 *
 *
 * @author Cyril Casaucau
 */
public class BrowserUtils {

    private BrowserUtils(){

    }

    public static boolean isInternetExplorer(WebSession session) {
        ClientProperties clientProperties = session.getClientInfo().getProperties();
        return clientProperties.isBrowserInternetExplorer();
    }

}
