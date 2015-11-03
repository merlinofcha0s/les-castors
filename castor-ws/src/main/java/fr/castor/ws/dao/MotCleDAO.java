package fr.castor.ws.dao;

import fr.castor.ws.entity.MotCle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.Set;

/**
 * Created by Casaucau on 25/08/2015.
 */
@Stateless(name = "MotCleDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotCleDAO extends AbstractDAO<MotCle> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MotCleDAO.class);

    /**
     * Enregistre les mot clés metiers des annonces dans la base de
     * données.
     *
     * @param motCle
     */
    public void persistMotCle(MotCle motCle) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Persistence d'une nouvelle catégorie");
        }
        entityManager.persist(motCle);
    }

    /**
     * Efface tous les mots clés contenu dans le Set de la base de données.
     *
     * @param motsCles La liste à effacer.
     */
    public void deleteAllMotsCles(Set<MotCle> motsCles) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Suppression de tous les mots clés contenu dans le Set");
        }
        motsCles.forEach(motCle -> delete(motCle));
    }
}