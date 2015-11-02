package fr.batimen.web.client.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Permet de valider qu'une checkbox est bien coché
 * 
 * @author Casaucau Cyril
 * 
 */
public class CheckBoxTrueValidator implements IValidator<Boolean> {

	private static final long serialVersionUID = 4753245876385663968L;

	@Override
	public void validate(IValidatable<Boolean> validateCheckBox) {
		boolean checkBoxValue = validateCheckBox.getValue();

		if (!checkBoxValue) {
			// Permet de rajouter des variables dans le feedback
			ValidationError error = new ValidationError(this);
			validateCheckBox.error(error);
		}

	}

}
