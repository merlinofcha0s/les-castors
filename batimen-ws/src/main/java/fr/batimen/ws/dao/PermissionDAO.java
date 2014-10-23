package fr.batimen.ws.dao;

import java.util.ArrayList;
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
import fr.batimen.ws.entity.Permission;

/**
 * Classe d'accés aux données pour les permissions
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "PermissionDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PermissionDAO extends AbstractDAO<Permission> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionDAO.class);

    /**
     * Methode de creation des permissions en base de données.
     * 
     * @param permission
     *            La permission a enregistré
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void creationPermission(Permission permission) {
        entityManager.persist(permission);
    }

    /**
     * Renvoi les roles pour un client donné
     * 
     * @param login
     *            Le login du client
     * @return
     */
    public List<Permission> getClientPermissions(String login) {
        List<Permission> permissions = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut récuperation des roles pour l'utilisateur : " + login);
        }

        try {
            TypedQuery<Permission> query = entityManager.createNamedQuery(QueryJPQL.PERMISSION_BY_LOGIN,
                    Permission.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            permissions = query.getResultList();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucun role trouvé pour l'utilisateur : " + login, nre);
            }
            return new ArrayList<Permission>();
        }
        return permissions;
    }

}
