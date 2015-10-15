package fr.batimen.ws.dao;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.ws.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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
    public List<Object[]> getNotificationForClient(String login, Integer rangeDebutNotification, Integer rangeFinNotification) {
        return getNotificationsForUser(login, QueryJPQL.NOTIFICATION_BY_CLIENT_LOGIN, TypeCompte.CLIENT, rangeDebutNotification, rangeFinNotification);
    }

    /**
     * Retourne toutes les notifications concernant cet artisan
     * 
     * @param login
     *            Le login de l'artisan.
     * @return Toutes les notifications de l'artisan
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Object[]> getNotificationForArtisan(String login, Integer rangeDebutNotification, Integer rangeFinNotification) {
        return getNotificationsForUser(login, QueryJPQL.NOTIFICATION_BY_ARTISAN_LOGIN, TypeCompte.ARTISAN, rangeDebutNotification, rangeFinNotification);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private List<Object[]> getNotificationsForUser(String login, String queryName, TypeCompte typeCompte, Integer rangeDebutNotification, Integer rangeFinNotification) {
        List<Object[]> notificationFinded = null;

        try {
            TypedQuery<Object[]> query = entityManager.createNamedQuery(queryName, Object[].class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);
            query.setParameter(QueryJPQL.PARAM_TYPE_COMPTE, typeCompte);

            query.setFirstResult(rangeDebutNotification);
            query.setMaxResults(rangeFinNotification);

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
            return new ArrayList<>();
        }
    }
}
