package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AdresseDAO {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdresseDAO.class);

	/**
	 * Methode de récuperation d'un client en fonction de son login.
	 * 
	 * @param String
	 *            Le login de l'utilsateur
	 * @return Client vide si l'utilisateur n'existe pas.
	 * @throws BackendException
	 */
	public void saveAdresse(Adresse nouvelleAdresse) throws BackendException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Debut persistence d'une nouvelle adresse......");
		}
		try {
			em.persist(nouvelleAdresse);
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
}
