package fr.batimen.web.client.session;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.client.service.ClientService;

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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Préparation appel ws pour : " + username + "et" + password);
		}
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setLogin(username);

		ClientDTO clientDTO = ClientService.login(loginDTO);

		// Si l'user dto est vide cela veut dire que l'authentification n'a pas
		// reussi
		if ("".equals(clientDTO.getLogin())) {
			return false;
		} else {
			// Vérification du password avec le hash qui se trouve dans la bdd
			boolean passwordMatch = HashHelper.check(password, clientDTO.getPassword());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Verification du password : " + passwordMatch);
			}

			if (passwordMatch) {
				// On enregistre les infos de l'utilisateur dans la session
				BatimenSession session = (BatimenSession) BatimenSession.get();
				session.putUserInSession(clientDTO);
				return true;
			} else {
				return false;
			}

		}
	}

	@Override
	public Roles getRoles() {
		return null;
	}

}
