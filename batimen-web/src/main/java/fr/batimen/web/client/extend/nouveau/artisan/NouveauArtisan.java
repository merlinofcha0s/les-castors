package fr.batimen.web.client.extend.nouveau.artisan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.FrontEndException;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.component.NavigationWizard;
import fr.batimen.web.client.event.CastorWizardEvent;
import fr.batimen.web.client.event.MapFranceEvent;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ArtisanServiceREST;

/**
 * Page permettant d'enregistré un nouveau partenaire (artisan)
 *
 * @author Casaucau Cyril
 */
public class NouveauArtisan extends MasterPage {

    private static final long serialVersionUID = 7796768527786855832L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NouveauArtisan.class);

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    // DTO
    private CreationPartenaireDTO nouveauPartenaire = new CreationPartenaireDTO();

    // Composant maitre
    private WebMarkupContainer masterContainer;

    // Composants Généraux
    private NavigationWizard navigationWizard;
    // Composants étape 1
    private MapFrance carteFrance;

    // Composant étape 2
    private Etape2PartenaireForm etape2PartenaireForm;
    private WebMarkupContainer containerEtape2;

    // Composant étape 3
    private Etape3Entreprise etape3Entreprise;

    // Composant étape 4
    private Etape4Confirmation etape4Confirmation;

    private Etape etapeEncours;

    public NouveauArtisan() {
        super("Inscription d'un nouveau partenaire qui effectuera les travaux chez un particulier",
                "Artisan Rénovation Inscription", "Nouveau partenaire", true, "img/bg_title1.jpg");
        initPage();
    }

    public NouveauArtisan(CreationPartenaireDTO nouveauPartenaire) {
        super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");
        this.nouveauPartenaire = nouveauPartenaire;
        initPage();
    }

    private final void initPage() {
        etapeEncours = Etape.ETAPE_1;

        masterContainer = new WebMarkupContainer("masterContainer");
        masterContainer.setMarkupId("masterContainer");
        masterContainer.setOutputMarkupId(true);

        initNavigationWizard();

        // Etape 1 : selection du departement avec la carte de la france
        carteFrance = new MapFrance("mapFrance") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_1);
            }

        };

        // Etape 2 : Informations du dirigeant
        etape2PartenaireForm = new Etape2PartenaireForm("etape2PartenaireForm",
                new CompoundPropertyModel<>(nouveauPartenaire));

        containerEtape2 = new WebMarkupContainer("containerEtape2") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_2);
            }

        };
        containerEtape2.add(etape2PartenaireForm);

        // Etape 3 : Information de l'entreprise
        etape3Entreprise = new Etape3Entreprise("etape3InformationsEntreprise", new Model<>(),
                nouveauPartenaire, false) {

            private static final long serialVersionUID = 1L;

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

        // Etape 4 : confirmation inscription
        etape4Confirmation = new Etape4Confirmation("etape4Confirmation") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return etapeEncours.equals(Etape.ETAPE_4);
            }

        };

        ContactezNous contactezNous = new ContactezNous("contactezNous");

        masterContainer.add(carteFrance, containerEtape2, etape3Entreprise, etape4Confirmation, contactezNous);

        this.add(masterContainer);

        try {
            changementEtape(nouveauPartenaire.getNumeroEtape());
            navigationWizard.setStep(nouveauPartenaire.getNumeroEtape());
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur lors du changment d'étape pendant la creation d'un nouveau partenaire", e);
            }
        }
    }

    private void initNavigationWizard() {
        List<String> etapes = new ArrayList<String>();
        etapes.add("Sélectionner un departement");
        etapes.add("Informations du dirigeant");
        etapes.add("Informations de l'entreprise");
        etapes.add("Confirmation");

        try {
            navigationWizard = new NavigationWizard("navigationWizard", etapes);
        } catch (FrontEndException fee) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Problème avec le chargement du wizard", fee);
            }
        }
        masterContainer.add(navigationWizard);
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
                etapeEncours = Etape.ETAPE_4;
                // Appel du service d'enregistrement du nouveau partenaire
                Integer retourService = creationPartenaire(nouveauPartenaire);
                if (retourService == 0) {
                    etape4Confirmation.succesInscription();
                } else {
                    etape4Confirmation.failureInscription();
                }
                break;
            default:
                throw new FrontEndException("Aucune étape du nouveau devis chargées, Situation Impossible");
        }
    }

    private void loggerChangementEtape(String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }

    private Integer creationPartenaire(CreationPartenaireDTO nouveauPartenaire) {
        return artisanServiceREST.creationNouveauPartenaire(nouveauPartenaire);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        if (event.getPayload() instanceof MapFranceEvent) {
            MapFranceEvent eventMapFrance = (MapFranceEvent) event.getPayload();

            // On récupére le departement qui se trouve dans l'event
            Integer departementInt = Integer.valueOf(eventMapFrance.getDepartement());

            // On le valide
            if (departementInt != null && departementInt > 0 && departementInt < 100) {
                nouveauPartenaire.getAdresse().setDepartement(departementInt);
                nouveauPartenaire.setNumeroEtape(2);
            } else {
                feedBackPanelGeneral.error("Numéro de département incorrecte, veuillez recommencer");
            }

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauArtisan(nouveauPartenaire));
        }

        // Catch de l'event de changement d'etape
        if (event.getPayload() instanceof ChangementEtapeEventArtisan) {
            ChangementEtapeEventArtisan changementEtapeEvent = (ChangementEtapeEventArtisan) event.getPayload();

            // On extrait le numero de l'etape
            this.nouveauPartenaire = changementEtapeEvent.getNouveauPartenaire();

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauArtisan(nouveauPartenaire));
        }

        if (event.getPayload() instanceof CastorWizardEvent) {
            CastorWizardEvent castorWizardEvent = (CastorWizardEvent) event.getPayload();
            nouveauPartenaire.setNumeroEtape(Integer.valueOf(castorWizardEvent.getStepNumber()));

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            setResponsePage(new NouveauArtisan(nouveauPartenaire));
        }
    }
}
