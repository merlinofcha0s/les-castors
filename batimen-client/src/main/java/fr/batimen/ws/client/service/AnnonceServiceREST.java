package fr.batimen.ws.client.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.AnnonceSelectEntrepriseDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.aggregate.DesinscriptionAnnonceDTO;
import fr.batimen.dto.aggregate.NbConsultationDTO;
import fr.batimen.ws.client.WsConnector;

/**
 * Classe d'appel au webservice concernant les annonces.
 * 
 * @author Casaucau Cyril
 * 
 */
@Named("annonceServiceREST")
public class AnnonceServiceREST implements Serializable {

    private static final long serialVersionUID = -7223646076633068466L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceServiceREST.class);

    @Inject
    private transient WsConnector wsConnector;

    /**
     * Appel le webservice pour creer l'annonce.
     * 
     * @param nouvelleAnnonce
     *            l'objet a envoyé au webservice pour qu'il puisse créer
     *            l'annonce.
     * @return Constant.CODE_SERVICE_RETOUR_OK ou
     *         Constant.CODE_SERVICE_RETOUR_KO
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
     * 
     * @param nouvelleAnnonce
     *            l'objet a envoyé au webservice pour qu'il puisse créer
     *            l'annonce.
     * @return Constant.CODE_SERVICE_RETOUR_OK ou
     *         Constant.CODE_SERVICE_RETOUR_KO
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
     * Appel le webservice pour recuperer les annonces par login client.
     * 
     * @param login
     *            L'identifiant du client
     * @return
     */
    public List<AnnonceDTO> getAnnonceByLoginForClient(String login) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_LOGIN, login);

        List<AnnonceDTO> annonces = AnnonceDTO.deserializeAnnonceDTOList(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return annonces;
    }

    /**
     * Permet de récuperer une annonce dans le but de l'afficher <br/>
     * Récupère également les informations sur les artisans et les entreprise
     * inscrites a cette annonce
     * 
     * @param demandeAnnonce
     *            le hashID avec le login du demandeur dans le but de vérifier
     *            les droits.
     * @return l'ensemble des informations qui permettent d'afficher l'annonce
     *         correctement
     */
    public AnnonceAffichageDTO getAnnonceByIDForAffichage(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = wsConnector.sendRequestJSON(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_ID, demandeAnnonceDTO);

        AnnonceAffichageDTO annonce = AnnonceAffichageDTO.deserializeAnnonceAffichageDTO(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return annonce;
    }

    /**
     * Permet de mettre à jour le nombre de consultation d'une annonce.
     * 
     * @param nbConsultationDTO
     *            le hashID avec le nb de consultation
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
     * @param demandeAnnonce
     *            le hashID avec le login du demandeur dans le but de vérifier
     *            les droits.
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
     * @param demandeAnnonce
     *            le hashID avec le login du demandeur dans le but de vérifier
     *            les droits ainsi que le siret de l'entreprise.
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
     * @param demandeAnnonce
     *            le hashID avec le login du demandeur dans le but de vérifier
     *            les droits ainsi que le siret de l'entreprise.
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
     * Permet de selectionner un entreprise pour une annonce.
     * 
     * @param demandeAnnonce
     *            le hashID avec le login du demandeur dans le but de vérifier
     *            les droits ainsi que le siret de l'entreprise.
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

}