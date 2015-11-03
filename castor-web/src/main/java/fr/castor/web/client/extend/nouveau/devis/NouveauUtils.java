package fr.castor.web.client.extend.nouveau.devis;

import fr.castor.web.client.event.CastorWizardEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;

public class NouveauUtils {

    private NouveauUtils() {
        super();
    }

    public static void sendEventForPreviousStep(AjaxRequestTarget target, Integer etapeEnCours) {
        CastorWizardEvent castorWizardEvent = new CastorWizardEvent(target);
        castorWizardEvent.setStepNumber(String.valueOf(etapeEnCours - 1));
        target.getPage().send(target.getPage(), Broadcast.BREADTH, castorWizardEvent);
    }

}
