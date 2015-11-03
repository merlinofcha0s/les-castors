package fr.castor.ws.client.service;

import com.google.gson.reflect.TypeToken;
import fr.castor.core.constant.CodeRetourService;
import fr.castor.core.constant.WsPath;
import fr.castor.dto.DemandeAnnonceDTO;
import fr.castor.dto.ImageDTO;
import fr.castor.dto.aggregate.*;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.ws.client.WsConnector;
import fr.castor.ws.client.service.commons.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Classe d'appel au webservice concernant les annonces.
 *
 * @author Casaucau Cyril
 */
@Named("annonceServiceREST")
public class AnnonceServiceREST implements Serializable {

    private static final long serialVersionUID = -7223646076633068466L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceServiceREST.class);

    @Inject
    private WsConnector wsConnector;

    @Inject
    private CommonService commonsService;

    /**
     * Appel le webservice pour creer l'annonce.
     *
     * @param nouvelleAnnonce l'objet a envoyé au webservice pour qu'il puisse créer
     *                        l'annonce.
     * @return Constant.CODE_SERVICE_RETOUR_OK ou
     * Constant.CODE_SERVICE_RETOUR_KO
     */
    public Integer creationAnnonce(CreationAnnonceDTO nouvelleAnnonce) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE, nouvelleAnnonce);

        Integer codeRetour = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return codeRetour;
    }

    /**
     * Appel le webservice pour creer l'annonce.
     * <p>
     * Contient des images, l'appel au web service est fait en mode multipart
     *
     * @param nouvelleAnnonce l'objet a envoyé au webservice pour qu'il puisse créer
     *                        l'annonce.
     * @return Constant.CODE_SERVICE_RETOUR_OK ou
     * Constant.CODE_SERVICE_RETOUR_KO
     */
    public Integer creationAnnonceAvecImage(CreationAnnonceDTO nouvelleAnnonce) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestWithFile(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE_AVEC_IMAGES, nouvelleAnnonce.getPhotos(),
                nouvelleAnnonce);

        Integer codeRetour = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return codeRetour;
    }

    /**
     * Permet de récuperer une annonce dans le but de l'afficher 
     * Récupère également les informations sur les artisans et les entreprise
     * inscrites a cette annonce
     *
     * @param demandeAnnonceDTO le hashID avec le login du demandeur dans le but de vérifier
     *                          les droits.
     * @return l'ensemble des informations qui permettent d'afficher l'annonce
     * correctement
     */
    public AnnonceAffichageDTO getAnnonceByIDForAffichage(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_ID, demandeAnnonceDTO);

        AnnonceAffichageDTO annonce = DeserializeJsonHelper.deserializeDTO(objectInJSON, AnnonceAffichageDTO.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return annonce;
    }

    /**
     * Permet de mettre à jour le nombre de consultation d'une annonce.
     *
     * @param nbConsultationDTO le hashID avec le nb de consultation
     * @return 0 si c'est OK
     */
    public Integer updateNbConsultationAnnonce(NbConsultationDTO nbConsultationDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service update nb consultation.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_UPDATE_NB_CONSULTATION, nbConsultationDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service update nb consulations.....");
        }

        return updateOK;
    }

    /**
     * Permet de supprimer une annonce.
     *
     * @param demandeAnnonceDTO le hashID avec le login du demandeur dans le but de vérifier
     *                          les droits.
     * @return 0 si c'est OK
     */
    public Integer suppressionAnnonce(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service suppression annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_SUPRESS_ANNONCE, demandeAnnonceDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service suppression annonce.....");
        }

        return updateOK;
    }

    /**
     * Permet de selectionner un entreprise pour une annonce.
     *
     * @param demandeAnnonceDTO le hashID avec le login du demandeur dans le but de vérifier
     *                          les droits ainsi que le siret de l'entreprise.
     * @return 0 si c'est OK
     */
    public Integer selectOneEnterprise(AnnonceSelectEntrepriseDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service selection d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_SELECTION_UNE_ENTREPRISE, demandeAnnonceDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service selection d'une annonce.....");
        }

        return updateOK;
    }

    /**
     * Permet de selectionner un entreprise pour une annonce.
     *
     * @param demandeAnnonceDTO le hashID avec le login du demandeur dans le but de vérifier
     *                          les droits ainsi que le siret de l'entreprise.
     * @return 0 si c'est OK
     */
    public Integer inscriptionUnArtisan(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service selection d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_INSCRIPTION_UN_ARTISAN, demandeAnnonceDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service selection d'une annonce.....");
        }

        return updateOK;
    }

    /**
     * Permet de déselectionner une entreprise pour une annonce.
     *
     * @param desinscriptionAnnonceDTO le hashID avec le login du demandeur dans le but de vérifier
     *                                 les droits ainsi que le siret de l'entreprise.
     * @return 0 si c'est OK
     */
    public Integer desinscriptionArtisan(DesinscriptionAnnonceDTO desinscriptionAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de desinscription d'un artisan.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_DESINSCRIPTION_UN_ARTISAN, desinscriptionAnnonceDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de desinscription d'un artisan.");
        }

        return updateOK;
    }

    /**
     * Service qui permet à un client de noter un artisan
     * <p>
     * Fais passer l'annonce en mode terminer
     *
     * @param noterArtisanDTO Objet permettant de valider la note de l'artisan
     * @return {@link CodeRetourService}
     */
    public Integer noterUnArtisan(AvisArtisanDTO noterArtisanDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de notation d'un artisan.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_NOTER_UN_ARTISAN, noterArtisanDTO);

        Integer notationOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de notation d'un artisan.");
        }
        return notationOK;
    }

    /**
     * Service qui permet à un client de pouvoir modifier son annonce
     * <p>
     * Génére une notification à destination des artisans inscrits
     *
     * @param modificationAnnonceDTO Objet permettant de récuperer les informations qui ont été modifiée par le client
     * @return {@link CodeRetourService}
     */
    public Integer modifierAnnonce(ModificationAnnonceDTO modificationAnnonceDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de modification d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_MODIFICATION_ANNONCE, modificationAnnonceDTO);

        Integer modificationOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de modification d'une annonce.");
        }
        return modificationOK;
    }

    /**
     * Service qui permet à un client de pouvoir ajouter / rajouter des photos à son annonce
     * <p>
     * Génére une notification à destination des artisans inscrits
     *
     * @param ajoutPhotoDTO Objet permettant de récuperer les photos et informations qui ont été transmise par le client
     * @return {@link CodeRetourService}
     */
    public List<ImageDTO> ajouterPhoto(AjoutPhotoDTO ajoutPhotoDTO) {
        return commonsService.ajouterPhoto(ajoutPhotoDTO, WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_AJOUT_PHOTO);
    }

    public List<ImageDTO> getPhotos(DemandeAnnonceDTO demandeAnnonceDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recuperation des photos d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_RECUPERATION_PHOTO, demandeAnnonceDTO);

        TypeToken<List<ImageDTO>> tokenImage = new TypeToken<List<ImageDTO>>() {
        };
        List<ImageDTO> imageDTOs = DeserializeJsonHelper.deserializeDTOList(objectInJSON, tokenImage);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation des photos d'une annonce.");
        }
        return imageDTOs;
    }

    public List<ImageDTO> suppressionPhoto(SuppressionPhotoDTO suppressionPhotoDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de suppression d'une photo d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_SUPPRESSION_PHOTO, suppressionPhotoDTO);

        TypeToken<List<ImageDTO>> tokenImage = new TypeToken<List<ImageDTO>>() {
        };
        List<ImageDTO> imageDTOs = DeserializeJsonHelper.deserializeDTOList(objectInJSON, tokenImage);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recuperation d'une photo d'une annonce.");
        }
        return imageDTOs;
    }

    /**
     * Service de recherche d'annonce pour les artisans.
     *
     * @param searchAnnonceDTOIn Objet contenant les criteres de recherche de l'artisan
     * @return La liste d'annonces correspondantent.
     */
    public SearchAnnonceDTOOut searchAnnonce(SearchAnnonceDTOIn searchAnnonceDTOIn) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service de recherche d'une annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_RECHERCHE, searchAnnonceDTOIn);

        SearchAnnonceDTOOut searchAnnonceDTOOut = DeserializeJsonHelper.deserializeDTO(objectInJSON, SearchAnnonceDTOOut.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service de recherche d'une annonce.");
        }
        return searchAnnonceDTOOut;
    }
}