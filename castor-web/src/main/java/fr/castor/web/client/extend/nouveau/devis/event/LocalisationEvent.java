package fr.castor.web.client.extend.nouveau.devis.event;

import fr.castor.dto.LocalisationDTO;
import fr.castor.web.client.event.AbstractEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

/**
 * Event wicket qui declenche l'envoi de la DTO conternant les informations de localisation.
 *
 * @author Casaucau Cyril
 */
public class LocalisationEvent extends AbstractEvent {

    private List<LocalisationDTO> localisationDTOMemeCodePostal;
    private String codePostal;

    public LocalisationEvent(AjaxRequestTarget target, List<LocalisationDTO> localisationDTO) {
        super(target);
        this.localisationDTOMemeCodePostal = localisationDTO;
    }

    public LocalisationEvent(AjaxRequestTarget target, String codePostal) {
        super(target);
        this.codePostal = codePostal;
    }

    public List<LocalisationDTO> getLocalisationDTOMemeCodePostal() {
        return localisationDTOMemeCodePostal;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}