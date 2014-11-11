package fr.batimen.ws.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.aggregate.MesAnnoncesPageDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Classe d'appel au webservice concernant les clients.
 * 
 * @author Casaucau Cyril
 * 
 */
public class ClientsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientsService.class);

    private ClientsService() {

    }

    public static MesAnnoncesPageDTO getMesInfosAnnoncePage(String login) {
        MesAnnoncesPageDTO mesDevis = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation des données de la page mes devis");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
                WsPath.GESTION_CLIENT_SERVICE_INFOS_MES_ANNONCES, login);

        mesDevis = MesAnnoncesPageDTO.deserializeMesDevisDTO(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation des données de la page mes devis + deserialization");
        }

        return mesDevis;
    }

}
