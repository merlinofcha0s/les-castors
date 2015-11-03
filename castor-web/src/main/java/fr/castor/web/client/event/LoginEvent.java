package fr.castor.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Cet evenement est envoyé lorsque l'utilisateur se log ou est logger ce qui
 * permet de sauter l'etape 3 dans la création d'annonce
 * 
 * @author Casaucau Cyril
 * 
 */
public class LoginEvent extends AbstractEvent {

	public LoginEvent(AjaxRequestTarget target) {
		super(target);
	}

}
