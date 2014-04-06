package fr.batimen.ws.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.client.WsConnector;

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
	public static ClientDTO login(LoginDTO loginDTO) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Début appel service login + deserialization");
		}

		String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
		        WsPath.GESTION_CLIENT_SERVICE_LOGIN, loginDTO);

		ClientDTO clientDTO = ClientDTO.deserializeUserDTO(objectInJSON);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin appel service login + deserialization");
		}

		return clientDTO;
	}

	public static ClientDTO getClientByEmail(String email) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Début appel service de recuperation client par email + deserialization");
		}

		String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
		        WsPath.GESTION_CLIENT_SERVICE_BY_EMAIL, email);

		ClientDTO clientDTO = ClientDTO.deserializeUserDTO(objectInJSON);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin appel service service de recuperation client par email + deserialization");
		}

		return clientDTO;
	}
}
