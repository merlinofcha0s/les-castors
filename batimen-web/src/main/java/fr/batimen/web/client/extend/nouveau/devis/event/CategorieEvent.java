package fr.batimen.web.client.extend.nouveau.devis.event;

import fr.batimen.dto.MotCleDTO;
import fr.batimen.web.client.event.AbstractEvent;
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

    public CategorieEvent(AjaxRequestTarget target, List<MotCleDTO> categoriesChoisies) {
        super(target);
        this.categoriesChoisies = categoriesChoisies;
    }

    public List<MotCleDTO> getCategoriesChoisies() {
        return categoriesChoisies;
    }
}