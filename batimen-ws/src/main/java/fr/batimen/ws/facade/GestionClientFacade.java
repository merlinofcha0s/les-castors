package fr.batimen.ws.facade;

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
import fr.batimen.core.exception.BackendException;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

/**
 * Facade REST de gestion des clients
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionClientFacade")
@LocalBean
@Path(WsPath.GESTION_CLIENT_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionClientFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionClientFacade.class);

    @Inject
    ClientDAO clientDAO;

    /**
     * Methode de login des utilisateurs
     * 
     * @param LoginDTO
     *            loginDTO objet permettant l'authentification
     * @return vide si la combinaison login / mdp ne corresponds pas ou si
     *         inexistant
     */
    @POST
    @Path(WsPath.GESTION_CLIENT_SERVICE_LOGIN)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ClientDTO login(LoginDTO toLogin) {
        ModelMapper modelMapper = new ModelMapper();

        Client client = clientDAO.getClientByLoginName(toLogin.getLogin());

        return modelMapper.map(client, ClientDTO.class);
    }

    /**
     * Methode de recuperation d'un client par son email
     * 
     * @param email
     *            l'email que l'on veut tester
     * @return Le client avec l'email correspondant
     */
    @POST
    @Path(WsPath.GESTION_CLIENT_SERVICE_BY_EMAIL)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ClientDTO clientByEmail(String email) {

        // Obligé pour les strings sinon il n'escape pas les ""
        String emailEscaped = DeserializeJsonHelper.parseString(email);

        ModelMapper modelMapper = new ModelMapper();

        Client client = clientDAO.getClientByEmail(emailEscaped);

        return modelMapper.map(client, ClientDTO.class);
    }

    /**
     * Methode de recuperation d'un client par son email
     * 
     * @param email
     *            l'email que l'on veut tester
     * @return Le client avec l'email correspondant
     */
    @POST
    @Path(WsPath.GESTION_CLIENT_SERVICE_ACTIVATION)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public int activateAccount(String cleActivation) {

        String cleActivationEscaped = DeserializeJsonHelper.parseString(cleActivation);

        Client clientByKey;
        clientByKey = clientDAO.getClientByActivationKey(cleActivationEscaped);

        if (!clientByKey.getLogin().isEmpty()) {
            if (clientByKey.getIsActive().equals(Boolean.FALSE)) {
                clientByKey.setIsActive(true);
                try {
                    clientDAO.saveClient(clientByKey);
                } catch (BackendException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Impossible de mettre à jour le client apres activation de son compte", e);
                    }
                    return Constant.CODE_SERVICE_RETOUR_KO;
                }
            } else {
                return Constant.CODE_SERVICE_ANNONCE_RETOUR_DEJA_ACTIF;
            }

        } else {
            return Constant.CODE_SERVICE_ANNONCE_RETOUR_COMPTE_INEXISTANT;
        }

        return Constant.CODE_SERVICE_RETOUR_OK;
    }
}
