package fr.castor.web.client.behaviour;

import fr.castor.web.client.event.LoginDialogEvent;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;

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
