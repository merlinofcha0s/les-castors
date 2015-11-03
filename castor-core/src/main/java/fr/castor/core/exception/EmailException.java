package fr.castor.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailException extends BackendException {

    /**
     * 
     */
    private static final long serialVersionUID = 2629518045577037698L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailException.class);

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, String email, String status) {
        this(message);
        if (LOGGER.isErrorEnabled()) {
            StringBuilder emailError = new StringBuilder("Problème d'envoi au destinataire suivant: ");
            emailError.append(email).append(" ,Status:").append(status);
            LOGGER.error(emailError.toString());
        }
    }
}
