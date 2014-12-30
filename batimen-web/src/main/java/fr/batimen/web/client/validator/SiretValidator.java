package fr.batimen.web.client.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import fr.batimen.dto.helper.SiretValidatorHelper;

/**
 * Valide un siret
 * 
 * @author Casaucau Cyril
 * 
 */
public class SiretValidator implements IValidator<String> {

    private static final long serialVersionUID = -9143693623488715025L;

    @Override
    public void validate(IValidatable<String> validatable) {
        if (!SiretValidatorHelper.isSiretValide(validatable.getValue())) {
            ValidationError error = new ValidationError(this);
            error.setVariable("siret", validatable.getValue());
            validatable.error(decorate(error, validatable));
        }
    }

    /**
     * Allows subclasses to decorate reported errors
     * 
     * @param error
     * @param validatable
     * @return decorated error
     */
    protected ValidationError decorate(ValidationError error, IValidatable<String> validatable) {
        return error;
    }

}
