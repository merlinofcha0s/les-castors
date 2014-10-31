package fr.batimen.web.client.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.client.service.UtilisateurService;

/**
 * Verifie que le login en cours d'inscription n'est pas deja présent en BDD
 * 
 * @author Casaucau Cyril
 * 
 */
public class LoginUniquenessValidator implements IValidator<String> {

	private static final long serialVersionUID = -3658940352362936663L;

	@Override
	public void validate(IValidatable<String> login) {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setLogin(login.getValue());

		ClientDTO clientChecked = UtilisateurService.login(loginDTO);

		if (!clientChecked.getLogin().isEmpty()) {
			// Permet de rajouter des variables dans le feedback
			ValidationError error = new ValidationError(this);
			login.error(error);
		}
	}
}
