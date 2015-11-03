package fr.castor.ws.client.service.commons;

import com.google.gson.reflect.TypeToken;
import fr.castor.dto.ImageDTO;
import fr.castor.dto.aggregate.AjoutPhotoDTO;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.ws.client.WsConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Classe permettant de mutiliser des services entre controlleurs REST
 *
 * @author Casaucau Cyril
 */
@Named("commonService")
public class CommonService implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    @Inject
    private WsConnector wsConnector;

    public List<ImageDTO> ajouterPhoto(AjoutPhotoDTO ajoutPhotoDTO, String controleur, String service) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service d'ajout de photo.....");
        }

        String objectInJSON = wsConnector.sendRequestWithFile(controleur,
                service, ajoutPhotoDTO.getImages(), ajoutPhotoDTO);

        TypeToken<List<ImageDTO>> tokenImage = new TypeToken<List<ImageDTO>>(){};
        List<ImageDTO> imageDTOs = DeserializeJsonHelper.deserializeDTOList(objectInJSON, tokenImage);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service d'ajout de photo.");
        }
        return imageDTOs;
    }
}