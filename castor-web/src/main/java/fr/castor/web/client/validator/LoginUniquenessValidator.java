package fr.castor.web.client.validator;

import fr.castor.dto.ClientDTO;
import fr.castor.dto.LoginDTO;
import fr.castor.web.app.security.Authentication;
import fr.castor.ws.client.service.UtilisateurServiceREST;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Verifie que le login en cours d'inscription n'est pas deja présent en BDD
 *
 * @author Casaucau Cyril
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
            if (SecurityUtils.getSubject().isAuthenticated()) {
                ClientDTO infosClient = authentication.getCurrentUserInfo();
                super.validateField(this, login, infosClient.getLogin());
            } else {
                super.validateField(this, login, "");
            }
        }
    }
}
