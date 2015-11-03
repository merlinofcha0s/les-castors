package fr.castor.ws.service;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import fr.castor.core.exception.EmailException;
import fr.castor.dto.NotificationDTO;
import fr.castor.dto.enums.StatutNotification;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.enums.TypeNotification;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.ws.dao.NotificationDAO;
import fr.castor.ws.entity.Annonce;
import fr.castor.ws.entity.Artisan;
import fr.castor.ws.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Inject
    private EmailService emailService;

    /**
     * Methode de récuperation des notifications d'un utilisateur
     * 
     * @param login
     * @return La liste de notification de l'utilisateur.
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<NotificationDTO> getNotificationByLogin(String login, TypeCompte typeCompte, Integer rangeDebutNotification, Integer rangeFinNotification) {
        List<NotificationDTO> notificationsDTO = new ArrayList<>();
        String loginEscaped = DeserializeJsonHelper.parseString(login);
        List<Object[]> notifications = null;

        Boolean clientOrArtisan = Boolean.FALSE;

        if (typeCompte.equals(TypeCompte.CLIENT)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query notification pour client en préparation");
            }
            notifications = notificationDAO.getNotificationForClient(loginEscaped, rangeDebutNotification, rangeFinNotification);
            clientOrArtisan = Boolean.TRUE;
        } else if (typeCompte.equals(TypeCompte.ARTISAN)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query notification pour artisan en préparation");
            }
            notifications = notificationDAO.getNotificationForArtisan(loginEscaped, rangeDebutNotification, rangeFinNotification);
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
                notificationDTO.setSiret(String.valueOf(notification[8]));

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

    /**
     * Génération d'une notification pour l'inscription d'un artisan
     * 
     * @param annonce
     *            l'annonce liée à la notif
     * @param artisan
     *            l'artisan qui sera notifié
     * @param typeCompte
     *            A qui est destinée la notification
     * @param typeNotification
     *            Le type de notification que l'on veut signaler
     * 
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void generationNotification(Annonce annonce, Artisan artisan, TypeCompte typeCompte,
                                       TypeNotification typeNotification) {

        Notification notification = new Notification();
        notification.setAnnonce(annonce);
        notification.setArtisanNotifier(artisan);
        notification.setClientNotifier(annonce.getDemandeur());
        notification.setDateNotification(new Date());
        notification.setPourQuiNotification(typeCompte);
        notification.setStatutNotification(StatutNotification.PAS_VUE);
        notification.setTypeNotification(typeNotification);

        notificationDAO.createMandatory(notification);

        try {
            emailService.envoiEmailNotification(notification);
        } catch (EmailException | MandrillApiError | IOException e) {
            if (LOGGER.isErrorEnabled()) {
                StringBuilder logError = new StringBuilder("Erreur lors de l'envoi du mail pour la notification : ");
                logError.append(" ").append(notification.getId()).append(" ")
                        .append(notification.getDateNotification());
                LOGGER.error(logError.toString(), e);
            }
        }
    }
}
