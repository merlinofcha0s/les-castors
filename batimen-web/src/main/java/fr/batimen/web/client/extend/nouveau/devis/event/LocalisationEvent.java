package fr.batimen.web.client.extend.nouveau.devis.event;

import fr.batimen.web.app.utils.codepostal.LocalisationDTO;
import fr.batimen.web.client.event.AbstractEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by Casaucau on 21/06/2015.
 */
public class LocalisationEvent extends AbstractEvent {

    private final LocalisationDTO localisationDTO;

    public LocalisationEvent(AjaxRequestTarget target, LocalisationDTO localisationDTO) {
        super(target);
        this.localisationDTO = localisationDTO;
    }

    public LocalisationDTO getLocalisationDTO() {
        return localisationDTO;
    }
}
