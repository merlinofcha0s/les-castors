package fr.batimen.ws.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.NotificationDAO;

/**
 * Classe de gestion des notifications
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "NotificationService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Inject
    private NotificationDAO notificationDAO;

    /**
     * Methode de récuperation des notifications d'un utilisateur
     * 
     * @param login
     * @return La liste de notification de l'utilisateur.
     */
    @POST
    @Path(WsPath.GESTION_UTILISATEUR_SERVICE_NOTIFICATION)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<NotificationDTO> getNotificationByLogin(String login, TypeCompte typeCompte) {
        List<NotificationDTO> notificationsDTO = new ArrayList<NotificationDTO>();
        String loginEscaped = DeserializeJsonHelper.parseString(login);
        List<Object[]> notifications = null;

        Boolean clientOrArtisan = Boolean.FALSE;

        if (typeCompte.equals(TypeCompte.CLIENT)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query notification pour client en préparation");
            }
            notifications = notificationDAO.getNotificationForClient(loginEscaped);
            clientOrArtisan = Boolean.TRUE;
        } else if (typeCompte.equals(TypeCompte.ARTISAN)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query notification pour artisan en préparation");
            }
            notifications = notificationDAO.getNotificationForArtisan(loginEscaped);
            clientOrArtisan = Boolean.TRUE;
        }

        if (clientOrArtisan) {
            for (Object[] notification : notifications) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Génération de la DTO de notification");
                }
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setTypeNotification((TypeNotification) notification[0]);
                notificationDTO.setDateNotification((Timestamp) notification[1]);
                notificationDTO.setStatutNotification((StatutNotification) notification[2]);
                notificationDTO.setPourQuiNotification((TypeCompte) notification[3]);
                notificationDTO.setArtisanLogin(String.valueOf(notification[4]));
                notificationDTO.setClientLogin(String.valueOf(notification[5]));
                notificationDTO.setNomEntreprise(String.valueOf(notification[6]));
                notificationDTO.setHashIDAnnonce(String.valueOf(notification[7]));

                notificationsDTO.add(notificationDTO);
            }
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(login + " n'est ni client ni artisan, cas impossible");
                LOGGER.error("Type de compte : " + typeCompte);
            }
        }
        return notificationsDTO;
    }
}
