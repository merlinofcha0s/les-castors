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

    /**
     * Sauvegarde d'un artisan dans la BDD
     * 
     * @param nouveauArtisan
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveArtisan(Artisan nouveauArtisan) {
        entityManager.persist(nouveauArtisan);
    }

    /**
     * Récuperation d"un artisan grace à son Email
     * 
     * @param email
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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

    /**
     * Récupération d'un artisan grace à son Login
     * 
     * @param login
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Artisan getArtisanByLogin(String login) {

        Artisan artisanTrouve = null;

        try {
            TypedQuery<Artisan> query = entityManager.createNamedQuery(QueryJPQL.ARTISAN_BY_LOGIN, Artisan.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            artisanTrouve = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL artisan by login OK ");
            }

            return artisanTrouve;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new Artisan();
        }
    }

    /**
     * Récupêration du Hash d'un artisan : sert pour l'authentifier
     * 
     * @param login
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getHash(String login) {
        String hash = null;

        try {
            TypedQuery<String> query = entityManager.createNamedQuery(QueryJPQL.ARTISAN_HASH_BY_LOGIN, String.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            hash = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL hash for artisan OK ");
            }

            return hash;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return "";
        }
    }

    /**
     * Renvoi le statut pour un artisan donné
     * 
     * @param login
     *            Le login du client
     * @return
     */
    public Boolean getStatutActive(String login) {
        Boolean isActive = Boolean.FALSE;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut récuperation statut pour l'artisan : " + login);
        }

        try {
            TypedQuery<Boolean> query = entityManager
                    .createNamedQuery(QueryJPQL.ARTISAN_STATUT_BY_LOGIN, Boolean.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            isActive = query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées pour le statut de l'artisan : " + login, nre);
            }
            return Boolean.FALSE;
        }
        return isActive;
    }

    /**
     * Récuperation d'un artisan grace a sa clé d'activation. <br/>
     * Sa clé lui est transmise par mail et elle sert a activé son compte une
     * fois qu'il s'est inscrit sur le site
     * 
     * @param activationKey
     * @return
     */
    public Artisan getArtisanByActivationKey(String activationKey) {

        Artisan artisanFinded = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut récuperation compte artisan avec la clé : " + activationKey);
        }

        try {
            TypedQuery<Artisan> query = entityManager.createNamedQuery(QueryJPQL.ARTISAN_BY_ACTIVATION_KEY,
                    Artisan.class);
            query.setParameter(QueryJPQL.PARAM_ACTIVATION_KEY, activationKey);

            artisanFinded = query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD pour la clé: " + activationKey, nre);
            }
            return new Artisan();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin récuperation compte artisan avec la clé");
        }

        return artisanFinded;

    }

}
