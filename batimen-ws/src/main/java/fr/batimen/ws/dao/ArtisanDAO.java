package fr.batimen.ws.dao;

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
import fr.batimen.ws.entity.Artisan;

/**
 * Classe d'accés aux données pour les artisans
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "ArtisanDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArtisanDAO extends AbstractDAO<Artisan> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanDAO.class);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveArtisan(Artisan nouveauArtisan) {
        entityManager.persist(nouveauArtisan);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Artisan getArtisanByEmail(String email) {

        Artisan artisanTrouve = null;

        try {
            TypedQuery<Artisan> query = entityManager.createNamedQuery(QueryJPQL.ARTISAN_BY_EMAIL, Artisan.class);
            query.setParameter(QueryJPQL.PARAM_ARTISAN_EMAIL, email);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            artisanTrouve = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL artisan by email OK ");
            }

            return artisanTrouve;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new Artisan();
        }
    }

}
