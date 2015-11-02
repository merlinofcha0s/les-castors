package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class SuppressionOpenEvent extends AbstractEvent {

    public SuppressionOpenEvent(AjaxRequestTarget target) {
        super(target);
    }

}
