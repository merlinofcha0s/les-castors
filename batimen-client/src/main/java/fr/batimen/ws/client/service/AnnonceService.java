package fr.batimen.ws.client.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
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

    /**
     * Appel le webservice pour recuperer les annonces par login client.
     * 
     * @param login
     *            L'identifiant du client
     * @return
     */
    public static List<AnnonceDTO> getAnnonceByLoginForClient(String login) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCE_BY_LOGIN, login);

        List<AnnonceDTO> annonces = AnnonceDTO.deserializeAnnonceDTOList(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return annonces;
    }

}