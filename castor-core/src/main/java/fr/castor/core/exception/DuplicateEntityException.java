package fr.castor.core.exception;

/**
 * Exception lancée quand une entité dupliquée en base de données
 * 
 * @author Casaucau Cyril
 * 
 */
public class DuplicateEntityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1395240617337447812L;

	public DuplicateEntityException(String message) {
		super(message);
	}
}
