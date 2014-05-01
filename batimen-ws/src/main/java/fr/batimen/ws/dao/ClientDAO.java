package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.ws.entity.Client;

/**
 * Classe d'accés aux données pour les clients
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "ClientDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClientDAO {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDAO.class);

	/**
	 * Methode de récuperation d'un client en fonction de son login.
	 * 
	 * @param String
	 *            Le login de l'utilsateur
	 * @return Client vide si l'utilisateur n'existe pas.
	 */
	public Client getClientByLoginName(String login) {

		Client clientFinded = null;

		try {
			Query query = em.createNamedQuery(QueryJPQL.CLIENT_LOGIN);
			query.setParameter(QueryJPQL.CLIENT_LOGIN, login);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL OK ");
			}

			clientFinded = (Client) query.getSingleResult();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL OK ");
			}

			return clientFinded;
		} catch (NoResultException nre) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new Client();
		}
	}

	/**
	 * Methode de récuperation d'un client en fonction de son email (utilisé
	 * notament dans la verification de la duplication)
	 * 
	 * @param email
	 *            L'email du client
	 * @return Le client qui a le mail passé en param.
	 */
	public Client getClientByEmail(String email) {

		Client clientFinded = null;

		try {
			Query query = em.createNamedQuery(QueryJPQL.CLIENT_BY_EMAIL);
			query.setParameter(QueryJPQL.PARAM_CLIENT_EMAIL, email);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL client email OK ");
			}

			clientFinded = (Client) query.getSingleResult();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL client email OK ");
			}

			return clientFinded;
		} catch (NoResultException nre) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new Client();
		}
	}

	/**
	 * Sauvegarde un client dans la base de données et check si l'utilisateur
	 * existe deja
	 * 
	 * @param nouveauClient
	 *            L'entité a ajouter dans la BDD
	 * @throws BackendException
	 */
	public void saveClient(Client nouveauClient) throws DuplicateEntityException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Debut persistence d'un nouveau client......");
		}

		// Test paranoiaque n'est jamais censé arrivé......il a normalement été
		// checké par le frontend
		Client clientMemeLogin = getClientByLoginName(nouveauClient.getLogin());
		Client clientMemeEmail = getClientByEmail(nouveauClient.getEmail());

		if ("".equals(clientMemeLogin.getLogin()) && "".equals(clientMemeEmail.getLogin())) {
			em.persist(nouveauClient);
		} else {
			StringBuilder sbError = new StringBuilder("Impossible de perister le client : ");
			sbError.append(nouveauClient.getLogin());
			sbError.append(" - ");
			sbError.append(nouveauClient.getEmail());
			sbError.append(" car il existe déjà");
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(sbError.toString());
			}

			throw new DuplicateEntityException(sbError.toString());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin persistence d'un nouveau client......OK");
		}
	}
}
