package fr.batimen.web.client.extend.nouveau.devis;

import java.util.ArrayList;
import java.util.List;

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

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.FrontEndException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.component.NavigationWizard;
import fr.batimen.web.client.event.Event;
import fr.batimen.web.client.event.LoginEvent;
import fr.batimen.web.client.event.MapFranceEvent;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;
import fr.batimen.web.client.master.MasterPage;
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
    private NavigationWizard navigationWizard;

    // Objet à remplir
    private CreationAnnonceDTO nouvelleAnnonce = new CreationAnnonceDTO();
    private final CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce = new CompoundPropertyModel<CreationAnnonceDTO>(
            nouvelleAnnonce);

    // Composants étape 1
    private final MapFrance carteFrance;

    // Composants étape 2
    private final Etape2Categorie etape2Categorie;

    // Composants étape 3
    private final Etape3AnnonceForm etape3AnnonceForm;
    private final WebMarkupContainer containerQualification;

    // Composant étape 4
    private final Etape4InscriptionForm etape4InscriptionForm;
    private final WebMarkupContainer containerInscription;

    // Composant étape 5
    private final WebMarkupContainer containerConfirmation;
    private final Label confirmation1;
    private final Label confirmation2;
    private Image imageConfirmation;
    private final Link<String> contactezNous;

    private Etape etapeEncours;

    public NouveauDevis() {
        super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");

        // Etape 1 : selection du departement avec la carte de la france
        carteFrance = new MapFrance("mapFrance") {

            private static final long serialVersionUID = -7657021021902246878L;

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
                if (etapeEncours.equals(Etape.ETAPE_2)) {
                    return true;
                } else {
                    return false;
                }
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
                if (etapeEncours.equals(Etape.ETAPE_3)) {
                    return true;
                } else {
                    return false;
                }
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
                if (etapeEncours.equals(Etape.ETAPE_4)) {
                    return true;
                } else {
                    return false;
                }
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
                if (etapeEncours.equals(Etape.ETAPE_5)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        // containerConfirmation.setVisible(false);
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
        etapes.add("Sélectionner un departement");
        etapes.add("Sélectionner la catégorie");
        etapes.add("Renseigner les caracteristiques du Devis");
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

        this.add(carteFrance, etape2Categorie, containerQualification, containerInscription, containerConfirmation,
                navigationWizard, contactezNousComposant);

        try {
            changementEtape(nouvelleAnnonce.getNumeroEtape());
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme frontend", e);
            }
        }
        chooseConfirmationMessage(false, Constant.CODE_SERVICE_RETOUR_OK);
    }

    public NouveauDevis(CreationAnnonceDTO creationAnnonce) {
        this();
        this.nouvelleAnnonce = creationAnnonce;
        try {
            changementEtape(creationAnnonce.getNumeroEtape());
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme frontend", e);
            }
        }
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
                && codeRetour.equals(Constant.CODE_SERVICE_RETOUR_OK)) {
            confirmation1
                    .setDefaultModelObject("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif");
            confirmation2.setDefaultModelObject("");
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
            imageConfirmation.add(new AttributeModifier("alt", "ok"));
        } else if (nouvelleAnnonce.getIsSignedUp() != null && !nouvelleAnnonce.getIsSignedUp() && isEtape4
                && codeRetour.equals(Constant.CODE_SERVICE_RETOUR_OK)) {
            imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
            imageConfirmation.add(new AttributeModifier("alt", "ok"));
            confirmation1
                    .setDefaultModelObject("Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer");
            confirmation2
                    .setDefaultModelObject("Votre devis a bien été enregistré. Celui-ci sera mis en ligne une fois votre compte activé.");
        } else if (codeRetour.equals(Constant.CODE_SERVICE_RETOUR_KO)
                || codeRetour.equals(Constant.CODE_SERVICE_ANNONCE_RETOUR_DUPLICATE)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur pendant le chargement de l'annonce");
                loggerAnnonce(nouvelleAnnonce);
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
        Authentication authentication = new Authentication();
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
                update.getTarget().add(this);
            }
        }

        // Event déclenché par la carte de france, cela permet de recuperer le
        // département et de passer à l'etape 2
        if (event.getPayload() instanceof MapFranceEvent) {
            MapFranceEvent eventMapFrance = (MapFranceEvent) event.getPayload();

            // On récupére le departement qui se trouve dans l'event
            Integer departementInt = Integer.valueOf(eventMapFrance.getDepartement());

            if (departementInt != null && departementInt > 0 && departementInt < 100) {

                // On l'enregistre dans la DTO
                nouvelleAnnonce.setDepartement(departementInt);
                // On prepare le passage à l'etape suivante
                nouvelleAnnonce.setNumeroEtape(2);
                try {
                    changementEtape(2);
                } catch (FrontEndException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Probleme frontend avec l'etape 2", e);
                    }
                }
                // Cas ou il y aurait un snake qui essaye de modifier la requete
                // ajax pour injecter des données erronées
            } else {
                feedBackPanelGeneral.error("Numéro de département incorrecte, veuillez recommencer");
            }
            eventMapFrance.getTarget().add(this);
        }

        if (event.getPayload() instanceof CategorieEvent) {
            CategorieEvent eventCategorie = (CategorieEvent) event.getPayload();
            // On recupere la catégorie métier
            nouvelleAnnonce.setCategorieMetier(eventCategorie.getCategorieChoisie());
            // On set la prochaine etape
            nouvelleAnnonce.setNumeroEtape(3);
            // On passe à l'etape suivante
            try {
                changementEtape(3);
            } catch (FrontEndException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Probleme frontend avec l'etape 3", e);
                }
            }
            // On dit a wicket de rafraichir ce panel avec la requete ajax
            eventCategorie.getTarget().add(this);
        }

        if (event.getPayload() instanceof ChangementEtapeClientEvent) {
            ChangementEtapeClientEvent eventChangementEtapeClient = (ChangementEtapeClientEvent) event.getPayload();
            // On passe à l'etape suivante
            this.nouvelleAnnonce = eventChangementEtapeClient.getNouvelleAnnonce();

            if (nouvelleAnnonce.getNumeroEtape().equals(Integer.valueOf(5))) {
                String password = nouvelleAnnonce.getClient().getPassword();
                nouvelleAnnonce.getClient().setPassword(HashHelper.hashScrypt(password));
            }

            try {
                changementEtape(nouvelleAnnonce.getNumeroEtape());
            } catch (FrontEndException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Probleme frontend avec l'etape " + nouvelleAnnonce.getNumeroEtape(), e);
                }
            }

            // On dit a wicket de rafraichir ce panel avec la requete ajax
            eventChangementEtapeClient.getTarget().add(this);
        }

    }

    private Integer creationAnnonce() {
        return AnnonceService.creationAnnonce(nouvelleAnnonce);
    }

    private void loggerAnnonce(CreationAnnonceDTO nouvelleAnnonce) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("+--------------------------------Annonce ------------------------------------------+");
            LOGGER.error("Categorie : " + nouvelleAnnonce.getCategorieMetier());
            LOGGER.error("Sous categorie : " + nouvelleAnnonce.getSousCategorie());
            LOGGER.error("Description: " + nouvelleAnnonce.getDescription());
            LOGGER.error("Contact ? : " + nouvelleAnnonce.getTypeContact().getAffichage());
            LOGGER.error("Intervention  : " + nouvelleAnnonce.getDelaiIntervention().getType());
            LOGGER.error("Adresse du chantier  : " + nouvelleAnnonce.getAdresse());
            LOGGER.error("Complément du chantier  : " + nouvelleAnnonce.getComplementAdresse());
            LOGGER.error("Code postal  : " + nouvelleAnnonce.getCodePostal());
            LOGGER.error("Ville  : " + nouvelleAnnonce.getVille());
            LOGGER.error("+--------------------------------Info Demandeur ------------------------------------------+");
            if (!nouvelleAnnonce.getIsSignedUp()) {
                LOGGER.error("Nom  : " + nouvelleAnnonce.getClient().getNom());
                LOGGER.error("Prénom  : " + nouvelleAnnonce.getClient().getPrenom());
                LOGGER.error("Numéro de téléphone  : " + nouvelleAnnonce.getClient().getNumeroTel());
                LOGGER.error("Adresse mail : " + nouvelleAnnonce.getClient().getEmail());
                LOGGER.error("Identifiant: " + nouvelleAnnonce.getClient().getLogin());
            } else {
                Authentication authentication = new Authentication();
                ClientDTO client = authentication.getCurrentUserInfo();
                LOGGER.error("Ce client est deja enregistrer dans la BDD");
                LOGGER.error("Nom  : " + client.getNom());
                LOGGER.error("Prénom  : " + client.getPrenom());
                LOGGER.error("Numéro de téléphone  : " + client.getNumeroTel());
                LOGGER.error("Adresse mail : " + client.getEmail());
                LOGGER.error("Identifiant: " + client.getLogin());
            }
            LOGGER.error("+-------------------------------- Fin annonce ------------------------------------------+");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Page#onInitialize()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        navigationWizard.setStep(etapeEncours.ordinal() + 1);
    }

}
