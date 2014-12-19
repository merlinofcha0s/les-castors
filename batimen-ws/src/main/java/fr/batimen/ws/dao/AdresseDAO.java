package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.exception.BackendException;
import fr.batimen.ws.entity.Adresse;

/**
 * Classe d'accés aux données pour les adresses
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "AdresseDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AdresseDAO extends AbstractDAO<Adresse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdresseDAO.class);

    /**
     * Methode de récuperation d'un client en fonction de son login.
     * 
     * @param String
     *            Le login de l'utilsateur
     * @return Client vide si l'utilisateur n'existe pas.
     * @throws BackendException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAdresse(Adresse nouvelleAdresse) throws BackendException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut persistence d'une nouvelle adresse......");
        }
        try {
            entityManager.persist(nouvelleAdresse);
        } catch (EntityExistsException eee) {

            StringBuilder sbError = new StringBuilder("Impossible de perister l'adresse: ");
            sbError.append(nouvelleAdresse.getAdresse());
            sbError.append(" - ");
            sbError.append(nouvelleAdresse.getComplementAdresse());
            sbError.append(" - ");
            sbError.append(nouvelleAdresse.getCodePostal());
            sbError.append(" car elle existe déjà");

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(sbError.toString(), eee);
            }

            throw new BackendException(sbError.toString());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin persistence d'une nouvelle adresse......OK");
        }
    }

    /**
     * Récupération d'une entreprise grace au login de l'artisan <br/>
     * 
     * @param login
     *            Identifiant de l'artisan
     * @return
     */
    public Adresse getAdresseByNomCompletEntreprise(String idEntreprise) {

        Adresse adresseTrouvee = null;

        try {
            TypedQuery<Adresse> query = entityManager.createNamedQuery(QueryJPQL.ADRESSE_BY_NOM_COMPLET_ENTREPRISE,
                    Adresse.class);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_NOM_COMPLET, idEntreprise);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL adresse by entreprise ID OK ");
            }

            adresseTrouvee = query.getSingleResult();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL adresse by entreprise ID OK ");
            }

            return adresseTrouvee;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new Adresse();
        }
    }
}
