package fr.batimen.ws.client.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Connecteur pour appeler le service de contact par mail
 * 
 * @author Adnane
 * 
 */
@Named("contactUsServiceREST")
public class ContactUsServiceREST implements Serializable {

    private static final long serialVersionUID = 8455033700958602147L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsServiceREST.class);

    @Inject
    private WsConnector wsConnector;

    /**
     * Webservice Client, in charge of pushing a contact email to the mail WS
     * 
     * @param contactMail
     * @return server operation code
     */
    public int pushContactMail(ContactMailDTO contactMail) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service push contact mail");
        }

        String jsonResponse = wsConnector.sendRequestJSON(WsPath.MAIL_SERVICE_PATH, WsPath.MAIL_SERVICE_SEND_CONTACT_MAIL,
                contactMail);

        int serverResponse = Integer.valueOf(jsonResponse);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service d'envoi email contact");
            LOGGER.debug("Server response : {}", serverResponse);
        }

        return serverResponse;
    }
}
