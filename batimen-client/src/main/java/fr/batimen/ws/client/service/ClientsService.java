package fr.batimen.ws.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.aggregate.MesAnnoncesDTO;
import fr.batimen.dto.aggregate.MonProfilDTO;
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

    public static MesAnnoncesDTO getMesInfosAnnonce(String login) {
        MesAnnoncesDTO mesDevis = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation des données de la page mes annonces");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
                WsPath.GESTION_CLIENT_SERVICE_INFOS_MES_ANNONCES, login);

        mesDevis = MesAnnoncesDTO.deserializeMesDevisDTO(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation des données de la page mes annonces + deserialization");
        }

        return mesDevis;
    }

    public static MonProfilDTO getMesInfosForMonProfil(String login) {
        MonProfilDTO monProfilDTO = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation des données de la page mon profil");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_CLIENT_SERVICE_PATH,
                WsPath.GESTION_CLIENT_SERVICE_INFOS_MON_PROFIL, login);

        monProfilDTO = MonProfilDTO.deserializeMonProfilDTO(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation des données de la page mon profil + deserialization");
        }

        return monProfilDTO;
    }

}
