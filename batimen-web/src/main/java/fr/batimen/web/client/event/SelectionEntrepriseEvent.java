package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.EntrepriseDTO;

public class SelectionEntrepriseEvent extends AbstractEvent {

    private final EntrepriseDTO entreprise;

    public SelectionEntrepriseEvent(AjaxRequestTarget target, EntrepriseDTO entreprise) {
        super(target);
        this.entreprise = entreprise;
    }

    /**
     * @return the entreprise
     */
    public EntrepriseDTO getEntreprise() {
        return entreprise;
    }

}
