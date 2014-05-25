package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Interface de tous les events
 * 
 * @author Casaucau
 * 
 */
public interface Event {

	public AjaxRequestTarget getTarget();

}
