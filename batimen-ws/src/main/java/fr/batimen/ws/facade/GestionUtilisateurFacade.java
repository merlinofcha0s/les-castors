package fr.batimen.ws.facade;

import java.util.List;

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

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.dao.PermissionDAO;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.entity.Permission;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.ArtisanService;
import fr.batimen.ws.service.ClientService;

/**
 * Facade REST de gestion des utilisateurs
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionUtilisateurFacade")
@LocalBean
@Path(WsPath.GESTION_UTILISATEUR_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
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

    /**
     * Methode de login des utilisateurs
     * 
     * @param LoginDTO
     *            loginDTO objet permettant l'authentification
     * @return vide si la combinaison login / mdp ne corresponds pas ou si
     *         inexistant
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_LOGIN)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ClientDTO login(LoginDTO toLogin) {
        ModelMapper modelMapper = new ModelMapper();
        Client client = clientDAO.getClientByLoginName(toLogin.getLogin());

        Artisan artisan = null;
        // Si on a pas trouvé le particulier, on va chercher l'artisan
        if (client.getLogin().isEmpty()) {
            artisan = artisanDAO.getArtisanByLogin(toLogin.getLogin());
            return modelMapper.map(artisan, ClientDTO.class);
        } else {
            return modelMapper.map(client, ClientDTO.class);
        }
    }

    /**
     * Renvoi le hash d'un utilisateur, vide si il n'existe pas.
     * 
     * @param login
     *            Le login du Client
     * @return
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_HASH)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getUtilisateurHash(String login) {
        // On enleve les "" car ils sont deserializer dans la requete REST
        String loginEscaped = DeserializeJsonHelper.parseString(login);

        String hash = clientDAO.getHash(loginEscaped);

        if (hash.isEmpty()) {
            hash = artisanDAO.getHash(loginEscaped);
        }

        return hash;
    }

    /**
     * Renvoi les roles d'un utilisateur, vide si pas de roles.
     * 
     * @param login
     *            Le login de l'utilisateur
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
     * @param email
     *            l'email que l'on veut tester
     * @return Le client avec l'email correspondant
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_BY_EMAIL)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ClientDTO utilisateurByEmail(String email) {
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
     * @param cleActivation
     *            la clé permettant de retrouver le client et d'activer son
     *            compte.
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

        return Constant.CODE_SERVICE_ANNONCE_RETOUR_COMPTE_INEXISTANT;
    }
}
