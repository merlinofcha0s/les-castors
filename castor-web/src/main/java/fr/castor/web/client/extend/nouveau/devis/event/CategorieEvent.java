package fr.castor.web.client.extend.nouveau.devis.event;

import fr.castor.dto.MotCleDTO;
import fr.castor.web.client.event.AbstractEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

/**
 * Evenement qui déclenche le passage de l'etape 2 à l'étape 3 dans la creation
 * de nouveau devis. Indique également la liste des categories choisies.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CategorieEvent extends AbstractEvent {

    private final List<MotCleDTO> categoriesChoisies;
    private final boolean goToTheNextStep;

    public CategorieEvent(AjaxRequestTarget target, List<MotCleDTO> categoriesChoisies, boolean goToTheNextStep) {
        super(target);
        this.categoriesChoisies = categoriesChoisies;
        this.goToTheNextStep = goToTheNextStep;
    }

    public List<MotCleDTO> getCategoriesChoisies() {
        return categoriesChoisies;
    }

    public boolean isGoToTheNextStep() {
        return goToTheNextStep;
    }
}