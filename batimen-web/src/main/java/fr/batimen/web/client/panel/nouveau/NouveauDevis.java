package fr.batimen.web.client.panel.nouveau;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.master.MasterPage;

/**
 * Permet la création de nouveau devis par un client demandeur de travaux.
 * 
 * @author Casaucau Cyril
 * 
 */
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true);

		MapFrance carteFrance = new MapFrance("mapFrance");
		this.add(carteFrance);

		final PageParameters parameters = new PageParameters();
		parameters.add("departement", "06");

		Link<String> testLinkParam = new Link<String>("testURL") {

			private static final long serialVersionUID = -2614506823488833445L;

			@Override
			public void onClick() {
				this.setResponsePage(NouveauDevis.class, parameters);

			}

		};

		this.add(testLinkParam);
	}

	public NouveauDevis(PageParameters parameters) {
		this();

		// On récupere le departement qu'a choisi l'utilisateur
		String departement = parameters.get("departement").toString();

	}
}
