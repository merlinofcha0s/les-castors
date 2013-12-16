package fr.batimen.web.client.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.web.client.master.SecuredPage;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */
public final class MonCompte extends SecuredPage {

	private static final long serialVersionUID = 1902734649854998120L;

	public MonCompte() {
		super("Page accueil de batimen", "lol", "Bienvenue sur batimen.fr");
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
	}

	public MonCompte(final PageParameters parameters) {
		this();
	}
}