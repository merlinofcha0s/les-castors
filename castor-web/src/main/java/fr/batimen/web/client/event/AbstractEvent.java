package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Represente un event qui pourra etre catcher par des composants
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class AbstractEvent implements Event {

	private final AjaxRequestTarget target;

	public AbstractEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

}
