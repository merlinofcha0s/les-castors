package fr.batimen.web.client.behaviour.border;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.BorderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Ajoute une étoile rouge a coté d'un champ pour signifier à l'utilisateur que
 * le champ est obligatoire.
 * 
 * @author Casaucau Cyril
 * 
 */
public class RequiredBorderBehaviour extends BorderBehavior {

	private static final long serialVersionUID = 4898306960741712290L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.markup.html.border.BorderBehavior#afterRender(org.apache
	 * .wicket.Component)
	 */
	@Override
	public void afterRender(Component component) {
		FormComponent<?> fc = (FormComponent<?>) component;
		if (fc.isRequired()) {
			super.afterRender(component);
		}
	}
}
