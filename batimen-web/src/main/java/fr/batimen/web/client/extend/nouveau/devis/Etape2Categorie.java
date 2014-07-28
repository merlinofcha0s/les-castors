package fr.batimen.web.client.extend.nouveau.devis;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Panel;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;

/**
 * Panel de l'Etape 2 de la creation d'annonce. Permet à l'utilisateur de
 * choisir la catégorie qu'il desire dans sa demande de devis.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape2Categorie extends Panel {

    private static final long serialVersionUID = -3950302126805043243L;

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

        decorationMaconnerieLink.setOutputMarkupId(true);
        decorationMaconnerieLink.setMarkupId("decorationMaconnerie");

        AjaxLink<String> grosOeuvreLink = new AjaxLink<String>("grosOeuvre") {

            /**
             * 
             */
            private static final long serialVersionUID = -5125845408338328259L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategorieGrosOeuvre());
            }

        };

        grosOeuvreLink.setOutputMarkupId(true);
        grosOeuvreLink.setMarkupId("grosOeuvre");

        AjaxLink<String> equipementLink = new AjaxLink<String>("equipement") {

            /**
             * 
             */
            private static final long serialVersionUID = 5693979328585286647L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                createAndTriggerEvent(target, CategorieLoader.getCategorieEquipement());
            }

        };

        equipementLink.setOutputMarkupId(true);
        equipementLink.setMarkupId("equipement");

        add(electriciteLink);
        add(plomberieLink);
        add(espacesVertLink);
        add(decorationMaconnerieLink);
        add(grosOeuvreLink);
        add(equipementLink);
    }

    private void createAndTriggerEvent(AjaxRequestTarget target, CategorieMetierDTO categorieMetier) {
        // On crée l'event qui sera envoyé à la page de nouveau devis
        CategorieEvent categorieEvent = new CategorieEvent(target, categorieMetier);
        // On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, categorieEvent);
    }

}
