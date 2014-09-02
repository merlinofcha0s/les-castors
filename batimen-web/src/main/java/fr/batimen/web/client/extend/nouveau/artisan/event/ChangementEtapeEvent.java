package fr.batimen.web.client.extend.nouveau.artisan.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.client.event.AbstractEvent;

public class ChangementEtapeEvent extends AbstractEvent {

    private CreationPartenaireDTO nouveauPartenaire;

    public ChangementEtapeEvent(AjaxRequestTarget target, CreationPartenaireDTO nouveauPartenaire) {
        super(target);
        this.nouveauPartenaire = nouveauPartenaire;
    }

    /**
     * @return the nouveauPartenaire
     */
    public CreationPartenaireDTO getNouveauPartenaire() {
        return nouveauPartenaire;
    }

    /**
     * @param nouveauPartenaire
     *            the nouveauPartenaire to set
     */
    public void setNouveauPartenaire(CreationPartenaireDTO nouveauPartenaire) {
        this.nouveauPartenaire = nouveauPartenaire;
    }

}
