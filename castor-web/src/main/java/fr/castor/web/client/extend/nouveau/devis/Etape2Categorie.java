package fr.castor.web.client.extend.nouveau.devis;

import fr.castor.dto.MotCleDTO;
import fr.castor.web.app.enums.Etape;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.client.component.MotCle;
import fr.castor.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.ArrayList;
import java.util.List;

/**
 * Panel de l'Etape 2 de la creation d'annonce. Permet à l'utilisateur de
 * choisir la catégorie qu'il desire dans sa demande de devis.
 *
 * @author Casaucau Cyril
 */
public class Etape2Categorie extends Panel {

    private static final long serialVersionUID = -3950302126805043243L;

    private List<MotCleDTO> categoriesSelectionnees = new ArrayList<>();

    public Etape2Categorie(String id) {
        super(id);

        MotCle motCle = new MotCle("motCle");

        AjaxLink<Void> etapePrecedente2 = new AjaxLink<Void>("etapePrecedente2") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_2.ordinal() + 1);
            }
        };

        etapePrecedente2.setOutputMarkupId(true);
        etapePrecedente2.setMarkupId("etapePrecedente2");

        AjaxLink<Void> etapeSuivante = new AjaxLink<Void>("etapeSuivante") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                categoriesSelectionnees = motCle.getCategoriesSelectionnees();

                if (categoriesSelectionnees.isEmpty()) {
                    MasterPage.triggerEventFeedBackPanel(target, motCle.ERROR_MESSAGE, FeedbackMessageLevel.ERROR);
                } else {
                    // On crée l'event qui sera envoyé à la page de nouveau devis
                    CategorieEvent categorieEvent = new CategorieEvent(target, categoriesSelectionnees);
                    // On trigger l'event
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, categorieEvent);
                }

            }
        };

        etapeSuivante.setOutputMarkupId(true);
        etapeSuivante.setMarkupId("etapeSuivante");

        add(motCle, etapePrecedente2, etapeSuivante);
    }
}