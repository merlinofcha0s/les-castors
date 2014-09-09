package fr.batimen.web.client.extend.nouveau.artisan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.FrontEndException;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.component.NavigationWizard;
import fr.batimen.web.client.event.MapFranceEvent;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page permettant d'enregistré un nouveau partenaire (artisan)
 * 
 * @author Casaucau Cyril
 * 
 */
public class NouveauArtisan extends MasterPage implements IAjaxIndicatorAware {

    private static final long serialVersionUID = 7796768527786855832L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NouveauArtisan.class);

    // DTO
    private CreationPartenaireDTO nouveauPartenaire = new CreationPartenaireDTO();

    // Composant maitre
    private final WebMarkupContainer masterContainer;

    // Composants Généraux
    private NavigationWizard navigationWizard;
    // Composants étape 1
    private final MapFrance carteFrance;

    // Composant étape 2
    private final Etape2PartenaireForm etape2PartenaireForm;
    private final WebMarkupContainer containerEtape2;

    // Composant étape 3
    private final Etape3Entreprise etape3Entreprise;

    // Composant étape 4
    private final Etape4Confirmation etape4Confirmation;

    public NouveauArtisan() {
        super("Inscription d'un nouveau partenaire qui effectuera les travaux chez un particulier",
                "Artisan Rénovation Inscription", "Nouveau partenaire", true, "img/bg_title1.jpg");
        super.setActiveMenu(MasterPage.NONE);

        masterContainer = new WebMarkupContainer("masterContainer");
        masterContainer.setMarkupId("masterContainer");
        masterContainer.setOutputMarkupId(true);

        initNavigationWizard();

        // Etape 1 : selection du departement avec la carte de la france
        carteFrance = new MapFrance("mapFrance");

        // Etape 2 : Informations du dirigeant
        etape2PartenaireForm = new Etape2PartenaireForm("etape2PartenaireForm",
                new CompoundPropertyModel<CreationPartenaireDTO>(nouveauPartenaire));
        containerEtape2 = new WebMarkupContainer("containerEtape2");
        containerEtape2.add(etape2PartenaireForm);
        containerEtape2.setVisible(false);

        // Etape 3 : Information de l'entreprise
        etape3Entreprise = new Etape3Entreprise("etape3InformationsEntreprise", new Model<Serializable>(),
                nouveauPartenaire);
        etape3Entreprise.setVisible(false);

        // Etape 4 : confirmation inscription
        etape4Confirmation = new Etape4Confirmation("etape4Confirmation");
        etape4Confirmation.setVisible(false);

        masterContainer.add(carteFrance);
        masterContainer.add(containerEtape2);
        masterContainer.add(etape3Entreprise);
        masterContainer.add(etape4Confirmation);

        this.add(masterContainer);

        try {
            changementEtape(nouveauPartenaire.getNumeroEtape());
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

    private void etape1Departement() {
        carteFrance.setVisible(true);
        containerEtape2.setVisible(false);
        etape3Entreprise.setVisible(false);
        etape4Confirmation.setVisible(false);
    }

    private void etape2InformationsDirigeant() {
        carteFrance.setVisible(false);
        containerEtape2.setVisible(true);
        etape3Entreprise.setVisible(false);
        etape4Confirmation.setVisible(false);
    }

    private void etape3InformationsEntreprise() {
        carteFrance.setVisible(false);
        containerEtape2.setVisible(false);
        etape3Entreprise.setVisible(true);
        etape4Confirmation.setVisible(false);
    }

    private void etape4Confirmation() {
        carteFrance.setVisible(false);
        containerEtape2.setVisible(false);
        etape3Entreprise.setVisible(false);
        etape4Confirmation.setVisible(true);
        // Appel du service d'enregistrement du nouveau partenaire
        Integer retourService = creationPartenaire();
        if (retourService == 0) {
            etape4Confirmation.succesInscription();
        } else {
            etape4Confirmation.failureInscription();
        }

    }

    private void changementEtape(Integer numeroEtape) throws FrontEndException {
        // On charge l'etape demandé grace au numero d'etape passé en parametre
        switch (numeroEtape) {
        case 1:
            loggerChangementEtape("Passage dans l'étape 1");
            etape1Departement();
            break;
        case 2:
            loggerChangementEtape("Passage dans l'étape 2");
            etape2InformationsDirigeant();
            break;
        case 3:
            loggerChangementEtape("Passage dans l'étape 3");
            etape3InformationsEntreprise();
            break;
        case 4:
            loggerChangementEtape("Passage dans l'étape 4");
            etape4Confirmation();
            break;
        default:
            throw new FrontEndException("Aucune étape du nouveau devis chargées, Situation Impossible");
        }

        navigationWizard.setStep(numeroEtape);
    }

    private void loggerChangementEtape(String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }

    private Integer creationPartenaire() {
        return 0;
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
                // SI c'est bon, on passe à l'etape 2
                try {
                    changementEtape(2);
                } catch (FrontEndException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Probleme frontend avec l'etape 2", e);
                    }
                }
                // Sinon affichage retour sur le feedback panel
            } else {
                feedBackPanelGeneral.error("Numéro de département incorrecte, veuillez recommencer");
            }
            eventMapFrance.getTarget().add(this);
        }

        // Catch de l'event de changement d'etape
        if (event.getPayload() instanceof ChangementEtapeEventArtisan) {
            ChangementEtapeEventArtisan changementEtapeEvent = (ChangementEtapeEventArtisan) event.getPayload();

            // On extrait le numero de l'etape
            this.nouveauPartenaire = changementEtapeEvent.getNouveauPartenaire();
            try {
                changementEtape(nouveauPartenaire.getNumeroEtape());
            } catch (FrontEndException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Probleme frontend avec l'etape " + nouveauPartenaire.getNumeroEtape(), e);
                }
            }

            AjaxRequestTarget targetChangementEtape = changementEtapeEvent.getTarget();
            targetChangementEtape.add(feedBackPanelGeneral);
            targetChangementEtape.add(this);
        }
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return "waiter";
    }
}
