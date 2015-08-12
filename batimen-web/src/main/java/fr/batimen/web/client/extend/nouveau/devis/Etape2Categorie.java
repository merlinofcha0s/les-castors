package fr.batimen.web.client.extend.nouveau.devis;

import fr.batimen.dto.CategorieDTO;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.helper.CategorieIniter;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Panel;

import javax.inject.Inject;
import java.util.List;

/**
 * Panel de l'Etape 2 de la creation d'annonce. Permet à l'utilisateur de
 * choisir la catégorie qu'il desire dans sa demande de devis.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape2Categorie extends Panel {

    private static final long serialVersionUID = -3950302126805043243L;

    @Inject
    private CategorieIniter categorieIniter;

    public Etape2Categorie(String id) {
        super(id);

        AjaxLink<String> electriciteLink = new AjaxLink<String>("electricite") {

            private static final long serialVersionUID = -614532061323998813L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategorieElectricite());
            }

        };

        electriciteLink.setOutputMarkupId(true);
        electriciteLink.setMarkupId("electricite");

        AjaxLink<String> plomberieLink = new AjaxLink<String>("plomberie") {

            /**
             * 
             */
            private static final long serialVersionUID = 3348068130513170534L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategoriePlomberie());
            }

        };

        plomberieLink.setOutputMarkupId(true);
        plomberieLink.setMarkupId("plomberie");

        AjaxLink<String> espacesVertLink = new AjaxLink<String>("espaceVert") {

            /**
             * 
             */
            private static final long serialVersionUID = 2357941702285210447L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategorieEspaceVert());
            }

        };

        espacesVertLink.setOutputMarkupId(true);
        espacesVertLink.setMarkupId("espaceVert");

        AjaxLink<String> decorationMaconnerieLink = new AjaxLink<String>("decorationMaconnerie") {

            /**
             * 
             */
            private static final long serialVersionUID = -8999749142033429880L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategorieDecorationMaconnerie());
            }

        };

        AjaxLink<Void> etapePrecedente2 = new AjaxLink<Void>("etapePrecedente2") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_2.ordinal() + 1);
            }
        };

        etapePrecedente2.setOutputMarkupId(true);
        etapePrecedente2.setMarkupId("etapePrecedente2");

        decorationMaconnerieLink.setOutputMarkupId(true);
        decorationMaconnerieLink.setMarkupId("decorationMaconnerie");

        List<CategorieDTO> categorieDTOList = categorieIniter.getAllCategories();

        add(electriciteLink, plomberieLink, espacesVertLink, decorationMaconnerieLink, etapePrecedente2);
    }

    private void createAndTriggerEvent(AjaxRequestTarget target, CategorieMetierDTO categorieMetier) {
        // On crée l'event qui sera envoyé à la page de nouveau devis
        CategorieEvent categorieEvent = new CategorieEvent(target, categorieMetier);
        // On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, categorieEvent);
    }

}
