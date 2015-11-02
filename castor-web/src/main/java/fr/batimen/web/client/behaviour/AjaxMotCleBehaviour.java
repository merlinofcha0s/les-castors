package fr.batimen.web.client.behaviour;

import fr.batimen.web.client.event.MotCleEvent;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

/**
 * Created by Casaucau on 24/08/2015.
 */
public class AjaxMotCleBehaviour extends AbstractDefaultAjaxBehavior {

    private String motCle;

    @Override
    protected void respond(AjaxRequestTarget target) {
        RequestCycle cycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) cycle.getRequest();
        // On extraie le departement de la requete AJAX
        motCle = webRequest.getQueryParameters().getParameterValue("motCle").toString();
        // On crée l'event qui sera envoyé a la page de nouveau devis
        MotCleEvent motCleEvent = new MotCleEvent(target);
        // On charge le mot clé
        motCleEvent.setMotCle(motCle);
        //On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, motCleEvent);
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
        // notre code js
        String script = super.getCallbackScript().toString();
        script = script.replace("\"PLACEHOLDER1\"", "motCle");
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
        // On crée un parametre motClé qui sera ajouté à la requete AJAX
        attributes.getExtraParameters().put("motCle", "PLACEHOLDER1");
    }

}