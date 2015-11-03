package fr.castor.ws.dao;

import fr.castor.core.constant.QueryJPQL;
import fr.castor.dto.enums.StatutJuridique;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.ws.entity.Entreprise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_SIRET, siret);

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
     * @param login Identifiant de l'artisan
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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

    /**
     * Récupère les informations d'une entreprise grace a plusieurs parametres.
     *
     * @param nomComplet
     * @param statutJuridique
     * @param siret
     * @param departement
     * @return L'entité de l'entreprise.
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Entreprise getArtisanByNomCompletSatutSiretDepartement(String nomComplet, StatutJuridique statutJuridique
            , String siret, Integer departement) {
        try {
            TypedQuery<Entreprise> query = entityManager.createNamedQuery(QueryJPQL.ENTREPRISE_BY_NOM_COMPLET_STATUT_SIRET_DEPARTEMENT,
                    Entreprise.class);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_NOM_COMPLET, nomComplet);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_SIRET, siret);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_STATUT_JURIDIQUE, statutJuridique);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_DEPARTEMENT, departement);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL entreprise by login ByNomCompletSatutSiretDepartement ");
            }

            return query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }
}