package fr.batimen.ws.client.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.aggregate.NbConsultationDTO;
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
    public static AnnonceAffichageDTO getAnnonceByIDForAffichage(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service creation annonce.....");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_ID, demandeAnnonceDTO);

        AnnonceAffichageDTO annonce = AnnonceAffichageDTO.deserializeAnnonceAffichageDTO(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service creation annonce.....");
        }

        return annonce;
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
    public static Integer updateNbConsultationAnnonce(NbConsultationDTO nbConsultationDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service update nb consultation.....");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_UPDATE_NB_CONSULTATION, nbConsultationDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service update nb consulations.....");
        }

        return updateOK;
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
    public static Integer suppressionAnnonce(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début appel service suppression annonce.....");
        }

        String objectInJSON = WsConnector.getInstance().sendRequest(WsPath.GESTION_ANNONCE_SERVICE_PATH,
                WsPath.GESTION_ANNONCE_SERVICE_SUPRESS_ANNONCE, demandeAnnonceDTO);

        Integer updateOK = Integer.valueOf(objectInJSON);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin appel service suppression annonce.....");
        }

        return updateOK;
    }

}