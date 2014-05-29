package fr.batimen.ws.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Classe d'appel au webservice concernant les annonces.
 * 
 * @author Casaucau Cyril
 * 
 */
public class AnnonceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceService.class);

	private AnnonceService() {

	}

	/**
	 * Appel le webservice pour creer l'annonce.
	 * 
	 * @param nouvelleAnnonce
	 *            l'objet a envoyé au webservice pour qu'il puisse créer
	 *            l'annonce.
	 * @return Constant.CODE_SERVICE_RETOUR_OK ou
	 *         Constant.CODE_SERVICE_RETOUR_KO
	 */
	public static Integer creationAnnonce(CreationAnnonceDTO nouvelleAnnonce) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Début appel service creation annonce.....");
		}

		String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_ANNONCE_SERVICE_PATH,
		        WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE, nouvelleAnnonce);

		Integer codeRetour = Integer.valueOf(objectInJSON);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin appel service creation annonce.....");
		}

		return codeRetour;

	}

}
