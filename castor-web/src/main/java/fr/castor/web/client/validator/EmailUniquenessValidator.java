package fr.castor.web.client.validator;

import fr.castor.dto.ClientDTO;
import fr.castor.web.app.security.Authentication;
import fr.castor.ws.client.service.UtilisateurServiceREST;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Verifie que l'email en cours d'inscription n'est pas deja présent en BDD
 *
 * @author Casaucau Cyril
 */
@Named
public class EmailUniquenessValidator extends AbstractUniquenessValidator implements IValidator<String> {

    private static final long serialVersionUID = 2423564239255893289L;

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

    @Inject
    private Authentication authentication;

    @Override
    public void validate(IValidatable<String> email) {
        ClientDTO checkedClientEmail = utilisateurServiceREST.getUtilisateurByEmail(email.getValue());
        if (!checkedClientEmail.getEmail().isEmpty()) {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                ClientDTO infosClient = authentication.getCurrentUserInfo();
                super.validateField(this, email, infosClient.getEmail());
            } else {
                super.validateField(this, email, "");
            }
        }
    }
}
