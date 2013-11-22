package fr.batimen.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Exception special pour la validation des DTOS
 * 
 * @author Casaucau Cyril
 * 
 */
public class ValidationDTOBatimenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2612619518897303204L;

	private static final Logger logger = LoggerFactory.getLogger(ValidationDTOBatimenException.class);

	public ValidationDTOBatimenException(String message) {
		if (logger.isErrorEnabled()) {
			logger.error("Erreur dans la validation des DTOs : " + message);
		}
	}

}
