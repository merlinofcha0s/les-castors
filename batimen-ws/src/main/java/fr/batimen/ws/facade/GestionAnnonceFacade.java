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
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.aggregate.DemAnnonceSelectEntrepriseDTO;
import fr.batimen.dto.aggregate.NbConsultationDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;
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

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private GestionUtilisateurFacade utilisateurFacade;

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
     * Permet de récuperer les annonces d'un client à partir de son login <br/>
     * Service servant principalement a la page mes annonces
     * 
     * @param login
     *            l'identifiant de l'utilisateur
     * @return La liste des annonces de cet utilisateur
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_LOGIN)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnnonceDTO> getAnnoncesByClientLoginForMesAnnonces(String login) {
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
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_GET_ANNONCES_BY_ID)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AnnonceAffichageDTO getAnnonceByIdForAffichage(DemandeAnnonceDTO demandeAnnonce) {
        String loginDemandeur = demandeAnnonce.getLoginDemandeur();
        String hashID = demandeAnnonce.getHashID();

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonce.getLoginDemandeur());
        // On crée l'objet qui contiendra les infos
        AnnonceAffichageDTO annonceAffichageDTO = new AnnonceAffichageDTO();

        Annonce annonce = annonceDAO.getAnnonceByIDForAffichage(hashID);

        if (annonce != null) {

            Boolean isArtisan = Boolean.FALSE;
            Boolean isArtisanInscrit = Boolean.FALSE;

            // Vérification des droits : soit l'artisan est inscrit soit il ne
            // l'est
            // pas
            if (rolesDemandeur.indexOf(TypeCompte.ARTISAN.getRole()) != -1) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un artisan qui fait la demande d'affichage d'annonce");
                }
                isArtisan = Boolean.TRUE;

                for (Artisan artisanInscrit : annonce.getArtisans()) {
                    // On regarde si il est inscrit...
                    if (artisanInscrit.getLogin().equals(loginDemandeur)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Il est inscrit à l'annonce");
                        }
                        // On charge le téléphone et le numéro de téléphone du
                        // client
                        isArtisanInscrit = Boolean.TRUE;
                    }
                }
                // Vérification des droits : Si c'est un client, est ce que
                // c'est
                // bien le possesseur de l'annonce.
            } else if (rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un client");
                }
                if (!annonce.getDemandeur().getLogin().equals(loginDemandeur)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Il ne possede par les droits d'affichage, on sort du service...");
                    }
                    return new AnnonceAffichageDTO();
                }
            }

            // Si on arrive jusque la c'est que l'utilisateur a les droits, donc
            // on mappe et on renvoi le résultat au front
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mapping des entités vers les DTOs");
            }
            annonceService.doMappingAnnonceAffichageDTO(annonce, annonceAffichageDTO, isArtisan, isArtisanInscrit);
        }
        return annonceAffichageDTO;
    }

    /**
     * Met à jour le nombre de consultation d'une annonce
     * 
     * @param nbConsultationDTO
     *            Objet contenant toutes les informations necessaires
     * @return
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_UPDATE_NB_CONSULTATION)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer updateNbConsultationAnnonce(NbConsultationDTO nbConsultationDTO) {

        Integer nbConsultation = nbConsultationDTO.getNbConsultation();

        Boolean updatedSuccess = annonceDAO.updateAnnonceNbConsultationByHashId(++nbConsultation,
                nbConsultationDTO.getHashID());

        if (updatedSuccess == Boolean.TRUE) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mise à jour du nb de consultation ok pour cette annonce: "
                        + nbConsultationDTO.getHashID());
            }
            return Constant.CODE_SERVICE_RETOUR_OK;
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme de mise à jour du nb de consultation pour l'annonce: "
                        + nbConsultationDTO.getHashID());
            }
            return Constant.CODE_SERVICE_RETOUR_KO;
        }
    }

    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_SUPRESS_ANNONCE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer suppressionAnnonce(DemandeAnnonceDTO demandeAnnonce) {

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonce.getLoginDemandeur());

        Boolean retourDAO = Boolean.FALSE;

        if (rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1
                || rolesDemandeur.indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1) {
            retourDAO = annonceDAO.suppressionAnnonce(demandeAnnonce);
        }

        if (retourDAO) {
            return Constant.CODE_SERVICE_RETOUR_OK;
        } else {
            return Constant.CODE_SERVICE_RETOUR_KO;
        }
    }

    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_SELECTION_UNE_ENTREPRISE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer selectOneEnterprise(DemAnnonceSelectEntrepriseDTO demandeAnnonceDTO) {

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonceDTO.getLoginDemandeur());

        Artisan artisan = artisanDAO.getArtisanByLogin(demandeAnnonceDTO.getLoginArtisanChoisi());
        Entreprise entrepriseChoisi = artisan.getEntreprise();

        Boolean retourDAO = Boolean.FALSE;
        if (rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1
                || rolesDemandeur.indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1 && entrepriseChoisi != null) {

            Annonce annonceToUpdate = annonceDAO.getAnnonceByIDWithoutTransaction(demandeAnnonceDTO.getHashID());
            if (demandeAnnonceDTO.getAjoutOuSupprimeArtisan() == DemAnnonceSelectEntrepriseDTO.AJOUT_ARTISAN) {
                annonceToUpdate.setEntrepriseSelectionnee(entrepriseChoisi);
            } else if (demandeAnnonceDTO.getAjoutOuSupprimeArtisan() == DemAnnonceSelectEntrepriseDTO.SUPPRESSION_ARTISAN) {
                annonceToUpdate.setEntrepriseSelectionnee(null);
            } else {
                return Constant.CODE_SERVICE_RETOUR_KO;
            }

            annonceDAO.update(annonceToUpdate);
            retourDAO = Boolean.TRUE;
        }

        if (retourDAO) {
            return Constant.CODE_SERVICE_RETOUR_OK;
        } else {
            return Constant.CODE_SERVICE_RETOUR_KO;
        }
    }
}