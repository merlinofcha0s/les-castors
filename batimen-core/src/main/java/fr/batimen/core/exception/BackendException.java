package fr.batimen.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lancer quand il y a un probléme du coté du webservice
 * 
 * @author Casaucau Cyril
 * 
 */
public class BackendException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3333089006556066715L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendException.class);

	public BackendException(String message) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Erreur du webservice : " + message);
		}
	}

}
