package fr.castor.web.client.behaviour;

import fr.castor.web.client.event.CastorWizardEvent;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

public class AjaxCastorWizardBehaviour extends AbstractDefaultAjaxBehavior {

    private static final long serialVersionUID = 1L;

    private String stepNumber;

    @Override
    protected void respond(AjaxRequestTarget target) {
        RequestCycle cycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) cycle.getRequest();
        // On extraie le departement de la requete AJAX
        stepNumber = webRequest.getQueryParameters().getParameterValue("stepNumber").toString();
        // On crée l'event qui sera envoyé a la page de nouveau devis
        CastorWizardEvent castorWizardEvent = new CastorWizardEvent(target);
        // On charge le département
        castorWizardEvent.setStepNumber(stepNumber);
        // On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, castorWizardEvent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#getCallbackScript()
     */
    @Override
    public CharSequence getCallbackScript() {
        // On remplace la valeur que l'on a mis dans la méthode
        // updateAjaxAttribute (PLACEHOLDER1) par le nom de la variable dans
        // notre code js (Mapfrance)
        String script = super.getCallbackScript().toString();
        script = script.replace("\"PLACEHOLDER1\"", "stepNumber");
        return script;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#updateAjaxAttributes
     * (org.apache.wicket.ajax.attributes.AjaxRequestAttributes)
     */
    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        // On crée un parametre departement qui sera ajouté à la requete AJAX
        attributes.getExtraParameters().put("stepNumber", "PLACEHOLDER1");
    }

}
