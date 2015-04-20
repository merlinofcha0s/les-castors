package fr.batimen.web.client.event;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Event qui declenche l'appel au service de mise à jour de l'annonce
 *
 * Created by Casaucau on 20/04/2015.
 *
 * @author Casaucau Cyril
 */
public class ModificationAnnonceEvent extends AbstractEvent{

    private CreationAnnonceDTO creationAnnonceDTO;

    public ModificationAnnonceEvent(AjaxRequestTarget target, CreationAnnonceDTO creationAnnonceDTO) {
        super(target);
        this.creationAnnonceDTO = creationAnnonceDTO;
    }
}
