package fr.batimen.ws.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Notification;

/**
 * Classe d'accés aux données pour les notifications
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "NotificationDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificationDAO extends AbstractDAO<Notification> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDAO.class);

    /**
     * Retourne toutes les notifications concernant ce client
     * 
     * @param login
     *            Le login du client.
     * @return Toutes les notifications du client
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Object[]> getNotificationForClient(String login) {
        return getNotificationsForUser(login, QueryJPQL.NOTIFICATION_BY_CLIENT_LOGIN, TypeCompte.CLIENT);
    }

    /**
     * Retourne toutes les notifications concernant cet artisan
     * 
     * @param login
     *            Le login de l'artisan.
     * @return Toutes les notifications de l'artisan
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Object[]> getNotificationForArtisan(String login) {
        return getNotificationsForUser(login, QueryJPQL.NOTIFICATION_BY_ARTISAN_LOGIN, TypeCompte.ARTISAN);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private List<Object[]> getNotificationsForUser(String login, String queryName, TypeCompte typeCompte) {
        List<Object[]> notificationFinded = null;

        try {
            TypedQuery<Object[]> query = entityManager.createNamedQuery(queryName, Object[].class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);
            query.setParameter(QueryJPQL.PARAM_TYPE_COMPTE, typeCompte);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL notification login client OK ");
            }

            notificationFinded = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL notification login client OK ");
            }

            return notificationFinded;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune notifications présentes pour cet utilisateur", nre);
            }
            return new ArrayList<Object[]>();
        }
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createNotificationEntrepriseChoisiParClient(Annonce annonce) {
        Notification notification = new Notification();
        notification.setAnnonce(annonce);
        notification.setArtisanNotifier(annonce.getEntrepriseSelectionnee().getArtisan());
        notification.setClientNotifier(annonce.getDemandeur());
        notification.setDateNotification(new Date());
        notification.setPourQuiNotification(TypeCompte.ARTISAN);
        notification.setStatutNotification(StatutNotification.PAS_VUE);
        notification.setTypeNotification(TypeNotification.A_CHOISI_ENTREPRISE);
        entityManager.persist(notification);
    }
}
