package fr.batimen.web.client.behaviour;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.extend.nouveau.devis.event.MapFranceEvent;

/**
 * Behaviour qui crée, envoi et recupere une requete qjax qui a pour but de
 * récuperer le numero de département selectionné par l'utilisateur grace à la
 * carte de france générée en JS
 * 
 * @author Casaucau Cyril
 * 
 * @see MapFrance
 * 
 */
public class AjaxMapFranceBehaviour extends AbstractDefaultAjaxBehavior {

    private static final long serialVersionUID = 4938828168535196182L;

    private String departementSelectionne;

    @Override
    protected void respond(AjaxRequestTarget target) {
        RequestCycle cycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) cycle.getRequest();
        // On extraie le departement de la requete AJAX
        departementSelectionne = webRequest.getQueryParameters().getParameterValue("departement").toString();
        // On crée l'event qui sera envoyé a la page de nouveau devis
        MapFranceEvent mapFranceEvent = new MapFranceEvent(target);
        // On charge le département
        mapFranceEvent.setDepartement(departementSelectionne);
        // On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, mapFranceEvent);
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
        script = script.replace("\"PLACEHOLDER1\"", "departementValue");
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
        attributes.getExtraParameters().put("departement", "PLACEHOLDER1");
    }

}
