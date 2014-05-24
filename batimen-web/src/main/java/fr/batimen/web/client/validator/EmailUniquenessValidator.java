package fr.batimen.web.client.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import fr.batimen.dto.ClientDTO;
import fr.batimen.ws.client.service.ClientService;

public class EmailUniquenessValidator implements IValidator<String> {

	private static final long serialVersionUID = 2423564239255893289L;

	@Override
	public void validate(IValidatable<String> email) {
		ClientDTO checkedClientEmail = ClientService.getClientByEmail(email.getValue());
		if (!checkedClientEmail.getEmail().isEmpty()) {
			// Permet de rajouter des variables dans le feedback
			ValidationError error = new ValidationError(this);
			email.error(error);
		}
	}

}
