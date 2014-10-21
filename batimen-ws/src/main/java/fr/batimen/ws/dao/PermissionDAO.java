package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

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

}
