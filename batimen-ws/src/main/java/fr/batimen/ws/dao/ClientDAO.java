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
import fr.batimen.ws.entity.Client;

/**
 * Controlleur d'utilisateurs
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
				LOGGER.debug("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new Client();
		}
	}
}
