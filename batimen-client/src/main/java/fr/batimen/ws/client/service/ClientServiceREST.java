package fr.batimen.ws.client.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.aggregate.MonProfilDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.client.WsConnector;

/**
 * Classe d'appel au webservice concernant les clients.
 * 
 * @author Casaucau Cyril
 * 
 */
@Named("clientServiceREST")
public class ClientServiceREST implements Serializable {

    private static final long serialVersionUID = -8930750826494454672L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceREST.class);

    @Inject
    private WsConnector wsConnector;


    public MonProfilDTO getMesInfosForMonProfil(String login) {
        MonProfilDTO monProfilDTO = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation des données de la page mon profil");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_CLIENT_SERVICE_PATH,
                WsPath.GESTION_CLIENT_SERVICE_INFOS_MON_PROFIL, login);

        monProfilDTO = DeserializeJsonHelper.deserializeDTO(objectInJSON, MonProfilDTO.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation des données de la page mon profil + deserialization");
        }

        return monProfilDTO;
    }

}
