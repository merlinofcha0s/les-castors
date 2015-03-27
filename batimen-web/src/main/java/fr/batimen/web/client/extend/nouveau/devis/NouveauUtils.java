package fr.batimen.web.client.extend.nouveau.devis;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;

import fr.batimen.web.client.event.CastorWizardEvent;

public class NouveauUtils {

    public static void sendEventForPreviousStep(AjaxRequestTarget target, Integer etapeEnCours) {
        CastorWizardEvent castorWizardEvent = new CastorWizardEvent(target);
        castorWizardEvent.setStepNumber(String.valueOf(etapeEnCours - 1));
        target.getPage().send(target.getPage(), Broadcast.BREADTH, castorWizardEvent);
    }

}
