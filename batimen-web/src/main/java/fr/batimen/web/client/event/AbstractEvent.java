package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractEvent implements Event {

	private final AjaxRequestTarget target;

	public AbstractEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

}
