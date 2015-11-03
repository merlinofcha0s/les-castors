package fr.castor.ws.facade;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import fr.castor.core.constant.CodeRetourService;
import fr.castor.core.constant.Constant;
import fr.castor.core.constant.WsPath;
import fr.castor.core.exception.EmailException;
import fr.castor.dto.ContactMailDTO;
import fr.castor.ws.helper.JsonHelper;
import fr.castor.ws.interceptor.BatimenInterceptor;
import fr.castor.ws.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

/**
 * Facade REST de gestion des clients
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "MailServiceFacade")
@LocalBean
@Path(WsPath.MAIL_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MailServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceFacade.class);

    @Inject
    private EmailService emailService;

    /**
     * Sends a contact mail to the team, and an acknowledgement mail to the
     * customer
     * 
     * @param mail
     *            object created by customer
     * @return if success : 0 else : 1
     */
    @POST
    @Path(WsPath.MAIL_SERVICE_SEND_CONTACT_MAIL)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int sendContactMail(ContactMailDTO mail) {
        boolean success = false;
        try {
            success = emailService.envoiEmailContact(mail);

            // si operation accomplie avec succes
            // envoi email accuse de reception
            if (success) {
                emailService.envoiEmailAccuseReception(mail);
            } else {
                return CodeRetourService.RETOUR_KO;
            }
        } catch (EmailException | IOException | MandrillApiError e) {
            LOGGER.error("Error while sending contact or aknowledgement email, contact mail sent? :" + success, e);
        }
        if (success) {
            return CodeRetourService.RETOUR_OK;
        }
        return CodeRetourService.RETOUR_KO;
    }
}
