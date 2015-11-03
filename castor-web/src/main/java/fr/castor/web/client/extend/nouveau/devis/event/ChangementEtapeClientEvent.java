package fr.castor.web.client.extend.nouveau.devis.event;

import fr.castor.web.client.event.AbstractEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.castor.dto.aggregate.CreationAnnonceDTO;

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
