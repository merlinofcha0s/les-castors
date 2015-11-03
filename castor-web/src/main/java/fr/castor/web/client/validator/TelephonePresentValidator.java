package fr.castor.web.client.validator;

import fr.castor.dto.ClientDTO;
import fr.castor.dto.enums.TypeContact;
import fr.castor.web.app.security.Authentication;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de valider que le client a bien renseigné son numero de téléphone
 *
 * @author Casaucau Cyril
 */
@Named
public class TelephonePresentValidator implements IValidator<TypeContact> {

    @Inject
    private Authentication authentication;

    List<String> keys =  new ArrayList<>();

    public TelephonePresentValidator(){
        keys.add("error");

    }

    @Override
    public void validate(IValidatable<TypeContact> validatable) {
        TypeContact typeContactChoisi = validatable.getValue();

        if(authentication.isAuthenticated()){
            ClientDTO clientAuthentifie = authentication.getCurrentUserInfo();
            boolean naPasDeNumeroDeTelephone = clientAuthentifie.getNumeroTel() == null || clientAuthentifie.getNumeroTel().isEmpty();

            if(typeContactChoisi.equals(TypeContact.TELEPHONE) && naPasDeNumeroDeTelephone){
                ValidationError error = new ValidationError(this);
                validatable.error(error);
            }
        }


    }
}
