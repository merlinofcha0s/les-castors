package fr.castor.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class SuppressionCloseEvent extends AbstractEvent {

    public SuppressionCloseEvent(AjaxRequestTarget target) {
        super(target);
    }

}
