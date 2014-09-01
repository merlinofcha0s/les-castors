package fr.batimen.web.client.extend.nouveau.artisan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.MapFranceEvent;
import fr.batimen.web.client.master.MasterPage;

public class NouveauArtisan extends MasterPage {

    private static final long serialVersionUID = 7796768527786855832L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NouveauArtisan.class);

    // DTO
    private CreationPartenaireDTO nouveauPartenaire;

    // Composants Généraux
    private NavigationWizard navigationWizard;
    // Composants étape 1
    private final MapFrance carteFrance;

    // Composant étape 2
    private final Etape2PartenaireForm etape2PartenaireForm;
    private final WebMarkupContainer containerEtape2;

    // Composant étape 3
    private final Etape3Entreprise etape3Entreprise;
    private final WebMarkupContainer containerEtape3;

    public NouveauArtisan() {
        super("Inscription d'un nouveau partenaire qui effectuera les travaux chez un particulier",
                "Artisan Rénovation Inscription", "Nouveau partenaire", true, "img/bg_title1.jpg");
        super.setActiveMenu(MasterPage.NONE);

        nouveauPartenaire = new CreationPartenaireDTO();

        initNavigationWizard();

        // Etape 1 : selection du departement avec la carte de la france
        carteFrance = new MapFrance("mapFrance");

        // Etape 2 : Informations du dirigeant
        etape2PartenaireForm = new Etape2PartenaireForm("etape2PartenaireForm",
                new CompoundPropertyModel<CreationPartenaireDTO>(nouveauPartenaire)) {

            private static final long serialVersionUID = -3398962583371973217L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit() {
                nouveauPartenaire.setNumeroEtape(3);
                this.setResponsePage(new NouveauArtisan(nouveauPartenaire));
            }

        };
        containerEtape2 = new WebMarkupContainer("containerEtape2");
        containerEtape2.add(etape2PartenaireForm);
        containerEtape2.setVisible(false);

        etape3Entreprise = new Etape3Entreprise("etape3", new Model<Serializable>(), nouveauPartenaire);

        containerEtape3 = new WebMarkupContainer("containerEtape3");
        containerEtape3.add(etape3Entreprise);
        containerEtape3.setVisible(false);

        this.add(carteFrance);
        this.add(containerEtape2);
        this.add(containerEtape3);

        try {
            changementEtape(/* nouveauPartenaire.getNumeroEtape() */3);
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur lors du changment d'étape pendant la creation d'un nouveau partenaire", e);
            }
        }
    }

    public NouveauArtisan(CreationPartenaireDTO nouveauPartenaire) {
        this();
        this.nouveauPartenaire = nouveauPartenaire;
        try {
            changementEtape(nouveauPartenaire.getNumeroEtape());
        } catch (FrontEndException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme frontend", e);
            }
        }
    };

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
        this.add(navigationWizard);
    }

    private void etape1Departement() {
        carteFrance.setVisible(true);
        containerEtape2.setVisible(false);
        containerEtape3.setVisible(false);
    }

    private void etape2InformationsDirigeant() {
        carteFrance.setVisible(false);
        containerEtape2.setVisible(true);
        containerEtape3.setVisible(false);
    }

    private void etape3InformationsEntreprise() {
        carteFrance.setVisible(false);
        containerEtape2.setVisible(false);
        containerEtape3.setVisible(true);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof MapFranceEvent) {
            MapFranceEvent eventMapFrance = (MapFranceEvent) event.getPayload();

            // On récupére le departement qui se trouve dans l'event
            Integer departementInt = Integer.valueOf(eventMapFrance.getDepartement());

            if (departementInt != null && departementInt > 0 && departementInt < 100) {
                nouveauPartenaire.getAdresse().setDepartement(departementInt);
                nouveauPartenaire.setNumeroEtape(2);
                try {
                    changementEtape(2);
                } catch (FrontEndException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Probleme frontend avec l'etape 2", e);
                    }
                }
            } else {
                feedBackPanelGeneral.error("Numéro de département incorrecte, veuillez recommencer");
            }
            eventMapFrance.getTarget().add(this);
        }

        if (event.getPayload() instanceof FeedBackPanelEvent) {
            FeedBackPanelEvent feedBackPanelUpdate = (FeedBackPanelEvent) event.getPayload();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Affichage message dans le feedBackPanel");
            }
            feedBackPanelGeneral.error(feedBackPanelUpdate.getMessage());
            feedBackPanelUpdate.getTarget().add(feedBackPanelGeneral);
        }
    }

}
