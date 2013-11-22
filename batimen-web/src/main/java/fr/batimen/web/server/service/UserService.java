package fr.batimen.web.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.UserDTO;
import fr.batimen.web.server.WsConnector;

/**
 * 
 * Sert à appeler les services de controle des utilisateurs du WS
 * 
 * @author Casaucau Cyril
 * 
 */
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * Verification nom utilisateur / mdp
	 * 
	 * @param loginDTO
	 *            l'objet d'échange pour verifier les données.
	 * @return true si le couple login / mdp correspond.
	 */
	public static boolean login(LoginDTO loginDTO) {

		if (logger.isDebugEnabled()) {
			logger.debug("Début appel service login + deserialization");
		}

		String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.USER_SERVICE_PATH,
				WsPath.USER_SERVICE_LOGIN, loginDTO);

		UserDTO userDTO = UserDTO.deserializeUserDTO(objectInJSON);

		if (logger.isDebugEnabled()) {
			logger.debug("Fin appel service login + deserialization");
		}

		if (userDTO.getLogin().equals("")) {
			return false;
		} else {
			// TODO Mettre en session l'userDTO
			return true;
		}
	}
}
