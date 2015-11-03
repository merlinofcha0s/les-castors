package fr.castor.web.client.extend.nouveau.artisan.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.castor.web.client.event.AbstractEvent;

public class UncheckedEvent extends AbstractEvent {

    public UncheckedEvent(AjaxRequestTarget target) {
        super(target);
    }

}
