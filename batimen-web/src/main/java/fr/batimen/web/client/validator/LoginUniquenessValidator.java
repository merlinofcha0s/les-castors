package fr.batimen.web.client.validator;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.ws.client.service.UtilisateurServiceREST;

/**
 * Verifie que le login en cours d'inscription n'est pas deja présent en BDD
 * 
 * @author Casaucau Cyril
 * 
 */
@Named
public class LoginUniquenessValidator extends AbstractUniquenessValidator implements IValidator<String> {

    private static final long serialVersionUID = -3658940352362936663L;

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

    @Inject
    private Authentication authentication;

    @Override
    public void validate(IValidatable<String> login) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(login.getValue());

        ClientDTO clientChecked = utilisateurServiceREST.login(loginDTO);

        if (!clientChecked.getLogin().isEmpty()) {
            ClientDTO infosClient = authentication.getCurrentUserInfo();
            super.validateField(this, login, infosClient.getLogin());
        }
    }
}
