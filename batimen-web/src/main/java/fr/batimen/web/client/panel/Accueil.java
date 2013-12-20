package fr.batimen.web.client.panel;

import org.apache.wicket.markup.html.link.Link;

import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.panel.nouveau.NouveauDevis;

/**
 * Premiere page visible quand l'utilisateur arrivera sur le site, elle definira
 * notre offre
 * 
 * @author Casaucau Cyril
 * 
 */
public class Accueil extends MasterPage {

	private static final long serialVersionUID = -690817359101639588L;

	public Accueil() {
		super("Page d'accueil de batimen.fr", "lol", "Accueil batimen", false);

		initLink();
	}

	private void initLink() {
		Link<String> nouveauDevis = new Link<String>("nouveauDevis") {

			private static final long serialVersionUID = -6716952676398723108L;

			@Override
			public void onClick() {
				this.setResponsePage(NouveauDevis.class);
			}

		};

		this.add(nouveauDevis);
	}

}
