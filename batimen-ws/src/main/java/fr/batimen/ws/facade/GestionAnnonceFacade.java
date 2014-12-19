package fr.batimen.ws.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.core.exception.EmailException;
import fr.batimen.core.utils.PropertiesUtils;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.AnnonceService;
import fr.batimen.ws.service.EmailService;

/**
 * Facade REST de gestion des annonces.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionAnnonceFacade")
@LocalBean
@Path(WsPath.GESTION_ANNONCE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionAnnonceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionAnnonceFacade.class);

    @Inject
    private AnnonceDAO annonceDAO;

    @Inject
    private EmailService emailService;

    @Inject
    private AnnonceService annonceService;

    /**
     * Permet la creation d'une nouvelle annonce par le client ainsi que le
     * compte de ce dernier
     * 
     * @see Constant
     * 
     * @param nouvelleAnnonceDTO
     *            L'objet provenant du frontend qui permet la creation de
     *            l'annonce.
     * @return CODE_SERVICE_RETOUR_KO ou CODE_SERVICE_RETOUR_OK voir la classe
     *         Constant
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) {

        Annonce nouvelleAnnonce = null;

        try {
            nouvelleAnnonce = annonceService.remplirAnnonce(nouvelleAnnonceDTO);
            annonceDAO.saveAnnonceFirstTime(nouvelleAnnonce);
            annonceService.remplirSelAndHash(nouvelleAnnonce);
            annonceDAO.update(nouvelleAnnonce);
        } catch (BackendException | DuplicateEntityException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur lors de l'enregistrement de l'annonce", e);
            }
            // L'annonce est deja présente dans le BDD
            if (e instanceof DuplicateEntityException) {
                return Constant.CODE_SERVICE_ANNONCE_RETOUR_DUPLICATE;
            }
            // Erreur pendant la creation du service de l'annonce.
            return Constant.CODE_SERVICE_RETOUR_KO;
        }

        try {
            if (nouvelleAnnonce != null) {
                if (nouvelleAnnonceDTO.getIsSignedUp()) {
                    emailService.envoiMailConfirmationCreationAnnonce(nouvelleAnnonce);
                } else {
                    // On recupere l'url du frontend
                    Properties urlProperties = PropertiesUtils.loadPropertiesFile("url.properties");
                    String urlFrontend = urlProperties.getProperty("url.frontend.web");
                    emailService.envoiMailActivationCompte(nouvelleAnnonceDTO.getClient().getNom(), nouvelleAnnonceDTO
                            .getClient().getPrenom(), nouvelleAnnonceDTO.getClient().getLogin(), nouvelleAnnonceDTO
                            .getClient().getEmail(), nouvelleAnnonce.getDemandeur().getCleActivation(), urlFrontend);
                }
            }
        } catch (EmailException | MandrillApiError | IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur d'envoi de mail", e);
            }
        }

        return Constant.CODE_SERVICE_RETOUR_OK;
    }

    /**
     * Permet de récuperer les annonces d'un client à partir de son login
     * 
     * @param login
     *            l'identifiant de l'utilisateur
     * @return La liste des annonces de cet utilisateur
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCE_BY_LOGIN)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnnonceDTO> getAnnonceByClientLogin(String login) {
        // On escape les ""
        String loginEscaped = DeserializeJsonHelper.parseString(login);
        // On recupere les annonces de l'utilisateur
        List<Object[]> queryAnnoncesResult = annonceDAO.getAnnoncesByLoginForMesAnnonces(loginEscaped);
        // On crée la liste qui accueuillera les DTO
        List<AnnonceDTO> annoncesDTO = new ArrayList<AnnonceDTO>();

        for (Object[] annonce : queryAnnoncesResult) {
            // On crée le nouvel objet
            AnnonceDTO annonceDTO = new AnnonceDTO();
            // On transfert les données d'un objet a l'autre
            annonceDTO.setCategorieMetier((Short) annonce[0]);
            annonceDTO.setDescription((String) annonce[1]);
            annonceDTO.setEtatAnnonce((EtatAnnonce) annonce[2]);
            Long nbDevis = (Long) annonce[3];
            annonceDTO.setNbDevis(nbDevis);

            // On ajoute à la liste
            annoncesDTO.add(annonceDTO);
        }

        return annoncesDTO;
    }
}