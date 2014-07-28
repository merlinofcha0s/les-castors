package fr.batimen.web.client.extend.nouveau.devis.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.web.client.event.AbstractEvent;

/**
 * Evenement qui déclenche le passage de l'etape 2 à l'étape 3 dans la creation
 * de nouveau devis. Indique également la categorie choisie.
 * 
 * @author Casaucau Cyril
 * 
 */
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
