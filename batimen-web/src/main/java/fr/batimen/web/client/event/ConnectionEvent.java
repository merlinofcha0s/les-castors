package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ConnectionEvent extends AbstractEvent {

	public ConnectionEvent(AjaxRequestTarget target) {
		super(target);
	}

}
