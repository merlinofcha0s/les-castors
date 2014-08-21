package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.event.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.FrontEndException;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.component.NavigationWizard;
import fr.batimen.web.client.extend.nouveau.devis.event.MapFranceEvent;
import fr.batimen.web.client.master.MasterPage;

public class NouveauArtisan extends MasterPage {

    private static final long serialVersionUID = 7796768527786855832L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NouveauArtisan.class);

    // Composants Généraux
    private NavigationWizard navigationWizard;
    // Composants étape 1
    private final MapFrance carteFrance;

    // Composant étape 2

    public NouveauArtisan() {
        super("Inscription d'un nouveau partenaire qui effectuera les travaux chez un particulier",
                "Artisan Rénovation Inscription", "Nouveau partenaire", true, "img/bg_title1.jpg");
        super.setActiveMenu(MasterPage.NONE);

        initNavigationWizard();

        // Etape 1 : selection du departement avec la carte de la france
        carteFrance = new MapFrance("mapFrance");

        this.add(carteFrance);
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
        this.add(navigationWizard);
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
                // TODO : On enregistre le departement
                // TODO : On change d'etape
            } else {
                feedBackPanelGeneral.error("Numéro de département incorrecte, veuillez recommencer");
            }
            eventMapFrance.getTarget().add(this);
        }
    }

}
