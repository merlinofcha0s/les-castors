package fr.batimen.web.client.extend.nouveau.devis;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.exception.FrontEndException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LocalisationDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.NavigationWizard;
import fr.batimen.web.client.event.CastorWizardEvent;
import fr.batimen.web.client.event.Event;
import fr.batimen.web.client.event.LoginEvent;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.extend.nouveau.communs.Etape1;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.LocalisationEvent;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet la création de nouveau devis par un client.
 *
 * @author Casaucau Cyril
 */
public class NouveauDevis extends MasterPage {

    private static final long serialVersionUID = -7595966450246951918L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NouveauDevis.class);

    @Inject
    private AnnonceServiceREST annonceService;

    @Inject
    private Authentication authentication;

    // Composants Généraux
    private NavigationWizard navigationWizard;

    // Objet à remplir
    private CreationAnnonceDTO nouvelleAnnonce;
    private CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce;

    private WebMarkupContainer containerGeneral;

    // Composants étape 1
    //private MapFrance carteFrance;
    private Etape1 etape1;

    // Composants étape 2
    private Etape2Categorie etape2Categorie;

    // Composants étape 3
    private Etape3AnnonceForm etape3AnnonceForm;
    private WebMarkupContainer containerQualification;

    // Composant étape 4
    private Etape4InscriptionForm etape4InscriptionForm;
    private WebMarkupContainer containerInscription;

    // Composant étape 5
    private WebMarkupContainer containerConfirmation;
    private Label confirmation1;
    private Label confirmation2;
    private Image imageConfirmation;
    private Link<String> contactezNous;

    private Etape etapeEncours;

    public NouveauDevis(Etape etapeEncours) {
        this();
        this.etapeEncours = etapeEncours;
    }

    public NouveauDevis() {
        super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");
        initPage();
    }

    public NouveauDevis(CreationAnnonceDTO creationAnnonce) {
        super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");
        this.nouvelleAnnonce = creationAnnonce;
        propertyModelNouvelleAnnonce = new CompoundPropertyModel<>(nouvelleAnnonce);
        initPage();
    }

    public void initPage() {
        if (nouvelleAnnonce == null) {
            nouvelleAnnonce = new CreationAnnonceDTO();
        }

        if (propertyModelNouvelleAnnonce == null) {
            propertyModelNouvelleAnnonce = new CompoundPropertyModel<>(nouvelleAnnonce);
        }

        containerGeneral = new WebMarkupContainer("containerGeneral");
        containerGeneral.setOutputMarkupId(true);

        etape1 = new Etape1("etape1") {
            @Override
            public boolean isVisible() {
                if (etapeEncours.equals(Etape.ETAPE_1)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        // Etape 2 : Selection de la catégorie principale des travaux
        etape2Categorie = new Etape2Categorie("etape2Categorie") {

            private static final long serialVersionUID = -3369100260215727230L;

            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_2);
            }

        };

        // Etape 3 : Qualification du devis.
        containerQualification = new WebMarkupContainer("containerQualification") {

            private static final long serialVersionUID = -656007360631664917L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_3);
            }
        };

        etape3AnnonceForm = new Etape3AnnonceForm("formQualification", propertyModelNouvelleAnnonce);

        containerQualification.add(etape3AnnonceForm);

        // Etape 4 : Enregistrement des informations du client
        containerInscription = new WebMarkupContainer("containerInscription") {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_4);
            }
        };

        etape4InscriptionForm = new Etape4InscriptionForm("formInscription", propertyModelNouvelleAnnonce,
                Boolean.FALSE);

        AjaxLink<String> connexionLink = new AjaxLink<String>("connexion") {

            private static final long serialVersionUID = -4897659500119552151L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                getLoginDialog().open(target);
            }

        };

        containerInscription.add(etape4InscriptionForm, connexionLink);

        // Etape 5 : Confirmation
        containerConfirmation = new WebMarkupContainer("containerConfirmation") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_5);

            }
        };
        confirmation1 = new Label("confirmation1", new Model<String>());
        confirmation2 = new Label("confirmation2", new Model<String>());
        contactezNous = new Link<String>("contactezNous") {

            static final long serialVersionUID = 4407712689323543731L;

            @Override
            public void onClick() {
                this.setResponsePage(Contact.class);
            }
        };
        contactezNous.setVisible(false);

        Link<String> retourAccueil = new Link<String>("retourAccueil") {

            private static final long serialVersionUID = 8929146182522407915L;

            @Override
            public void onClick() {
                setResponsePage(Accueil.class);
            }

        };

        retourAccueil.setOutputMarkupId(true);
        retourAccueil.setMarkupId("retourAccueil");

        containerConfirmation.add(confirmation1, confirmation2, contactezNous, retourAccueil);

        List<String> etapes = new ArrayList<String>();
        etapes.add("Renseigner le code postal");
        etapes.add("Indiquer les catégories");
        etapes.add("Renseigner les caracteristiques de l'annonce");
        etapes.add("Inscription / Connexion");
        etapes.add("Confirmation");

        try {
            navigationWizard = new NavigationWizard("navigationWizard", etapes);
        } catch (FrontEndException fee) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Problème avec le chargement du wizard", fee);
            }
        }

        ContactezNous contactezNousComposant = new ContactezNous("contactezNous");

        containerGeneral.add(etape1, etape2Categorie, containerQualification, containerInscription,
                containerConfirmation, navigationWizard);

        add(containerGeneral, contactezNousComposant);

        try {
            changementEtape(nouvelleAnnonce.getNumeroEtape());
            navigationWizard.setStep(nouvelleAnnonce.getNumeroEtape());
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme frontend", e);
            }
        }
        chooseConfirmationMessage(false, CodeRetourService.RETOUR_OK);
    }

    private void chooseConfirmationMessage(boolean isEtape4, Integer codeRetour) {

        // On choisi le message d'erreur que l'on va afficher suivant different
        // cas :
        // Premier cas : il est connecté et l'enregistrement s'est bien passé
        // Deuxieme cas : il n'est pas connecté et l'enregistrement s'est bien
        // passé
        // Troisieme cas : l'enregistrement ne s'est pas bien passé.
        // Dernier cas : cas par defaut, on est pas dans l'etape 4 (mais il faut
        // quand meme instantié l'objet)
        if (nouvelleAnnonce.getIsSignedUp() != null && nouvelleAnnonce.getIsSignedUp() && isEtape4
                && codeRetour.equals(CodeRetourService.RETOUR_OK)) {
            confirmation1
                    .setDefaultModelObject("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif");
            confirmation2.setDefaultModelObject("");
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
            imageConfirmation.add(new AttributeModifier("alt", "ok"));
        } else if (nouvelleAnnonce.getIsSignedUp() != null && !nouvelleAnnonce.getIsSignedUp() && isEtape4
                && codeRetour.equals(CodeRetourService.RETOUR_OK)) {
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
            imageConfirmation.add(new AttributeModifier("alt", "ok"));
            confirmation1
                    .setDefaultModelObject("Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer");
            confirmation2
                    .setDefaultModelObject("Votre devis a bien été enregistré. Celui-ci sera mis en ligne une fois votre compte activé.");
        } else if (codeRetour.equals(CodeRetourService.RETOUR_KO)
                || codeRetour.equals(CodeRetourService.ANNONCE_RETOUR_DUPLICATE)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur pendant le chargement de l'annonce");
                LOGGER.error("Détails: {}", nouvelleAnnonce.toString());
            }
            confirmation1
                    .setDefaultModelObject("Problème pendant l'enregistrement de l'annonce, veuillez nous excuser pour la gène occasionnée.");
            confirmation2.setDefaultModelObject("Si le problème persiste ");
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/error.png"));
            imageConfirmation.add(new AttributeModifier("alt", "error"));
            contactezNous.setVisible(true);
        } else {
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
            imageConfirmation.add(new AttributeModifier("alt", "ok"));
        }

        // Dans un objet image de wicket, nous sommes obligé de l'instantier
        // avec
        // une image car ensuite si on la change, au rechargement de la page vu
        // que l'instantiation
        // passera toujours avant, l'image alternative ne s'affichera jamais
        containerConfirmation.addOrReplace(imageConfirmation);
    }

    private void changementEtape(Integer numeroEtape) throws FrontEndException {
        // On charge l'etape demandé grace au numero d'etape passé en parametre
        switch (numeroEtape) {
            case 1:
                loggerChangementEtape("Passage dans l'étape 1");
                etapeEncours = Etape.ETAPE_1;
                break;
            case 2:
                loggerChangementEtape("Passage dans l'étape 2");
                etapeEncours = Etape.ETAPE_2;
                break;
            case 3:
                loggerChangementEtape("Passage dans l'étape 3");
                etapeEncours = Etape.ETAPE_3;
                //etape3AnnonceForm.setSousCategorieChoices(nouvelleAnnonce.getCategorieMetier().getSousCategories());
                break;
            case 4:
                loggerChangementEtape("Passage dans l'étape 4");
                // Si l'utilisateur est deja authentifié on saute l'inscription
                // (etape 3)
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    loggerChangementEtape("L'utilisateur s'est connecté, on passe l'etape 5");
                    changementEtape(5);
                } else {
                    etapeEncours = Etape.ETAPE_4;
                }
                break;
            case 5:
                loggerChangementEtape("Passage dans l'étape 5");
                // Si il est authentifié on l'enregistre dans la DTO (pour le
                // backend) et on charge le bon message de confirmation
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    remplissageCreationAnnonceSiLogin();
                }
                Integer codeRetour = creationAnnonce();
                chooseConfirmationMessage(true, codeRetour);
                etapeEncours = Etape.ETAPE_5;
                break;
            default:
                throw new FrontEndException("Aucune étape du nouveau devis chargées, Situation Impossible");
        }
    }

    private void remplissageCreationAnnonceSiLogin() {
        ClientDTO client = authentication.getCurrentUserInfo();
        nouvelleAnnonce.setIsSignedUp(true);
        nouvelleAnnonce.getClient().setLogin(client.getLogin());

    }

    private void loggerChangementEtape(String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        // Event déclenché par la popup de connexion, fait sauter l'etape 3 si
        // l'utilisateur se connecte
        if (event.getPayload() instanceof LoginEvent) {
            Event update = (Event) event.getPayload();
            if (nouvelleAnnonce.getNumeroEtape() == 4) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("On est a l'étape 3 : Evenement recu de la popup de connexion, on passe à l'etape 5");
                }
                try {
                    changementEtape(5);
                } catch (FrontEndException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Probleme frontend", e);
                    }
                }

                navigationWizard.setStep(etapeEncours.ordinal() + 1);
                update.getTarget().add(containerGeneral);
            }
        }

        if (event.getPayload() instanceof CategorieEvent) {
            CategorieEvent eventCategorie = (CategorieEvent) event.getPayload();
            // On recupere la catégorie métier
            nouvelleAnnonce.setMotCles(eventCategorie.getCategoriesChoisies());
            // On set la prochaine etape
            nouvelleAnnonce.setNumeroEtape(3);

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }
            setResponsePage(new NouveauDevis(nouvelleAnnonce));
        }

        if (event.getPayload() instanceof ChangementEtapeClientEvent) {
            ChangementEtapeClientEvent eventChangementEtapeClient = (ChangementEtapeClientEvent) event.getPayload();
            // On passe à l'etape suivante
            this.nouvelleAnnonce = eventChangementEtapeClient.getNouvelleAnnonce();

            if (nouvelleAnnonce.getNumeroEtape().equals(Integer.valueOf(5))) {
                String password = nouvelleAnnonce.getClient().getPassword();
                nouvelleAnnonce.getClient().setPassword(HashHelper.hashScrypt(password));
            }

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauDevis(nouvelleAnnonce));
        }

        if (event.getPayload() instanceof CastorWizardEvent) {
            CastorWizardEvent castorWizardEvent = (CastorWizardEvent) event.getPayload();
            nouvelleAnnonce.setNumeroEtape(Integer.valueOf(castorWizardEvent.getStepNumber()));

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauDevis(nouvelleAnnonce));
        }

        if (event.getPayload() instanceof LocalisationEvent) {
            LocalisationEvent localisationEvent = (LocalisationEvent) event.getPayload();
            if (!localisationEvent.getLocalisationDTOMemeCodePostal().isEmpty()) {
                nouvelleAnnonce.setCodePostal(localisationEvent.getLocalisationDTOMemeCodePostal().get(0).getCodePostal());
                nouvelleAnnonce.setDepartement(Integer.valueOf(localisationEvent.getLocalisationDTOMemeCodePostal().get(0).getDepartement()));
            }

            for (LocalisationDTO localisationDTO : localisationEvent.getLocalisationDTOMemeCodePostal()) {
                nouvelleAnnonce.getVillesPossbles().clear();
                nouvelleAnnonce.getVillesPossbles().add(localisationDTO.getCommune());
            }

            nouvelleAnnonce.setNumeroEtape(2);

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauDevis(nouvelleAnnonce));
        }
    }

    private Integer creationAnnonce() {
        if (nouvelleAnnonce.getPhotos() == null) {
            nouvelleAnnonce.setPhotos(new ArrayList<File>());
        }
        if (nouvelleAnnonce.getPhotos().isEmpty()) {
            return annonceService.creationAnnonce(nouvelleAnnonce);
        } else {
            return annonceService.creationAnnonceAvecImage(nouvelleAnnonce);
        }
    }
}