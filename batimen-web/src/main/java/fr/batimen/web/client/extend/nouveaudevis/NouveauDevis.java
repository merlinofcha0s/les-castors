package fr.batimen.web.client.extend.nouveaudevis;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.FrontEndException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.event.Event;
import fr.batimen.web.client.event.LoginEvent;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.session.BatimenSession;
import fr.batimen.ws.client.service.AnnonceService;

/**
 * Permet la création de nouveau devis par un client.
 * 
 * @author Casaucau Cyril
 * 
 */
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NouveauDevis.class);

	// Composants Généraux
	private WebMarkupContainer progressBar;
	private Label etape;

	// Objet à remplir
	private CreationAnnonceDTO creationAnnonce = new CreationAnnonceDTO();
	private CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce = new CompoundPropertyModel<CreationAnnonceDTO>(
	        creationAnnonce);

	// Composants étape 1
	private MapFrance carteFrance;
	private String departement;

	// Composants étape 2
	private WebMarkupContainer containerQualification;

	// Composant étape 3
	private final Etape3InscriptionForm etape3InscriptionForm;
	private WebMarkupContainer containerInscription;

	// Composant étape 4
	private WebMarkupContainer containerConfirmation;
	private Label confirmation1;
	private Label confirmation2;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");
		super.setActiveMenu(MasterPage.NOUVEAU_DEVIS);

		// Etape 1 : selection du departement avec la carte de la france
		carteFrance = new MapFrance("mapFrance");

		// Etape 2 : Qualification du devis.
		containerQualification = new WebMarkupContainer("containerQualification");
		containerQualification.setVisible(false);

		Etape2AnnonceForm etape2AnnonceForm = new Etape2AnnonceForm("formQualification", propertyModelNouvelleAnnonce) {

			private static final long serialVersionUID = -6436387191126517996L;

			@Override
			protected void onSubmit() {
				creationAnnonce.setDepartement(Integer.valueOf(departement));
				creationAnnonce.setNumeroEtape(3);
				this.setResponsePage(new NouveauDevis(creationAnnonce));
			}
		};

		containerQualification.add(etape2AnnonceForm);

		// Etape 3 : Enregistrement des informations du client
		containerInscription = new WebMarkupContainer("containerInscription");
		containerInscription.setVisible(false);

		etape3InscriptionForm = new Etape3InscriptionForm("formInscription", propertyModelNouvelleAnnonce) {

			private static final long serialVersionUID = -7785574548677996934L;

			@Override
			protected void onSubmit() {
				// On set les champs manuellement contrairement a l'étape 2 car
				// le compound property model ne marche pas pour une raison
				// inconnu.
				creationAnnonce.setCivilite(etape3InscriptionForm.getCiviliteField().getConvertedInput());
				creationAnnonce.setNom(etape3InscriptionForm.getNomField().getConvertedInput());
				creationAnnonce.setPrenom(etape3InscriptionForm.getPrenomField().getConvertedInput());
				creationAnnonce.setNumeroTel(etape3InscriptionForm.getNumeroTelField().getConvertedInput());
				creationAnnonce.setEmail(etape3InscriptionForm.getEmailField().getConvertedInput());
				creationAnnonce.setLogin(etape3InscriptionForm.getLoginField().getConvertedInput());
				creationAnnonce.setPassword(HashHelper.hashString(etape3InscriptionForm.getPasswordField()
				        .getConvertedInput()));
				creationAnnonce.setNumeroEtape(4);
				this.setResponsePage(new NouveauDevis(creationAnnonce));
			}

		};

		AjaxLink<String> connexionLink = new AjaxLink<String>("connexion") {

			private static final long serialVersionUID = -4897659500119552151L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getLoginDialog().open(target);
			}

		};

		containerInscription.add(etape3InscriptionForm);
		containerInscription.add(connexionLink);

		// Etape 4 : Confirmation
		containerConfirmation = new WebMarkupContainer("containerConfirmation");
		containerConfirmation.setVisible(false);
		confirmation1 = new Label("confirmation1", new Model<String>());
		confirmation2 = new Label("confirmation2", new Model<String>());

		Link<String> retourAccueil = new Link<String>("retourAccueil") {

			private static final long serialVersionUID = 8929146182522407915L;

			@Override
			public void onClick() {
				setResponsePage(Accueil.class);
			}

		};

		containerConfirmation.add(confirmation1);
		containerConfirmation.add(confirmation2);
		containerConfirmation.add(retourAccueil);

		// Composant généraux
		progressBar = new WebMarkupContainer("progressBar");
		etape = new Label("etape", new Model<String>());

		this.add(carteFrance);
		this.add(containerQualification);
		this.add(containerInscription);
		this.add(containerConfirmation);
		this.add(progressBar);
		this.add(etape);

		try {
			changementEtape(creationAnnonce.getNumeroEtape());
		} catch (FrontEndException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Probleme frontend", e);
			}
		}
		chooseConfirmationMessage(false, Constant.CODE_SERVICE_RETOUR_OK);
	}

	public NouveauDevis(PageParameters parameters) {
		this();
		// On passe dans ce constructeur au debut de l'étape 2
		// On récupere le departement qu'a choisi l'utilisateur
		departement = parameters.get("departement").toString();
		if (departement != null) {
			try {
				changementEtape(2);
			} catch (FrontEndException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Probleme frontend", e);
				}
			}
		}
	}

	public NouveauDevis(CreationAnnonceDTO creationAnnonce) {
		this();
		this.creationAnnonce = creationAnnonce;
		try {
			changementEtape(creationAnnonce.getNumeroEtape());
		} catch (FrontEndException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Probleme frontend", e);
			}
		}
	}

	private void chooseConfirmationMessage(boolean isEtape4, Integer codeRetour) {
		if (creationAnnonce.getIsSignedUp() != null && creationAnnonce.getIsSignedUp() && isEtape4
		        && codeRetour == Constant.CODE_SERVICE_RETOUR_OK) {
			confirmation1
			        .setDefaultModelObject("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif");
			confirmation2.setDefaultModelObject("");
		} else if (creationAnnonce.getIsSignedUp() != null && !creationAnnonce.getIsSignedUp() && isEtape4
		        && codeRetour == Constant.CODE_SERVICE_RETOUR_OK) {
			confirmation1
			        .setDefaultModelObject("Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer");
			confirmation2
			        .setDefaultModelObject("Votre devis a bien été enregistré. Celui-ci sera mis en ligne une fois votre compte activé.");
		} else if (codeRetour == Constant.CODE_SERVICE_RETOUR_KO
		        || codeRetour == Constant.CODE_SERVICE_RETOUR_DUPLICATE) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erreur pendant le chargement de l'annonce");
				loggerAnnonce(creationAnnonce);
			}
			confirmation1
			        .setDefaultModelObject("Problème pendant l'enregistrement de l'annonce, veuillez nous excuser pour la gène occasionnée.");
			confirmation2.setDefaultModelObject("Si le problème persiste contactez nous.");
		}
	}

	private void etape2Qualification() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(true);
		containerInscription.setVisible(false);
		containerConfirmation.setVisible(false);
	}

	private void etape3Inscription() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(false);
		containerInscription.setVisible(true);
		containerConfirmation.setVisible(false);

	}

	private void etape4Confirmation() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(false);
		containerInscription.setVisible(false);
		containerConfirmation.setVisible(true);
	}

	private void changementEtape(Integer numeroEtape) throws FrontEndException {
		String percent = "";

		// On charge l'etape demandé grace au numero d'etape passé en parametre
		switch (numeroEtape) {
		case 1:
			loggerChangementEtape("Passage dans l'étape 1");
			percent = "25";
			break;
		case 2:
			loggerChangementEtape("Passage dans l'étape 2");
			percent = "50";
			etape2Qualification();
			break;
		case 3:
			loggerChangementEtape("Passage dans l'étape 3");
			percent = "75";
			// Si l'utilisateur est deja authentifié on saute l'inscription
			// (etape 3)
			if (BatimenSession.get().isSignedIn()) {
				loggerChangementEtape("L'utilisateur s'est connecté, on passe l'etape 3");
				changementEtape(4);
			} else {
				etape3Inscription();
			}
			break;
		case 4:
			loggerChangementEtape("Passage dans l'étape 4");
			percent = "100";
			// Si il est authentifié on l'enregistre dans la DTO (pour le
			// backend) et on charge le bon message de confirmation
			if (BatimenSession.get().isSignedIn()) {
				remplissageCreationAnnonceSiLogin();
			}
			Integer codeRetour = creationAnnonce();
			chooseConfirmationMessage(true, codeRetour);
			etape4Confirmation();
			break;
		default:
			throw new FrontEndException("Aucune étape du nouveau devis chargées, Situation Impossible");
		}

		// On incrémente la progress bar
		StringBuilder cssWidthProgress = new StringBuilder("width:");
		cssWidthProgress.append(percent);
		cssWidthProgress.append("%;");
		progressBar.add(new AttributeModifier("style", cssWidthProgress.toString()));

		// On construit la chaine de caract pour afficher Etape x/4
		StringBuilder numeroEtapeAffiche = new StringBuilder("Etape ");
		numeroEtapeAffiche.append(numeroEtape).append("/4");

		etape.setDefaultModelObject(numeroEtapeAffiche.toString());

	}

	private void remplissageCreationAnnonceSiLogin() {
		BatimenSession session = (BatimenSession) BatimenSession.get();
		ClientDTO client = session.getSessionUser();
		creationAnnonce.setIsSignedUp(true);
		creationAnnonce.setEmail(client.getEmail());

	}

	private void loggerChangementEtape(String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		// Event déclenché par la popup de connexion, fait sauter l'etape 3 si
		// l'utilisateur se connecte
		if (event.getPayload() instanceof LoginEvent) {
			Event update = (Event) event.getPayload();
			if (creationAnnonce.getNumeroEtape() == 3) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("On est a l'étape 3 : Evenement recu de la popup de connexion, on passe à l'etape 4");
				}
				try {
					changementEtape(4);
				} catch (FrontEndException e) {
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error("Probleme frontend", e);
					}
				}
				update.getTarget().add(this);
			}

		}
	}

	private Integer creationAnnonce() {
		Integer codeRetour = AnnonceService.creationAnnonce(creationAnnonce);
		return codeRetour;
	}

	private void loggerAnnonce(CreationAnnonceDTO nouvelleAnnonce) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("+--------------------------------Annonce ------------------------------------------+");
			LOGGER.error("Objet : " + nouvelleAnnonce.getTitre());
			LOGGER.error("Corps de metier : " + nouvelleAnnonce.getMetier());
			LOGGER.error("Description: " + nouvelleAnnonce.getDescription());
			LOGGER.error("Contact ? : " + nouvelleAnnonce.getTypeContact().getAffichage());
			LOGGER.error("Intervention  : " + nouvelleAnnonce.getDelaiIntervention().getType());
			LOGGER.error("Combien de devis  : " + nouvelleAnnonce.getNbDevis());
			LOGGER.error("Adresse du chantier  : " + nouvelleAnnonce.getAdresse());
			LOGGER.error("Complément du chantier  : " + nouvelleAnnonce.getComplementAdresse());
			LOGGER.error("Code postal  : " + nouvelleAnnonce.getCodePostal());
			LOGGER.error("Ville  : " + nouvelleAnnonce.getVille());
			LOGGER.error("+--------------------------------Info Demandeur ------------------------------------------+");
			LOGGER.error("Civilité  : " + nouvelleAnnonce.getCivilite().getAffichage());
			LOGGER.error("Nom  : " + nouvelleAnnonce.getNom());
			LOGGER.error("Prénom  : " + nouvelleAnnonce.getPrenom());
			LOGGER.error("Numéro de téléphone  : " + nouvelleAnnonce.getNumeroTel());
			LOGGER.error("Adresse mail : " + nouvelleAnnonce.getEmail());
			LOGGER.error("Identifiant: " + nouvelleAnnonce.getLogin());
			LOGGER.error("Password : " + nouvelleAnnonce.getPassword());
			LOGGER.error("+-------------------------------- Fin log annonce ------------------------------------------+");
		}

	}
}
