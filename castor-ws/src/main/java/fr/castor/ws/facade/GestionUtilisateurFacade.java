package fr.castor.ws.facade;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.core.constant.Constant;
import fr.castor.core.constant.WsPath;
import fr.castor.dto.*;
import fr.castor.dto.aggregate.MesAnnoncesAnnonceDTO;
import fr.castor.dto.aggregate.MesAnnoncesNotificationDTO;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.ws.dao.*;
import fr.castor.ws.entity.AbstractUser;
import fr.castor.ws.entity.Artisan;
import fr.castor.ws.entity.Client;
import fr.castor.ws.entity.Permission;
import fr.castor.ws.helper.JsonHelper;
import fr.castor.ws.interceptor.BatimenInterceptor;
import fr.castor.ws.service.AnnonceService;
import fr.castor.ws.service.ArtisanService;
import fr.castor.ws.service.ClientService;
import fr.castor.ws.service.NotificationService;
import fr.castor.ws.utils.RolesUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Facade REST de gestion des utilisateurs
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "GestionUtilisateurFacade")
@LocalBean
@Path(WsPath.GESTION_UTILISATEUR_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = {BatimenInterceptor.class})
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionUtilisateurFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionUtilisateurFacade.class);

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private ClientService clientService;

    @Inject
    private ArtisanService artisanService;

    @Inject
    private PermissionDAO permissionDAO;

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private RolesUtils rolesUtils;

    @Inject
    private NotificationService notificationService;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private AnnonceService annonceService;

    @Inject
    private AnnonceDAO annonceDAO;

    /**
     * Methode de login des utilisateurs
     *
     * @param toLogin loginDTO objet permettant l'authentification
     * @return vide si la combinaison login / mdp ne corresponds pas ou si
     * inexistant
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_LOGIN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ClientDTO login(LoginDTO toLogin) {
        ModelMapper modelMapper = new ModelMapper();
        Client client = clientDAO.getClientByLoginName(toLogin.getLogin());

        Artisan artisan = null;
        // Si on a pas trouvé le particulier, on va chercher l'artisan
        if (client.getLogin().isEmpty()) {
            artisan = artisanDAO.getArtisanByLogin(toLogin.getLogin());
            ClientDTO clientDTO = modelMapper.map(artisan, ClientDTO.class);
            for (Permission permission : artisan.getPermissions()) {
                clientDTO.getPermissions().add(modelMapper.map(permission, PermissionDTO.class));
            }

            return clientDTO;
        } else {
            ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
            for (Permission permission : client.getPermissions()) {
                clientDTO.getPermissions().add(modelMapper.map(permission, PermissionDTO.class));
            }
            return clientDTO;
        }
    }

    /**
     * Renvoi le hash d'un utilisateur, vide si il n'existe pas.<br/>
     * Vide également dans le cas où son compte n'est pas activé
     *
     * @param login Le login du Client
     * @return
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_HASH)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getUtilisateurHashAndStatut(String login) {
        // On enleve les "" car ils sont deserializer dans la requete REST
        String loginEscaped = DeserializeJsonHelper.parseString(login);

        String hash = clientDAO.getHash(loginEscaped);
        Boolean isActive = Boolean.FALSE;

        if (hash.isEmpty()) {
            hash = artisanDAO.getHash(loginEscaped);
            isActive = artisanDAO.getStatutActive(loginEscaped);
        } else {
            isActive = clientDAO.getStatutActive(loginEscaped);
        }

        if (!hash.isEmpty() && isActive) {
            return hash;
        }

        return "";
    }

    /**
     * Renvoi les roles d'un utilisateur, vide si pas de roles.
     *
     * @param login Le login de l'utilisateur
     * @return
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_ROLES)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getUtilisateurRoles(String login) {
        // On enleve les "" car ils sont deserializer dans la requete REST
        String loginEscaped = DeserializeJsonHelper.parseString(login);
        List<Permission> permissions = permissionDAO.getClientPermissions(loginEscaped);

        // Si les permissions sont vide c'est qu'il s'agit d'un artisan
        if (permissions.isEmpty()) {
            permissions = permissionDAO.getArtisanPermissions(loginEscaped);
        }

        if (permissions.isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur impossible, Cet utilisateur n'a aucun droit :" + login);
            }
            return "";
        }

        StringBuilder rolesExtracted = new StringBuilder();

        for (Permission permission : permissions) {
            rolesExtracted.append(permission.getTypeCompte().getRole());
            rolesExtracted.append(", ");
        }

        // On supprime le dernier ", "
        rolesExtracted.delete(rolesExtracted.length() - 2, rolesExtracted.length());

        return rolesExtracted.toString();
    }

    /**
     * Methode de recuperation d'un utilisateur par son email
     *
     * @param email l'email que l'on veut tester
     * @return Le client avec l'email correspondant
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_BY_EMAIL)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ClientDTO getUtilisateurByEmail(String email) {
        // Obligé pour les strings sinon il n'escape pas les ""
        String emailEscaped = DeserializeJsonHelper.parseString(email);
        ModelMapper modelMapper = new ModelMapper();
        Client client = clientDAO.getClientByEmail(emailEscaped);
        Artisan artisan = null;

        if (client.getLogin().isEmpty()) {
            artisan = artisanDAO.getArtisanByEmail(emailEscaped);
            return modelMapper.map(artisan, ClientDTO.class);
        } else {
            return modelMapper.map(client, ClientDTO.class);
        }

    }

    /**
     * Methode d'activation d'un compte d'un utilisateur.
     *
     * @param cleActivation la clé permettant de retrouver le client et d'activer son
     *                      compte.
     * @return Le resultat de l'activation.
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_ACTIVATION)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer activateAccount(String cleActivation) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Activation du compte avec la clé : " + cleActivation);
        }

        String cleActivationEscaped = DeserializeJsonHelper.parseString(cleActivation);
        Client clientByKey = clientDAO.getClientByActivationKey(cleActivationEscaped);

        if (!clientByKey.getLogin().isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("C'est un client : " + clientByKey.getLogin());
            }
            return clientService.activateAccount(clientByKey);
        }

        Artisan artisanByKey = artisanDAO.getArtisanByActivationKey(cleActivationEscaped);

        if (!artisanByKey.getLogin().isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("C'est un artisan : " + artisanByKey.getLogin());
            }
            return artisanService.activateAccount(artisanByKey);
        }

        return CodeRetourService.ANNONCE_RETOUR_COMPTE_INEXISTANT;
    }

    /**
     * Methode de mise à jour des infos d'un client
     *
     * @param clientDTO les infos du clients
     * @return Code retour @see {@link Constant}
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_UPDATE_INFO)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer updateUtilisateurInfos(ModifClientDTO clientDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation de l'entité client pour : " + clientDTO.getLogin());
        }

        boolean isArtisan = false;

        AbstractUser clientInDB = clientDAO.getClientByEmail(clientDTO.getOldEmail());
        if (clientInDB.getDateInscription() == null) {
            isArtisan = true;
            clientInDB = artisanDAO.getArtisanByEmail(clientDTO.getOldEmail());
        }

        if (clientInDB == null) {
            return CodeRetourService.RETOUR_KO;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Mapping de la dto avec l'entité.....");
        }
        ModelMapper mapper = new ModelMapper();
        mapper.map(clientDTO, clientInDB);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Mise a jour de l'entité dans la BDD");
        }

        if (isArtisan) {
            artisanDAO.update((Artisan) clientInDB);
        } else {
            clientDAO.update((Client) clientInDB);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Code retour du service : " + CodeRetourService.RETOUR_OK);
        }
        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Récupére les informations des annonces pour les afficher sur la page mesannonces.
     *
     * Si c'est un client, on renvoi les annonces qu'il a créée.
     *
     * Si c'est un artisan on renvoi les annonces auquels il est inscrit.
     *
     * @param demandeMesAnnoncesDTO
     * @return La liste des annonces paginées concernant l'utilisateur.
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_ANNONCES_FOR_MES_ANNONCES)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public MesAnnoncesAnnonceDTO getAnnonceForMesAnnonces(DemandeMesAnnoncesDTO demandeMesAnnoncesDTO) {

        MesAnnoncesAnnonceDTO mesAnnoncesAnnonceDTO = new MesAnnoncesAnnonceDTO();
        Long nbannonceByLogin = new Long(0);

        String rolesDemandeur = getUtilisateurRoles(demandeMesAnnoncesDTO
                .getLoginDemandeur());
        String rolesDemander = getUtilisateurRoles(demandeMesAnnoncesDTO
                .getLogin());

        if (rolesDemandeur.isEmpty()) {
            return new MesAnnoncesAnnonceDTO();
        }

        if (!rolesUtils.checkIfAdminWithString(rolesDemandeur)
                && !demandeMesAnnoncesDTO.getLogin().equals(demandeMesAnnoncesDTO.getLoginDemandeur())) {
            return new MesAnnoncesAnnonceDTO();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation annonce.........");
        }

        String login = demandeMesAnnoncesDTO.getLogin();

        List<AnnonceDTO> annoncesDTO =
                annonceService.getAnnoncesByClientLoginForMesAnnonces(login, rolesUtils.checkIfArtisanWithString(rolesDemander)
                        , demandeMesAnnoncesDTO.getRangeAnnoncesDebut(), demandeMesAnnoncesDTO.getRangeAnnonceFin());

        if (rolesUtils.checkIfArtisanWithString(rolesDemander)) {
            nbannonceByLogin = annonceDAO.getNbAnnonceByLoginForArtisan(demandeMesAnnoncesDTO.getLogin());
        } else if (rolesUtils.checkIfClientWithString(rolesDemander)) {
            nbannonceByLogin = annonceDAO.getNbAnnonceByLoginForClient(demandeMesAnnoncesDTO.getLogin());
        }

        mesAnnoncesAnnonceDTO.setAnnonces(annoncesDTO);
        mesAnnoncesAnnonceDTO.setNbTotalAnnonces(nbannonceByLogin);

        return mesAnnoncesAnnonceDTO;
    }

    /**
     * Récupère les notifications d'un utilisateur.
     * <p>
     * Charge les notifications de manière paginées
     *
     * @param demandeMesAnnoncesDTO
     * @return
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_NOTIFICATIONS_FOR_MES_ANNONCES)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public MesAnnoncesNotificationDTO getNotificationForMesAnnonces(DemandeMesAnnoncesDTO demandeMesAnnoncesDTO) {

        MesAnnoncesNotificationDTO mesAnnoncesNotificationDTO = new MesAnnoncesNotificationDTO();
        Long nbNotificationByLogin = new Long(0);

        String rolesDemandeur = getUtilisateurRoles(demandeMesAnnoncesDTO
                .getLoginDemandeur());
        String rolesDemander = getUtilisateurRoles(demandeMesAnnoncesDTO
                .getLogin());

        if (rolesDemandeur.isEmpty()) {
            return new MesAnnoncesNotificationDTO();
        }

        if (!rolesUtils.checkIfAdminWithString(rolesDemandeur)
                && !demandeMesAnnoncesDTO.getLogin().equals(demandeMesAnnoncesDTO.getLoginDemandeur())) {
            return new MesAnnoncesNotificationDTO();
        }

        String login = demandeMesAnnoncesDTO.getLogin();


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation notification.........");
        }

        List<NotificationDTO> notificationsDTO = null;

        if (rolesUtils.checkIfArtisanWithString(rolesDemander)) {
            notificationsDTO = notificationService.getNotificationByLogin(login, TypeCompte.ARTISAN, demandeMesAnnoncesDTO.getRangeNotificationsDebut(), demandeMesAnnoncesDTO.getRangeNotificationsFin());
            nbNotificationByLogin = notificationDAO.countNotificationByUser(login, TypeCompte.ARTISAN);
        } else if (rolesUtils.checkIfClientWithString(rolesDemander)) {
            notificationsDTO = notificationService.getNotificationByLogin(login, TypeCompte.CLIENT, demandeMesAnnoncesDTO.getRangeNotificationsDebut(), demandeMesAnnoncesDTO.getRangeNotificationsFin());
            nbNotificationByLogin = notificationDAO.countNotificationByUser(login, TypeCompte.CLIENT);
        }

        mesAnnoncesNotificationDTO.setNotifications(notificationsDTO);
        mesAnnoncesNotificationDTO.setNbTotalNotifications(nbNotificationByLogin);

        return mesAnnoncesNotificationDTO;
    }

}