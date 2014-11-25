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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.MesAnnoncesPageDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;
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
    private GestionAnnonceFacade gestionAnnonceFacade;

    @Inject
    private GestionUtilisateurFacade gestionUtilisationFacade;

    /**
     * Methode de récuperation des informations de la page de mes annonces
     * (notifications + annonces) d'un client
     * 
     * @param login
     * @return
     */
    @POST
    @Path(WsPath.GESTION_CLIENT_SERVICE_INFOS_MES_ANNONCES)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public MesAnnoncesPageDTO getInfoForMesAnnoncesPage(String login) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation des annonces / notifs pour : " + login);
        }

        MesAnnoncesPageDTO mesAnnoncesDTO = new MesAnnoncesPageDTO();
        String loginEscaped = DeserializeJsonHelper.parseString(login);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation notification.........");
        }

        List<NotificationDTO> notificationsDTO = gestionUtilisationFacade.getNotificationByLogin(loginEscaped,
                TypeCompte.CLIENT);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation annonce.........");
        }

        List<AnnonceDTO> annoncesDTO = gestionAnnonceFacade.getAnnonceByClientLogin(loginEscaped);

        mesAnnoncesDTO.setNotifications(notificationsDTO);
        mesAnnoncesDTO.setAnnonces(annoncesDTO);

        return mesAnnoncesDTO;
    }

}
