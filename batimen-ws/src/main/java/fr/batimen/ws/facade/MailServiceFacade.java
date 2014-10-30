package fr.batimen.ws.facade;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.EmailException;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.EmailService;

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
	 * Methode de login des utilisateurs
	 * 
	 * @param LoginDTO
	 *            loginDTO objet permettant l'authentification
	 * @return vide si la combinaison login / mdp ne corresponds pas ou si
	 *         inexistant
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
				return Constant.CODE_SERVICE_RETOUR_KO;
			}
		} catch (EmailException | IOException | MandrillApiError e) {
			LOGGER.error("Error while sending contact or aknowledgement email, contact mail sent? :" + success, e);
		}
		if (success)	return Constant.CODE_SERVICE_RETOUR_OK;
		return Constant.CODE_SERVICE_RETOUR_KO;
	}
}
