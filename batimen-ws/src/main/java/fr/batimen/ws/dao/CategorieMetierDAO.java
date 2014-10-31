package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.ws.entity.CategorieMetier;

/**
 * Classe d'accés aux données des catégories métier
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "CategorieMetierDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CategorieMetierDAO extends AbstractDAO<CategorieMetier> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorieMetierDAO.class);

    /**
     * Enregistre les catégories metiers des entreprises dans la base de
     * données.
     * 
     * @param nouvelleCategorieMetier
     */
    public void persistCategorieMetier(CategorieMetier nouvelleCategorieMetier) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Persistence d'une nouvelle catégorie");
        }
        entityManager.persist(nouvelleCategorieMetier);
    }

}
