package fr.batimen.ws.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.ws.entity.Annonce;

/**
 * Classe d'accés aux données pour les annonces
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "AnnonceDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnnonceDAO {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceDAO.class);

	/**
	 * Recupere les annonces par login de leur utilsateurs
	 * 
	 * @param login
	 *            le login du client dont on veut recupérer les annonces.
	 * @return Liste d'annonces appartenant à l'utilisateur.
	 */
	@SuppressWarnings("unchecked")
	public List<Annonce> getAnnoncesByLogin(String login) {

		List<Annonce> listAnnonceByLogin = null;

		try {
			Query query = em.createNamedQuery(QueryJPQL.ANNONCE_BY_LOGIN);
			query.setParameter(QueryJPQL.CLIENT_LOGIN, login);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL OK ");
			}

			listAnnonceByLogin = Collections.checkedList(query.getResultList(), Annonce.class);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL OK ");
			}

			return listAnnonceByLogin;
		} catch (NoResultException nre) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new ArrayList<Annonce>();
		}
	}

	/**
	 * Recupere les annonces par titre, description et login : notament utilisé
	 * dans la verification de la duplication.
	 * 
	 * @param titre
	 *            Le titre de l'annonce.
	 * @param description
	 *            La description de l'annonce.
	 * @param login
	 *            Le login du cliebnt
	 * @return La liste d'annonce qui correspond au titre, description et
	 *         utilsateur present en BDD
	 */
	@SuppressWarnings("unchecked")
	public List<Annonce> getAnnonceByTitleAndDescriptionAndLogin(String titre, String description, String login) {

		List<Annonce> annoncesBytitreAndDescription = null;

		try {
			Query query = em.createNamedQuery(QueryJPQL.ANNONCE_BY_TITLE_AND_DESCRIPTION);
			query.setParameter(QueryJPQL.PARAM_ANNONCE_TITRE, titre);
			query.setParameter(QueryJPQL.PARAM_ANNONCE_DESCRIPTION, description);
			query.setParameter(QueryJPQL.CLIENT_LOGIN, login);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL OK ");
			}

			annoncesBytitreAndDescription = Collections.checkedList(query.getResultList(), Annonce.class);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL OK ");
			}

			return annoncesBytitreAndDescription;
		} catch (NoResultException nre) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new ArrayList<Annonce>();
		}
	}

	/**
	 * Sauvegarde d'une annonce, check dans la bdd si elle existe déjà pour un
	 * utilisateur donné.
	 * 
	 * @param nouvelleAnnonce
	 *            L'annonce a sauvegarder dans la bdd
	 * @throws DuplicateEntityException
	 *             Exception throw si l'entité existe déjà.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveAnnonce(Annonce nouvelleAnnonce) throws DuplicateEntityException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Debut persistence d'une nouvelle annonce......");
		}

		List<Annonce> annoncesDupliquees = getAnnonceByTitleAndDescriptionAndLogin(nouvelleAnnonce.getTitre(),
		        nouvelleAnnonce.getDescription(), nouvelleAnnonce.getDemandeur().getLogin());

		// On check si l'annonce n'existe pas déjà
		if (annoncesDupliquees.size() == 0) {
			em.persist(nouvelleAnnonce);
		} else {
			StringBuilder sbError = new StringBuilder("Impossible de perister l'annonce: ");
			sbError.append(nouvelleAnnonce.getTitre());
			sbError.append(" car elle existe déjà");

			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(sbError.toString());
			}

			throw new DuplicateEntityException(sbError.toString());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin persistence d'une nouvelle annonce......OK");
		}
	}
}
