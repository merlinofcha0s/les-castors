package fr.batimen.ws.dao;

import java.util.ArrayList;
import java.util.List;

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
import fr.batimen.ws.entity.Annonce;

/**
 * Controlleur d'utilisateurs
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

	public List<Annonce> getAnnoncesByLogin(String login) {
		return new ArrayList<Annonce>();
	}

	public void saveAnnonce(Annonce nouvelleAnnonce) throws BackendException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Debut persistence d'une nouvelle annonce......");
		}
		try {
			em.persist(nouvelleAnnonce);
		} catch (EntityExistsException eee) {

			StringBuilder sbError = new StringBuilder("Impossible de perister l'annonce ");
			sbError.append(nouvelleAnnonce.getTitre());
			sbError.append(" car elle existe déjà");

			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(sbError.toString(), eee);
			}

			throw new BackendException(sbError.toString());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin persistence d'une nouvelle annonce......OK");
		}
	}
}
