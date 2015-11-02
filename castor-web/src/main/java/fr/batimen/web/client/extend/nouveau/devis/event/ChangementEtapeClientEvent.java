package fr.batimen.web.client.extend.nouveau.devis.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.web.client.event.AbstractEvent;

/**
 * Event qui declenche le changement d'etape dans la page creation d'une
 * nouvelle annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class ChangementEtapeClientEvent extends AbstractEvent {

    private CreationAnnonceDTO nouvelleAnnonce;

    public ChangementEtapeClientEvent(AjaxRequestTarget target, CreationAnnonceDTO nouvelleAnnonce) {
        super(target);
        this.nouvelleAnnonce = nouvelleAnnonce;
    }

    /**
     * @return the nouvelleAnnonce
     */
    public CreationAnnonceDTO getNouvelleAnnonce() {
        return nouvelleAnnonce;
    }

    /**
     * @param nouvelleAnnonce
     *            the nouvelleAnnonce to set
     */
    public void setNouvelleAnnonce(CreationAnnonceDTO nouvelleAnnonce) {
        this.nouvelleAnnonce = nouvelleAnnonce;
    }

}
