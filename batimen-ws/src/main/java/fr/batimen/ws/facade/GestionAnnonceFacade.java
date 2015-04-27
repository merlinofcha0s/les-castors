package fr.batimen.ws.facade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

import fr.batimen.dto.aggregate.*;
import fr.batimen.ws.mapper.AnnonceMap;
import org.modelmapper.ModelMapper;
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
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
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
import fr.batimen.ws.service.NotationService;
import fr.batimen.ws.service.NotificationService;
import fr.batimen.ws.service.PhotoService;
import fr.batimen.ws.utils.FluxUtils;
import fr.batimen.ws.utils.RolesUtils;

/**
 * Facade REST de gestion des annonces.
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "GestionAnnonceFacade")
@LocalBean
@Path(WsPath.GESTION_ANNONCE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = {BatimenInterceptor.class})
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

    @Inject
    private NotationService notationService;

    /**
     * Permet la creation d'une nouvelle annonce par le client ainsi que le
     * compte de ce dernier
     *
     * @param nouvelleAnnonceDTO L'objet provenant du frontend qui permet la creation de
     *                           l'annonce.
     * @return CODE_SERVICE_RETOUR_KO ou CODE_SERVICE_RETOUR_OK voir la classe
     * Constant
     * @see Constant
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
     * <p/>
     * Mode multipart, en plus de JSON la request contient des photos.
     *
     * @param content     L'objet provenant du frontend qui permet la creation de
     *                    l'annonce.
     * @param files       Liste contenant l'ensemble des photos.
     * @param filesDetail Liste contenant les metadata des photos du client.
     * @return CODE_SERVICE_RETOUR_KO ou CODE_SERVICE_RETOUR_OK voir la classe
     * Constant
     * @see Constant
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
     * @param login l'identifiant de l'utilisateur
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
     * @param demandeAnnonce le hashID avec le login du demandeur dans le but de vérifier
     *                       les droits.
     * @return l'ensemble des informations qui permettent d'afficher l'annonce
     * correctement
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
     * @param nbConsultationDTO Objet contenant toutes les informations necessaires
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
     * <p/>
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+------------------------------------------------------------------------------+");
            LOGGER.debug("| Hash ID : " + demandeAnnonceDTO.getHashID());
            LOGGER.debug("| Login demandeur : " + demandeAnnonceDTO.getLoginDemandeur());
            LOGGER.debug("| Ajout / suppression : " + demandeAnnonceDTO.getAjoutOuSupprimeArtisan());
            LOGGER.debug("| Artisan choisi : " + demandeAnnonceDTO.getLoginArtisanChoisi());
            LOGGER.debug("+------------------------------------------------------------------------------+");
        }

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(demandeAnnonceDTO.getLoginDemandeur());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("On vérifie le / les role(s) du demandeur : " + rolesDemandeur);
        }

        Artisan artisan = artisanDAO.getArtisanByLogin(demandeAnnonceDTO.getLoginArtisanChoisi());
        Entreprise entrepriseChoisi = artisan.getEntreprise();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("On récupére l'artisan choisi : " + artisan.getLogin());
        }

        Boolean retourDAO = Boolean.FALSE;

        Annonce annonceToUpdate = null;

        annonceToUpdate = loadAnnonceAndCheckUserClientOrAdminRight(rolesDemandeur, demandeAnnonceDTO.getHashID());

        if (annonceToUpdate != null) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Le demandeur est donc soit un client soit un admin");
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
                notificationService.generationNotificationArtisan(annonceToUpdate, artisan, TypeCompte.ARTISAN,
                        TypeNotification.A_CHOISI_ENTREPRISE);
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Ni ajout, ni suppression dans la selection artisan, cas impossible");
                }
                return CodeRetourService.RETOUR_KO;
            }

            annonceDAO.update(annonceToUpdate);
            retourDAO = Boolean.TRUE;
        } else {
            retourDAO = Boolean.FALSE;
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
     * @param demandeAnnonceDTO Objet permettant de faire la demande
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_INSCRIPTION_UN_ARTISAN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer inscriptionUnArtisan(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+------------------------------------------------------------------------------+");
            LOGGER.debug("| Hash ID : " + demandeAnnonceDTO.getHashID());
            LOGGER.debug("| Login demandeur : " + demandeAnnonceDTO.getLoginDemandeur());
            LOGGER.debug("+------------------------------------------------------------------------------+");
        }

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

        notificationService.generationNotificationArtisan(annonce, artisan, TypeCompte.CLIENT,
                TypeNotification.INSCRIT_A_ANNONCE);

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Service qui permet à un client de ne pas accepter un artisan à son
     * annonce <br/>
     * <p/>
     * Réactive l'annonce si elle etait en quotas max atteint.
     *
     * @param desinscriptionAnnonceDTO Objet permettant de faire la demande de desinscription
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

        annonce = loadAnnonceAndCheckUserClientOrAdminRight(rolesClientDemandeur, desinscriptionAnnonceDTO.getHashID());

        boolean atLeastOneRemoved = false;

        if (annonce != null) {

            Set<Artisan> artisans = annonce.getArtisans();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Début itération recherche de l'artisan a supprimer de l'annonce");

            }

            for (Iterator<Artisan> itArtisan = artisans.iterator(); itArtisan.hasNext(); ) {

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

    /**
     * Service qui permet à un client de noter un artisan<br/>
     * <p/>
     * Fait passer l'annonce en état terminer
     * <p/>
     * Génére une notification à destination de l'artisan
     *
     * @param noterArtisanDTO Objet permettant de valider la note de l'artisan
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_NOTER_UN_ARTISAN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer noterUnArtisan(NoterArtisanDTO noterArtisanDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+-------------------------------------------------------------+");
            LOGGER.debug("| Hash ID : {}", noterArtisanDTO.getHashID());
            LOGGER.debug("| Artisan : {}", noterArtisanDTO.getLoginArtisan());
            LOGGER.debug("| Demandeur : {}", noterArtisanDTO.getLoginDemandeur());
            LOGGER.debug("| Notation : {}", noterArtisanDTO.getNotation().getScore());
            LOGGER.debug("| Commentaire : {}", noterArtisanDTO.getNotation().getCommentaire());
            LOGGER.debug("| Nom Entreprise : {}", noterArtisanDTO.getNotation().getNomEntreprise());
            LOGGER.debug("+-------------------------------------------------------------+");
        }

        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(noterArtisanDTO.getLoginDemandeur());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Roles du demandeur : {}", rolesDemandeur);
        }

        Annonce annonceANoter = loadAnnonceAndCheckUserClientOrAdminRight(rolesDemandeur, noterArtisanDTO.getHashID());

        if (annonceANoter == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Annonce introuvable, ou le demandeur ne possede pas cette annonce !!!");
                LOGGER.error("+-------------------------------------------------------------+");
                LOGGER.error("| Hash ID : {}", noterArtisanDTO.getHashID());
                LOGGER.error("| Artisan : {}", noterArtisanDTO.getLoginArtisan());
                LOGGER.error("| Demandeur : {}", noterArtisanDTO.getLoginDemandeur());
                LOGGER.error("| Notation : {}", noterArtisanDTO.getNotation().getScore());
                LOGGER.error("| Commentaire : {}", noterArtisanDTO.getNotation().getCommentaire());
                LOGGER.error("| Nom Entreprise : {}", noterArtisanDTO.getNotation().getNomEntreprise());
                LOGGER.error("+-------------------------------------------------------------+");
            }
            return CodeRetourService.RETOUR_KO;
        }

        Artisan artisan = artisanDAO.getArtisanByLogin(noterArtisanDTO.getLoginArtisan());

        if (artisan == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Artisan introuvable : {}", noterArtisanDTO.getLoginArtisan());
            }
            return CodeRetourService.RETOUR_KO;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enregistrement de la note de l'artisan");
        }

        notationService.noterArtisanService(annonceANoter, artisan, noterArtisanDTO.getNotation().getCommentaire(),
                noterArtisanDTO.getNotation().getScore());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Changement de l'etat de l'annonce");
        }

        // Changement de l'etat de l'annonce
        annonceANoter.setEtatAnnonce(EtatAnnonce.TERMINER);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Génération de la notification à destination de l'artisan");
        }

        notificationService.generationNotificationArtisan(annonceANoter, artisan, TypeCompte.ARTISAN,
                TypeNotification.A_NOTER_ENTREPRISE);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin du service de notation");
        }

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Service qui permet à un client de pouvoir modifier son annonce<br/>
     * <p/>
     * Génére une notification à destination des artisans inscrits
     *
     * @param modificationAnnonceDTO Objet permettant de récuperer les informations qui ont été modifiée par le client
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_MODIFICATION_ANNONCE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer modifierAnnonce(ModificationAnnonceDTO modificationAnnonceDTO) {
        String rolesDemandeur = utilisateurFacade.getUtilisateurRoles(modificationAnnonceDTO.getLoginDemandeur());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Roles du demandeur : {}", rolesDemandeur);
        }

        boolean generateNotification = false;

        Annonce annonceAModifier = loadAnnonceAndCheckUserClientOrAdminRight(rolesDemandeur, modificationAnnonceDTO.getAnnonce().getHashID());
        if (annonceAModifier != null) {
            ModelMapper mapper = new ModelMapper();
            mapper.addMappings(new AnnonceMap());
            mapper.map(modificationAnnonceDTO.getAnnonce(), annonceAModifier);
            mapper.map(modificationAnnonceDTO.getAdresse(), annonceAModifier.getAdresseChantier());
            annonceAModifier.setDateMAJ(new Date());
            annonceDAO.update(annonceAModifier);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mise à jour de l'annonce : {}", annonceAModifier);
            }
            generateNotification = true;
        } else {
            return CodeRetourService.ANNONCE_RETOUR_INTROUVABLE;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Génération des notifications à destinations des artisans inscrit à cette annonce");
        }
        if (generateNotification) {
            for (Artisan artisanToNotify : annonceAModifier.getArtisans()) {
                notificationService.generationNotificationArtisan(annonceAModifier, artisanToNotify, TypeCompte.ARTISAN, TypeNotification.A_MODIFIER_ANNONCE);
            }
        }
        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Service qui permet à un client de pouvoir ajouter / rajouter des photos à son annonce<br/>
     * <p/>
     * Génére une notification à destination des artisans inscrits
     * <p/>
     * <p/>
     * Mode multipart, en plus du JSON la request contient des photos.
     *
     * @param content     L'objet provenant du frontend qui permet la creation de
     *                    l'annonce.
     * @param files       Liste contenant l'ensemble des photos.
     * @param filesDetail Liste contenant les metadata des photos du client.
     * @return {@link CodeRetourService}
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_AJOUT_PHOTO)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer ajouterPhoto(@FormDataParam("content") final InputStream content,
                                @FormDataParam("files") final List<FormDataBodyPart> files,
                                @FormDataParam("files") final List<FormDataContentDisposition> filesDetail) {

        AjoutPhotoDTO ajoutPhotoDTO = DeserializeJsonHelper.deserializeDTO(
                FluxUtils.getJsonByInputStream(content), AjoutPhotoDTO.class);

        if (LOGGER.isDebugEnabled()) {
            for (FormDataContentDisposition fileDetail : filesDetail) {
                LOGGER.debug("Details fichier : " + fileDetail);
            }
        }

        List<File> photos = FluxUtils.transformFormDataBodyPartsToFiles(files);

        //TODO appeler le service cloud pour les photos
        //TODO Une fois les urls recuperer les persister dans l'annonce charger en dessous.

        Annonce annonceRajouterPhoto = loadAnnonceAndCheckUserClientOrAdminRight(
                utilisateurFacade.getUtilisateurRoles(ajoutPhotoDTO.getLoginDemandeur()), ajoutPhotoDTO.getHashID());

        return CodeRetourService.RETOUR_OK;
    }

    private Annonce loadAnnonceAndCheckUserClientOrAdminRight(String rolesClientDemandeur, String hashID) {
        if (rolesUtils.checkIfAdminWithString(rolesClientDemandeur)) {
            return annonceDAO.getAnnonceByIDWithTransaction(hashID, true);
        } else if (rolesUtils.checkIfClientWithString(rolesClientDemandeur)) {
            return  annonceDAO.getAnnonceByIDWithTransaction(hashID, false);
            //TODO Refactorer pour checker que le client possede l'annonce
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("N'a pas les bons droits pour accéder à ce service !!!");
                LOGGER.error("Roles : {}", rolesClientDemandeur);
                LOGGER.error("Hash ID : {}", hashID);
            }
            return null;
        }
    }
}