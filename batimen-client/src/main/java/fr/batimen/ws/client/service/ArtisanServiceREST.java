package fr.batimen.ws.client.service;

import com.google.gson.reflect.TypeToken;
import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.AvisDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AjoutPhotoDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.aggregate.SuppressionPhotoDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.client.WsConnector;
import fr.batimen.ws.client.service.commons.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Classe d'appel du webservice permettant la gestion des artisans.
 *
 * @author Casaucau Cyril
 */
@Named("artisanServiceREST")
public class ArtisanServiceREST implements Serializable {

    private static final long serialVersionUID = -1941230690072232687L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanServiceREST.class);

    @Inject
    private WsConnector wsConnector;

    @Inject
    private CommonService commonService;

    /**
     * Crée un nouvel artisan / partenaire, son entreprise, etc
     *
     * @param nouveauPartenaire DTO contenant toutes les informations données par
     *                          l'utilisateur
     * @return Voir la classe {@link Constant} pour les retours possibles du
     * service
     */
    public Integer creationNouveauPartenaire(CreationPartenaireDTO nouveauPartenaire) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service nouveau partenaire + deserialization");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE, nouveauPartenaire);

        Integer codeRetour = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service nouveau partenaire + deserialization");
        }

        return codeRetour;
    }

    public EntrepriseDTO getEntrepriseInformationByArtisanLogin(String login) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service entreprise information par artisan login");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_GET_ENTREPISE_INFORMATION_BY_LOGIN, login);

        EntrepriseDTO entrepriseDTO = DeserializeJsonHelper.deserializeDTO(objectInJSON, EntrepriseDTO.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service entreprise information par artisan login + deserialization");
        }

        return entrepriseDTO;
    }

    /**
     * Permet de récuperer les informations d'une entreprise (Infos + adresse)
     *
     * @param entrepriseDTO Les informations de l'entreprise que l'on doit modifier
     * @return CodeRetourService
     */
    public Integer saveEntrepriseInformation(EntrepriseDTO entrepriseDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service sauvegarde entreprise information + deserialization");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_SAVE_ENTREPRISE_INFORMATION, entrepriseDTO);

        Integer codeRetour = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service sauvegarde entreprise information + deserialization");
        }

        return codeRetour;
    }

    /**
     * Permet de récuperer les informations d'une entreprise (Infos + adresse)
     *
     * @param siret Le siret de l'entreprise
     * @return Les informations de l'entreprise.
     */
    public EntrepriseDTO getEntrepriseInformationBySiret(String siret) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service get entreprise information par siret + deserialization");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_GET_ENTREPISE_INFORMATION_BY_SIRET, siret);

        EntrepriseDTO entrepriseDTO = DeserializeJsonHelper.deserializeDTO(objectInJSON, EntrepriseDTO.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service entreprise information par siret + deserialization");
        }

        return entrepriseDTO;
    }

    /**
     * Permet de récuperer tous les avis d'une entreprise par son SIRETs
     *
     * @param siret Le siret de l'entreprise
     * @return Les informations de l'entreprise.
     */
    public List<AvisDTO> getEntrepriseNotationBySiret(String siret) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service entreprise notation par SIRET + deserialization");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_GET_NOTATION_BY_SIRET, siret);

        TypeToken<List<AvisDTO>> tokenAvis = new TypeToken<List<AvisDTO>>() {
        };
        List<AvisDTO> notationDTOs = DeserializeJsonHelper.deserializeDTOList(objectInJSON, tokenAvis);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service entreprise notation par SIRET + deserialization");
        }

        return notationDTOs;
    }


    /**
     * Service qui permet à un artisan de pouvoir ajouter / rajouter des photos de chantier témoin<br/>
     * <p/>
     * Mode multipart, en plus du JSON la request contient des photos.
     *
     * @param ajoutPhotoDTO Les photos a envoyer in da cloud
     *
     * @return La liste des images qui se trouve dans le backend.
     */
    public List<ImageDTO> ajouterPhotosChantierTemoin(AjoutPhotoDTO ajoutPhotoDTO) {
        return commonService.ajouterPhoto(ajoutPhotoDTO, WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_AJOUT_PHOTO_CHANTIER_TEMOIN);
    }

    public List<ImageDTO> suppressionPhotoChantierTemoin(SuppressionPhotoDTO suppressionPhotoDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de suppression d'une photo d'un chantier témoin.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_PARTENAIRE_SERVICE_PATH,
                WsPath.GESTION_PARTENAIRE_SERVICE_SUPPRESSION_PHOTO_CHANTIER_TEMOIN, suppressionPhotoDTO);

        TypeToken<List<ImageDTO>> tokenImage = new TypeToken<List<ImageDTO>>(){};
        List<ImageDTO> imageDTOs = DeserializeJsonHelper.deserializeDTOList(objectInJSON, tokenImage);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation d'une photo d'un chantier témoin.");
        }
        return imageDTOs;
    }
}