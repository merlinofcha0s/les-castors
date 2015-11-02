package fr.batimen.web.client.validator;

import fr.batimen.dto.ClientDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Classe abstraite pour le validateur qui s'assure de l'unicité de certaines
 * valeurs clientes.
 * 
 * @author Casaucau Cyril
 * 
 */
public class AbstractUniquenessValidator {

    public void validateField(IValidator<String> child, IValidatable<String> field, String infoToValidate) {
        // Si le client est connecté
        if (SecurityUtils.getSubject().isAuthenticated()) {
            // Si jamais l'information que l'on valide n'est pas égale cela veut
            // dire par exemple qu'il essaye de changer son login mais qu'il en
            // a choisi un qui existe deja en BDD
            if (!infoToValidate.equals(field.getValue())) {
                // Permet de rajouter des variables dans le feedback
                ValidationError error = new ValidationError(child);
                field.error(error);
            }
        } else {
            // Si il n'est pas connecter il y a une erreur vu que le ws a
            // ramener un client
            ValidationError error = new ValidationError(child);
            field.error(error);
        }
    }

}
