package fr.batimen.web.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.web.client.session.BatimenSession;
import fr.batimen.web.server.WsConnector;

/**
 * 
 * Sert à appeler les services de controle des utilisateurs du WS
 * 
 * @author Casaucau Cyril
 * 
 */
public class ClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

	private ClientService() {

	}

	/**
	 * Verification nom utilisateur / mdp
	 * 
	 * @param loginDTO
	 *            l'objet d'échange pour verifier les données.
	 * @return true si le couple login / mdp correspond.
	 */
	public static boolean login(LoginDTO loginDTO) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Début appel service login + deserialization");
		}

		String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.USER_SERVICE_PATH,
				WsPath.CLIENT_SERVICE_LOGIN, loginDTO);

		ClientDTO clientDTO = ClientDTO.deserializeUserDTO(objectInJSON);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin appel service login + deserialization");
		}

		// Si l'user dto est vide cela veut dire que l'authentification n'a pas
		// reussi
		if ("".equals(clientDTO.getLogin())) {
			return false;
		} else {
			// On enregistre les infos de l'utilisateur dans la session
			BatimenSession session = (BatimenSession) BatimenSession.get();
			session.putUserInSession(clientDTO);
			return true;
		}
	}
}
