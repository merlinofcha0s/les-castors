package fr.batimen.web.client.panel.nouveau;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.core.metier.CorpsMetier;
import fr.batimen.core.metier.SousCorpsMetier;
import fr.batimen.web.app.BatimenApplication;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.master.MasterPage;

/**
 * Permet la création de nouveau devis par un client.
 * 
 * @author Casaucau Cyril
 * 
 */
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	private WebMarkupContainer containerQualification;
	private WebMarkupContainer containerInscription;
	private WebMarkupContainer progressBar;
	private MapFrance carteFrance;
	private Label etape;

	// Composant étape 2
	private DropDownChoice<CorpsMetier> corpsMetierSelect;
	private DropDownChoice<SousCorpsMetier> sousCorpsMetierSelect;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true);

		// Etape 1 : selection du departement avec la carte de la france
		carteFrance = new MapFrance("mapFrance");

		// Affiche l'étape 2
		containerQualification = new WebMarkupContainer("containerQualification");
		containerQualification.setVisible(false);

		corpsMetierSelect = new DropDownChoice<CorpsMetier>("corpsMetierSelect", new Model<CorpsMetier>(),
				BatimenApplication.getCorpsMetier());
		corpsMetierSelect.setRequired(true);
		containerQualification.add(corpsMetierSelect);

		sousCorpsMetierSelect = new DropDownChoice<SousCorpsMetier>("sousCorpsMetierSelect");
		corpsMetierSelect.setRequired(true);
		containerQualification.add(sousCorpsMetierSelect);

		// Affiche l'étape 3
		containerInscription = new WebMarkupContainer("containerInscription");
		containerInscription.setVisible(false);

		progressBar = new WebMarkupContainer("progressBar");

		etape = new Label("etape", new Model<String>());

		this.add(carteFrance);
		this.add(containerQualification);
		this.add(containerInscription);
		this.add(progressBar);
		this.add(etape);

		changementEtape("10", "Etape 1/3");
	}

	public NouveauDevis(PageParameters parameters) {
		this();
		// On récupere le departement qu'a choisi l'utilisateur
		String departement = parameters.get("departement").toString();
		if (departement != null) {
			etape2Qualification(departement);
		}

	}

	private void etape2Qualification(String departement) {
		carteFrance.setVisible(false);
		containerQualification.setVisible(true);
		changementEtape("33", "Etape 2/3");

	}

	private void etape3Inscription() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(false);
		changementEtape("66", "Etape 3/3");
	}

	private void changementEtape(String percent, String etapeSur3) {
		progressBar.add(new AttributeModifier("data-percent", percent));
		etape.setDefaultModelObject(etapeSur3);
	}
}
