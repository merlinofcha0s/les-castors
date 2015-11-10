package fr.castor.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by Casaucau on 24/08/2015.
 */
public class MotCleEvent extends AbstractEvent {

    private String motCle;

    public MotCleEvent(AjaxRequestTarget target) {
        super(target);
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }
}
