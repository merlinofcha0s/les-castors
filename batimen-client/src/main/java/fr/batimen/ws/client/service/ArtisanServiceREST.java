package fr.batimen.ws.client.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Classe d'appel du webservice permettant la gestion des artisans.
 * 
 * @author Casaucau Cyril
 * 
 */
@Named("artisanServiceREST")
public class ArtisanServiceREST implements Serializable {

    private static final long serialVersionUID = -1941230690072232687L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanServiceREST.class);

    @Inject
    private WsConnector wsConnector;

    /**
     * Crée un nouvel artisan / partenaire, son entreprise, etc
     * 
     * @param nouveauPartenaire
     *            DTO contenant toutes les informations données par
     *            l'utilisateur
     * @return Voir la classe {@link Constant} pour les retours possibles du
     *         service
     */
    public Integer creationNouveauPartenaire(CreationPartenaireDTO nouveauPartenaire) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service nouveau partenaire + deserialization");
        }

        String objectInJSON = wsConnector.sendRequest(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE, nouveauPartenaire);

        Integer codeRetour = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service nouveau partenaire + deserialization");
        }

        return codeRetour;
    }

}
