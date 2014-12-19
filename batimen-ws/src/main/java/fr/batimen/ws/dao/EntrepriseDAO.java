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
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.entity.Entreprise;

/**
 * Classe d'accés aux données pour les entreprises.
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "EntrepriseDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EntrepriseDAO extends AbstractDAO<Entreprise> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntrepriseDAO.class);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveEntreprise(Entreprise nouvelleEntreprise) {
        entityManager.persist(nouvelleEntreprise);
    }

    /**
     * Récupération d'une entreprise grace à son SIRET <br/>
     * Utile pour verifier si il n'y a pas de doublon lors de l'inscription
     * d'une entreprise
     * 
     * @param siret
     * @return
     */
    public Entreprise getEntrepriseBySiret(String siret) {

        Entreprise entrepriseTrouvee = null;

        try {
            TypedQuery<Entreprise> query = entityManager.createNamedQuery(QueryJPQL.ENTREPRISE_BY_SIRET,
                    Entreprise.class);
            query.setParameter(QueryJPQL.PARAM_ENTEPRISE_SIRET, siret);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL entreprise by siret OK ");
            }

            entrepriseTrouvee = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL entreprise by siret OK ");
            }

            return entrepriseTrouvee;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new Entreprise();
        }
    }

    /**
     * Récupération d'une entreprise grace au login de l'artisan <br/>
     * 
     * @param login
     *            Identifiant de l'artisan
     * @return
     */
    public Entreprise getEntrepriseByArtisan(String login) {

        String loginEscaped = DeserializeJsonHelper.parseString(login);

        Entreprise entrepriseTrouvee = null;

        try {
            TypedQuery<Entreprise> query = entityManager.createNamedQuery(QueryJPQL.ENTREPRISE_BY_ARTISAN,
                    Entreprise.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, loginEscaped);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL entreprise by login OK ");
            }

            entrepriseTrouvee = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL entreprise by login OK ");
            }

            return entrepriseTrouvee;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new Entreprise();
        }
    }

}
