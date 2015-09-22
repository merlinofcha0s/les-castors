package fr.batimen.web.client.validator;

import fr.batimen.dto.LocalisationDTO;
import fr.batimen.web.app.utils.codepostal.CSVCodePostalReader;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import javax.inject.Inject;
import java.util.List;

/**
 * Validator qui verifie que la ville founit correspond au code postal
 *
 * @author Cyril Casaucau
 */
public class VilleValidator implements IValidator<String> {

    @Inject
    private CSVCodePostalReader csvCodePostalReader;

    private FormComponent<String> codepostalField;

    @Override
    public void validate(IValidatable<String> validatable) {
        String ville = validatable.getValue();
        List<LocalisationDTO> localisationDTOs = csvCodePostalReader.getLocalisationDTOs().get(codepostalField.getConvertedInput());
        boolean villeCorrespondsCodePostal = false;

        if (localisationDTOs != null) {
            for (LocalisationDTO localisationDTO : localisationDTOs) {
                if (localisationDTO.getCommune().equalsIgnoreCase(ville)) {
                    villeCorrespondsCodePostal = true;
                }
            }
        }


        if (!villeCorrespondsCodePostal) {
            ValidationError error = new ValidationError(this);
            error.setVariable("ville", validatable.getValue());
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

    public void setCodepostalField(FormComponent<String> codepostalField) {
        this.codepostalField = codepostalField;
    }
}
