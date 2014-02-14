package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface Event {

	public AjaxRequestTarget getTarget();

}
