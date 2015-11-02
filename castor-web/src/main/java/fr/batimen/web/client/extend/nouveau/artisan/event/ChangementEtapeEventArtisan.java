package fr.batimen.web.client.extend.nouveau.artisan.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.client.event.AbstractEvent;

/**
 * Event qui declenche le changement d'etape dans la page creation de nouveau
 * partenaire.
 * 
 * @author Casaucau Cyril
 * 
 */
public class ChangementEtapeEventArtisan extends AbstractEvent {

    private CreationPartenaireDTO nouveauPartenaire;

    public ChangementEtapeEventArtisan(AjaxRequestTarget target, CreationPartenaireDTO nouveauPartenaire) {
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
