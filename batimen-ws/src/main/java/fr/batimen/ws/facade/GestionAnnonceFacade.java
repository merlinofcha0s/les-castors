package fr.batimen.ws.facade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.core.exception.EmailException;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.AnnonceSelectEntrepriseDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.aggregate.DesinscriptionAnnonceDTO;
import fr.batimen.dto.aggregate.NbConsultationDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.ImageDAO;
import fr.batimen.ws.dao.NotificationDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.entity.Image;
import fr.batimen.ws.enums.PropertiesFileWS;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.AnnonceService;
import fr.batimen.ws.service.EmailService;
import fr.batimen.ws.service.NotificationService;
import fr.batimen.ws.service.PhotoService;
import fr.batimen.ws.utils.FluxUtils;
import fr.batimen.ws.utils.RolesUtils;

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

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private NotificationService notificationService;

    @Inject
    private RolesUtils rolesUtils;

    @Inject
    private PhotoService photoService;

    @Inject
    private ImageDAO imageDAO;

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
                return CodeRetourService.ANNONCE_RETOUR_DUPLICATE;
            }
            // Erreur pendant la creation du service de l'annonce.
            return CodeRetourService.RETOUR_KO;
        }

        try {
            if (nouvelleAnnonce != null) {
                if (nouvelleAnnonceDTO.getIsSignedUp()) {
                    emailService.envoiMailConfirmationCreationAnnonce(nouvelleAnnonce);
                } else {
                    // On recupere l'url du frontend
                    Properties urlProperties = PropertiesFileWS.URL.getProperties();
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

        if (!nouvelleAnnonceDTO.getPhotos().isEmpty()) {
            List<String> imageUrls = photoService.sendPhotoToCloud(nouvelleAnnonceDTO.getPhotos());

            for (String url : imageUrls) {
                Image nouvelleImage = new Image();
                nouvelleImage.setUrl(url);
                nouvelleImage.setAnnonce(nouvelleAnnonce);
                nouvelleAnnonce.getImages().add(nouvelleImage);
                imageDAO.createMandatory(nouvelleImage);
            }
        }

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Permet la creation d'une nouvelle annonce par le client ainsi que le
     * compte de ce dernier <br/>
     * 
     * Mode multipart, en plus de JSON la request contient des photos.
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
    @Path(WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE_AVEC_IMAGES)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationAnnonceAvecImage(@FormDataParam("content") final InputStream content,
            @FormDataParam("files") final List<FormDataBodyPart> files,
            @FormDataParam("files") final List<FormDataContentDisposition> filesDetail) {

        CreationAnnonceDTO nouvelleAnnonceDTO = DeserializeJsonHelper.deserializeDTO(
                FluxUtils.getJsonByInputStream(content), CreationAnnonceDTO.class);

        if (LOGGER.isDebugEnabled()) {
            for (FormDataContentDisposition fileDetail : filesDetail) {
                LOGGER.debug("Details fichier : " + fileDetail);
            }
        }

        List<File> photos = FluxUtils.transformFormDataBodyPartsToFiles(files);

        nouvelleAnnonceDTO.getPhotos().addAll(photos);

        creationAnnonce(nouvelleAnnonceDTO);

        return CodeRetourService.RETOUR_OK;
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
            annonceDTO.setHashID(String.valueOf(annonce[4]));

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
            Boolean isAdmin = Boolean.FALSE;

            // Vérification des droits : soit l'artisan est inscrit soit il ne
            // l'est
            // pas
            if (rolesUtils.checkIfArtisanWithString(rolesDemandeur)) {
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
            } else if (rolesUtils.checkIfClientWithString(rolesDemandeur)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un client");
                }
                if (!annonce.getDemandeur().getLogin().equals(loginDemandeur)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Il ne possede par les droits d'affichage, on sort du service...");
                    }
                    return new AnnonceAffichageDTO();
                }
            } else if (rolesUtils.checkIfAdminWithString(rolesDemandeur)) {
                isAdmin = true;
            }

            // Si on arrive jusque la c'est que l'utilisateur a les droits, donc
            // on mappe et on renvoi le résultat au front
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mapping des entités vers les DTOs");
            }
            annonceService.doMappingAnnonceAffichageDTO(annonce, annonceAffichageDTO, isArtisan, isArtisanInscrit,
                    isAdmin);
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

        if (updatedSuccess.equals(Boolean.TRUE)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mise à jour du nb de consultation ok pour cette annonce: "
                        + nbConsultationDTO.getHashID());
            }
            return CodeRetourService.RETOUR_OK;
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme de mise à jour du nb de consultation pour l'annonce: "
                        + nbConsultationDTO.getHashID());
            }
            return CodeRetourService.RETOUR_KO;
        }
    }

    /**
     * Service de suppression d'une annonce <br/>
     * Si c'est un client, il doit posseder l'annonce, sinon le demandeur doit
     * etre admin.
     * 
     * @param demandeAnnonce
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_SUPRESS_ANNONCE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer suppressionAnnonce(DemandeAnnonceDTO demandeAnnonce) {

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonce.getLoginDemandeur());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Suppression à la demande de : " + demandeAnnonce.getLoginDemandeur());
            LOGGER.debug("Récupération du role du demandeur : " + rolesDemandeur);
            LOGGER.debug("Récupération de l'annonce : " + demandeAnnonce.getHashID());
        }

        Boolean retourDAO = Boolean.FALSE;

        Boolean isClient = rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1;
        Boolean isAdmin = rolesDemandeur.indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1;

        if (isClient || isAdmin) {

            Annonce annonceToUpdate = null;
            if (isAdmin) {
                annonceToUpdate = annonceDAO.getAnnonceByIDWithTransaction(demandeAnnonce.getHashID(), true);
            } else if (isClient) {
                annonceToUpdate = annonceDAO.getAnnonceByIDWithTransaction(demandeAnnonce.getHashID(), false);
            }

            if (isClient && !annonceToUpdate.getDemandeur().getLogin().equals(demandeAnnonce.getLoginDemandeur())) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Il ne possede par les droits de suppression, on sort du service...");
                }
                return CodeRetourService.RETOUR_KO;
            }
            retourDAO = annonceDAO.suppressionAnnonce(demandeAnnonce);
        }

        if (retourDAO) {
            return CodeRetourService.RETOUR_OK;
        } else {
            return CodeRetourService.RETOUR_KO;
        }
    }

    /**
     * Selection d'une entreprise par un particulier ou un admin <br/>
     * 
     * Si c'est un client, il doit posseder l'annonce, sinon le demandeur doit
     * etre admin.
     * 
     * @param demandeAnnonceDTO
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_SELECTION_UNE_ENTREPRISE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer selectOneEnterprise(AnnonceSelectEntrepriseDTO demandeAnnonceDTO) {

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonceDTO.getLoginDemandeur());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("On vérifie le / les role(s) du demandeur : " + rolesDemandeur);
        }

        Artisan artisan = artisanDAO.getArtisanByLogin(demandeAnnonceDTO.getLoginArtisanChoisi());
        Entreprise entrepriseChoisi = artisan.getEntreprise();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("On récupére l'artisan choisi : " + artisan.getLogin());
        }

        Boolean isClient = rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1;
        Boolean isAdmin = rolesDemandeur.indexOf(TypeCompte.ADMINISTRATEUR.getRole()) != -1;

        Boolean retourDAO = Boolean.FALSE;
        if (isClient || isAdmin && entrepriseChoisi != null) {

            Annonce annonceToUpdate = null;
            if (isAdmin) {
                annonceToUpdate = annonceDAO.getAnnonceByIDWithTransaction(demandeAnnonceDTO.getHashID(), true);
            } else if (isClient) {
                annonceToUpdate = annonceDAO.getAnnonceByIDWithTransaction(demandeAnnonceDTO.getHashID(), false);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Le demandeur est donc soit un client soit un admin");
                LOGGER.debug("On charge l'annonce : " + annonceToUpdate.getHashID());
            }

            if (rolesDemandeur.indexOf(TypeCompte.CLIENT.getRole()) != -1
                    && !annonceToUpdate.getDemandeur().getLogin().equals(demandeAnnonceDTO.getLoginDemandeur())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Il ne possede par les droits d'ajout, on sort du service...");
                }
                return CodeRetourService.RETOUR_KO;
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Si c'est un client, il possede les droits necessaires");
            }

            if (demandeAnnonceDTO.getAjoutOuSupprimeArtisan() == AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un ajout, on met à jour l'annonce avec l'entreprise choisi et on change l'état de l'annonce");
                }
                annonceToUpdate.setEntrepriseSelectionnee(entrepriseChoisi);
                annonceToUpdate.setEtatAnnonce(EtatAnnonce.A_NOTER);
                notificationDAO.createNotificationEntrepriseChoisiParClient(annonceToUpdate);
                // TODO Envoi de mail de notification
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Ni ajout, ni suppression dans la selection artisan, cas impossible");
                }
                return CodeRetourService.RETOUR_KO;
            }

            annonceDAO.update(annonceToUpdate);
            retourDAO = Boolean.TRUE;
        }

        if (retourDAO) {
            return CodeRetourService.RETOUR_OK;
        } else {
            return CodeRetourService.RETOUR_KO;
        }
    }

    /**
     * Service qui permet à un artisan de s'inscrire à une annonce, pas besoin
     * du type de compte dans l'objet demande anonnce DTO
     * 
     * @param demandeAnnonceDTO
     *            Objet permettant de faire la demande
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_INSCRIPTION_UN_ARTISAN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer inscriptionUnArtisan(DemandeAnnonceDTO demandeAnnonceDTO) {

        Annonce annonce = annonceDAO.getAnnonceByIDWithTransaction(demandeAnnonceDTO.getHashID(), false);

        if (annonce == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Impossible de trouver l'annonce avec l'id: " + demandeAnnonceDTO.getHashID());
            }
            return CodeRetourService.RETOUR_KO;
        }
        Artisan artisan = artisanDAO.getArtisanByLogin(demandeAnnonceDTO.getLoginDemandeur());

        if (artisan == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Impossible de trouver l'artisan avec le login: " + demandeAnnonceDTO.getLoginDemandeur());
            }
            return CodeRetourService.RETOUR_KO;
        }

        Integer codeRetourInscription = annonceService.inscrireArtisan(demandeAnnonceDTO, annonce, artisan);

        if (!codeRetourInscription.equals(CodeRetourService.RETOUR_OK)) {
            return codeRetourInscription;
        }

        notificationService.generationNotificationInscriptionArtisan(demandeAnnonceDTO, annonce, artisan);

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Service qui permet à un client de ne pas accepter un artisan à son
     * annonce <br/>
     * 
     * Réactive l'annonce si elle etait en quotas max atteint.
     * 
     * @param desinscriptionAnnonceDTO
     *            Objet permettant de faire la demande de desinscription
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_DESINSCRIPTION_UN_ARTISAN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer desinscriptionUnArtisan(DesinscriptionAnnonceDTO desinscriptionAnnonceDTO) {
        Annonce annonce = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+------------------------------------------------------------------------------+");
            LOGGER.debug("| Hash ID : " + desinscriptionAnnonceDTO.getHashID());
            LOGGER.debug("| Login demandeur : " + desinscriptionAnnonceDTO.getLoginDemandeur());
            LOGGER.debug("| Artisan à desinscrire : " + desinscriptionAnnonceDTO.getLoginArtisan());
            LOGGER.debug("+------------------------------------------------------------------------------+");
        }

        String rolesClientDemandeur = utilisateurFacade.getUtilisateurRoles(desinscriptionAnnonceDTO
                .getLoginDemandeur());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Role du client récupéré: " + rolesClientDemandeur);
        }

        if (rolesUtils.checkIfAdminWithString(rolesClientDemandeur)) {
            annonce = annonceDAO.getAnnonceByIDWithTransaction(desinscriptionAnnonceDTO.getHashID(), true);
        } else if (rolesUtils.checkIfClientWithString(rolesClientDemandeur)) {
            annonce = annonceDAO.getAnnonceByIdByLogin(desinscriptionAnnonceDTO.getHashID(),
                    desinscriptionAnnonceDTO.getLoginDemandeur());
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("N'a pas les bons droits pour accéder à ce service !!!");
            }
            return CodeRetourService.RETOUR_KO;
        }

        boolean atLeastOneRemoved = false;

        if (annonce != null) {

            Set<Artisan> artisans = annonce.getArtisans();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Début itération recherche de l'artisan a supprimer de l'annonce");

            }

            for (Iterator<Artisan> itArtisan = artisans.iterator(); itArtisan.hasNext();) {

                Artisan artisanADesinscrire = itArtisan.next();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("+-------------------------------------------------------------+");
                    LOGGER.debug("| Login : " + artisanADesinscrire.getLogin());
                    LOGGER.debug("| Email : " + artisanADesinscrire.getEmail());
                    LOGGER.debug("+-------------------------------------------------------------+");
                }

                if (artisanADesinscrire.getLogin().equals(desinscriptionAnnonceDTO.getLoginArtisan())) {
                    artisans.remove(artisanADesinscrire);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Artisan trouvée");
                    }
                    atLeastOneRemoved = true;

                    if (annonce.getEtatAnnonce().equals(EtatAnnonce.QUOTA_MAX_ATTEINT)) {
                        annonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
                    }
                }
            }
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Annonce inexistante");
            }
            return CodeRetourService.RETOUR_KO;
        }

        if (atLeastOneRemoved) {
            return CodeRetourService.RETOUR_OK;
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Artisan introuvable dans l'annonce");
            }
            return CodeRetourService.ANNONCE_RETOUR_ARTISAN_INTROUVABLE;
        }

    }
}