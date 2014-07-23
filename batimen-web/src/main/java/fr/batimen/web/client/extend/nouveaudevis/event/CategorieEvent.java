package fr.batimen.web.client.extend.nouveaudevis.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.web.client.event.AbstractEvent;

public class CategorieEvent extends AbstractEvent {

    private final CategorieMetierDTO categorieChoisie;

    public CategorieEvent(AjaxRequestTarget target, CategorieMetierDTO categorieChoisie) {
        super(target);
        this.categorieChoisie = categorieChoisie;
    }

    /**
     * @return the categorieChoisie
     */
    public CategorieMetierDTO getCategorieChoisie() {
        return categorieChoisie;
    }

}
