package fr.batimen.web.client.panel.nouveau;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.master.MasterPage;

/**
 * Permet la création de nouveau devis par un client.
 * 
 * @author Casaucau Cyril
 * 
 */
// TODO : Etape 3
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	// Composants Généraux
	private WebMarkupContainer containerInscription;
	private WebMarkupContainer progressBar;
	private Label etape;

	// Composants étape 1
	private MapFrance carteFrance;
	private String departement;

	// Objet à remplir
	private CreationAnnonceDTO creationAnnonce = new CreationAnnonceDTO();

	// Composants étape 2
	private WebMarkupContainer containerQualification;
	private Etape2AnnonceForm formEtape2;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");

		// Etape 1 : selection du departement avec la carte de la france
		carteFrance = new MapFrance("mapFrance");

		// Etape 2 : Qualification du devis.
		containerQualification = new WebMarkupContainer("containerQualification");
		containerQualification.setVisible(false);

		formEtape2 = new Etape2AnnonceForm("formQualification", new CompoundPropertyModel<CreationAnnonceDTO>(
				creationAnnonce)) {

			private static final long serialVersionUID = -6436387191126517996L;

			@Override
			protected void onSubmit() {
				creationAnnonce.setDepartement(Integer.valueOf(departement));
				creationAnnonce.setIsEtape2(false);
				this.setResponsePage(new NouveauDevis(creationAnnonce));
			}
		};

		containerQualification.add(formEtape2);

		// Etape 3 : Enregistrement des informations du client
		containerInscription = new WebMarkupContainer("containerInscription");
		containerInscription.setVisible(false);

		// Composant généraux
		progressBar = new WebMarkupContainer("progressBar");
		etape = new Label("etape", new Model<String>());

		this.add(carteFrance);
		this.add(containerQualification);
		this.add(containerInscription);
		this.add(progressBar);
		this.add(etape);

		changementEtape("25", "Etape 1/4");
	}

	public NouveauDevis(PageParameters parameters) {
		this();
		// On passe dans ce constructeur au debut de l'étape 2
		// On récupere le departement qu'a choisi l'utilisateur
		departement = parameters.get("departement").toString();
		if (departement != null) {
			etape2Qualification();
		}
	}

	public NouveauDevis(CreationAnnonceDTO creationAnnonce) {
		this();
		this.creationAnnonce = creationAnnonce;
		if (creationAnnonce.getIsEtape2()) {
			etape2Qualification();
		} else {
			etape3Inscription();
		}
	}

	private void etape2Qualification() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(true);
		changementEtape("50", "Etape 2/4");
	}

	private void etape3Inscription() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(false);
		changementEtape("75", "Etape 3/4");
		feedBackPanelGeneral.info("description : " + creationAnnonce.getDescription());
	}

	private void changementEtape(String percent, String numeroEtape) {
		progressBar.add(new AttributeModifier("data-percent", percent));
		etape.setDefaultModelObject(numeroEtape);
	}

}
