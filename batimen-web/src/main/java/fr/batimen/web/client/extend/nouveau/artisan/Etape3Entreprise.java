package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogAnimateOption;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.client.event.FeedBackPanelEvent;

public class Etape3Entreprise extends Panel {

    private static final long serialVersionUID = -4959756477938900372L;

    private Etape3EntrepriseForm etape3EntrepriseForm;
    private List<CategorieMetierDTO> categoriesSelectionnees;
    private WebMarkupContainer containerCategorieSelection;
    private WebMarkupContainer containerActivite;

    public Etape3Entreprise(String id, IModel<?> model) {
        super(id, model);
    }

    public Etape3Entreprise(String id, IModel<?> model, final CreationPartenaireDTO nouveauPartenaire) {
        this(id, model);

        // Etape 3 : Information de l'entreprise
        etape3EntrepriseForm = new Etape3EntrepriseForm("etape3EntrepriseForm",
                new CompoundPropertyModel<CreationPartenaireDTO>(nouveauPartenaire)) {

            private static final long serialVersionUID = -7951018707634233008L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit() {
                nouveauPartenaire.setNumeroEtape(4);
                this.setResponsePage(new NouveauArtisan(nouveauPartenaire));
            }
        };

        final Dialog activiteDialog = initActiviteDialog();

        categoriesSelectionnees = new ArrayList<CategorieMetierDTO>();

        Image ajouterImg = new Image("ajouterImg", new ContextRelativeResource("img/add.png"));

        AjaxLink<Void> ajouterActivite = new AjaxLink<Void>("ajouterActivite") {

            private static final long serialVersionUID = -9008353963454924193L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                activiteDialog.open(target);
            }
        };

        ajouterActivite.add(ajouterImg);

        ListView<CategorieMetierDTO> categoriesSelectionneesAffichage = new ListView<CategorieMetierDTO>(
                "entreprise.categoriesMetier", categoriesSelectionnees) {

            private static final long serialVersionUID = -5218867993988535379L;

            @Override
            protected void populateItem(final ListItem<CategorieMetierDTO> item) {
                item.add(new Label("categorieSel", new PropertyModel<String>(item.getModel(), "name")));

                Image removeImgCategorie = new Image("removeImgCategorie",
                        new ContextRelativeResource("img/remove.png"));

                AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLinkCategorie") {

                    private static final long serialVersionUID = 4175339072981444071L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        categoriesSelectionnees.remove(item.getModel().getObject());
                        target.add(containerCategorieSelection);
                    }
                };
                removeLink.add(removeImgCategorie);
                item.add(removeLink);
            }
        };

        containerCategorieSelection = new WebMarkupContainer("containerCategorieSelection");
        containerCategorieSelection.add(categoriesSelectionneesAffichage);

        containerCategorieSelection.setOutputMarkupId(true);

        containerActivite = new WebMarkupContainer("containerActivite");
        containerActivite.add(ajouterActivite);
        containerActivite.add(containerCategorieSelection);

        this.add(etape3EntrepriseForm);
        this.add(activiteDialog);
        this.add(containerActivite);

    }

    private Dialog initActiviteDialog() {
        final Dialog activiteDialog = new Dialog("activiteDialog");
        activiteDialog.setModal(true);
        activiteDialog.setTitle("Ajouter une activité");
        activiteDialog.setResizable(false);
        activiteDialog.setDraggable(false);
        activiteDialog.setWidth(420);
        activiteDialog.setShow(new DialogAnimateOption("fade"));
        activiteDialog.setHide(new DialogAnimateOption("fade"));
        activiteDialog.setOutputMarkupId(true);

        final DropDownChoice<CategorieMetierDTO> categorieMetier = new DropDownChoice<CategorieMetierDTO>(
                "dropdownCategoriesMetier", new Model<CategorieMetierDTO>(), CategorieLoader.getAllCategories(true));

        AjaxSubmitLink enregistreCategorie = new AjaxSubmitLink("enregistrerCategorie") {

            private static final long serialVersionUID = 2669048054198891618L;

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                CategorieMetierDTO categorieSelectionnee = categorieMetier.getConvertedInput();

                if (categoriesSelectionnees.contains(categorieSelectionnee)) {
                    triggerEventFeedBackPanel(target, "Vous ne pouvez pas sélectionner deux fois la même catégorie");
                } else if (categoriesSelectionnees.contains(CategorieLoader.getCategorieAll())) {
                    triggerEventFeedBackPanel(target, "Vous avez déjà selectionné toutes les categories");
                } else {
                    categoriesSelectionnees.add(categorieSelectionnee);
                }

                activiteDialog.close(target);
                target.add(containerCategorieSelection);
            }

        };

        Form<Void> formSelectionActivite = new Form<Void>("formSelectionActivite");
        formSelectionActivite.add(categorieMetier);
        formSelectionActivite.add(enregistreCategorie);

        activiteDialog.add(formSelectionActivite);

        return activiteDialog;
    }

    private void triggerEventFeedBackPanel(AjaxRequestTarget target, String message) {
        FeedBackPanelEvent feedbackPanelEvent = new FeedBackPanelEvent(target);
        feedbackPanelEvent.setMessage(message);
        target.getPage().send(target.getPage(), Broadcast.EXACT, feedbackPanelEvent);
    }

}
