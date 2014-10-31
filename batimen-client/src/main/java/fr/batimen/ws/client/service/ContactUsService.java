package fr.batimen.ws.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Connecteur pour appeler le service de contact par mail
 * @author Adnane
 *
 */
public class ContactUsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsService.class);

    private ContactUsService() {

    }
    
    /**
     * 
     * @param contactMail
     * @return server operation code
     */
    public static int pushContactMail(ContactMailDTO contactMail){
    	if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service push contact mail");
        }

        String jsonResponse = WsConnector.getInstance().sendRequest(WsPath.MAIL_SERVICE_PATH,
                WsPath.MAIL_SERVICE_SEND_CONTACT_MAIL, contactMail);
        
        int serverResponse = Integer.valueOf(jsonResponse);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service d'envoi email contact");
            LOGGER.debug("Server response : {}",serverResponse);
        }
        
		return serverResponse;
    	
    }

    public static int activateAccount(String cleActivation) {

        int resultatService;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation client par email + deserialization");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
                WsPath.GESTION_UTILISATEUR_SERVICE_ACTIVATION, cleActivation);

        resultatService = Integer.parseInt(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service service de recuperation client par email + deserialization");
        }

        return resultatService;
    }
}
