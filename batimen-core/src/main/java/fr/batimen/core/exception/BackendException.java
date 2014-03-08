package fr.batimen.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3333089006556066715L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndException.class);

	public BackendException(String message) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Erreur du webservice : " + message);
		}
	}

}
