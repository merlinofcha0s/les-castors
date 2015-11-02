package fr.batimen.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Exception special pour les problemes liés à l'ihm des DTOS
 * 
 * @author Casaucau Cyril
 * 
 */
public class FrontEndException extends Exception {

	private static final long serialVersionUID = -7416978359691265975L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndException.class);

	public FrontEndException(String message) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Erreur dans l'IHM : " + message);
		}
	}

}
