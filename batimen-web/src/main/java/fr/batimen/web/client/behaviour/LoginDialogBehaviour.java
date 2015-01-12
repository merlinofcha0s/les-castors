package fr.batimen.web.client.behaviour;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;

import fr.batimen.web.client.event.LoginDialogEvent;

public class LoginDialogBehaviour extends AbstractDefaultAjaxBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8377788715415613479L;

	@Override
	protected void respond(AjaxRequestTarget target) {
		// broadcast event on page		
		target.getPage().send(target.getPage(), Broadcast.BREADTH, new LoginDialogEvent(target));
	}

}
