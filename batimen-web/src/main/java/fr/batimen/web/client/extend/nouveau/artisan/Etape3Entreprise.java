package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.client.component.BatimenToolTip;
import fr.batimen.web.client.extend.nouveau.artisan.event.UncheckedEvent;

/**
 * Etape 3 de l'inscription d'un nouvel artisan : Informations sur l'entreprise
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape3Entreprise extends Panel {

    private static final long serialVersionUID = -4959756477938900372L;

    private Etape3EntrepriseForm etape3EntrepriseForm;
    private List<CategorieMetierDTO> categoriesSelectionnees;
    private WebMarkupContainer containerActivite;

    public Etape3Entreprise(String id, IModel<?> model) {
        super(id, model);
    }

    public Etape3Entreprise(String id, IModel<?> model, final CreationPartenaireDTO nouveauPartenaire) {
        this(id, model);

        this.add(BatimenToolTip.getTooltipBehaviour());

        categoriesSelectionnees = nouveauPartenaire.getEntreprise().getCategoriesMetier();

        etape3EntrepriseForm = new Etape3EntrepriseForm("etape3EntrepriseForm",
                new CompoundPropertyModel<CreationPartenaireDTO>(nouveauPartenaire));
        containerActivite = new WebMarkupContainer("containerActivite");
        containerActivite.setOutputMarkupId(true);
        containerActivite.setMarkupId("containerActivite");

        initActiviteSelection();

        this.add(etape3EntrepriseForm);
        this.add(containerActivite);

    }

    private void initActiviteSelection() {

        final WebMarkupContainer iElectricite = new WebMarkupContainer("iElectricite");
        final WebMarkupContainer containerElectricite = new WebMarkupContainer("containerElectricite");
        final AjaxCheckBox checkBoxElectricite = new AjaxCheckBox("electricite", new Model<Boolean>()) {

            private static final long serialVersionUID = 8274255006449725507L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    categoriesSelectionnees.add(CategorieLoader.getCategorieElectricite());
                    checked(containerElectricite, iElectricite, this);
                } else {
                    categoriesSelectionnees.remove(CategorieLoader.getCategorieElectricite());
                    unChecked(containerElectricite, iElectricite, this);
                    sendUncheckedAllCheckBox(target);
                }
                target.add(containerActivite);
            }

        };

        checkBoxElectricite.setMarkupId("electriciteCheck");
        containerElectricite.setOutputMarkupId(true);
        containerElectricite.setMarkupId("containerElectricite");
        containerElectricite.add(checkBoxElectricite);
        containerElectricite.add(iElectricite);
        containerActivite.add(containerElectricite);

        final WebMarkupContainer iPlomberie = new WebMarkupContainer("iPlomberie");
        final WebMarkupContainer containerPlomberie = new WebMarkupContainer("containerPlomberie");
        final AjaxCheckBox checkBoxPlomberie = new AjaxCheckBox("plomberie", new Model<Boolean>()) {

            private static final long serialVersionUID = -4711312112702359439L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    categoriesSelectionnees.add(CategorieLoader.getCategoriePlomberie());
                    checked(containerPlomberie, iPlomberie, this);
                } else {
                    categoriesSelectionnees.remove(CategorieLoader.getCategoriePlomberie());
                    unChecked(containerPlomberie, iPlomberie, this);
                    sendUncheckedAllCheckBox(target);
                }
                target.add(containerActivite);
            }

        };

        checkBoxPlomberie.setMarkupId("plomberieSelect");
        containerPlomberie.setOutputMarkupId(true);
        containerPlomberie.setMarkupId("containerPlomberie");
        containerPlomberie.add(checkBoxPlomberie);
        containerPlomberie.add(iPlomberie);
        containerActivite.add(containerPlomberie);

        final WebMarkupContainer containerEspaceVert = new WebMarkupContainer("containerEspaceVert");
        final WebMarkupContainer iEspaceVert = new WebMarkupContainer("iEspaceVert");
        final AjaxCheckBox checkBoxEspaceVert = new AjaxCheckBox("espaceVert", new Model<Boolean>()) {

            private static final long serialVersionUID = -1839696014719001158L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    categoriesSelectionnees.add(CategorieLoader.getCategorieEspaceVert());
                    checked(containerEspaceVert, iEspaceVert, this);
                } else {
                    categoriesSelectionnees.remove(CategorieLoader.getCategorieEspaceVert());
                    unChecked(containerEspaceVert, iEspaceVert, this);
                    sendUncheckedAllCheckBox(target);
                }
                target.add(containerActivite);
            }
        };

        checkBoxEspaceVert.setMarkupId("espaceVertCheck");
        containerEspaceVert.setOutputMarkupId(true);
        containerEspaceVert.setMarkupId("containerEspaceVert");
        containerEspaceVert.add(checkBoxEspaceVert);
        containerEspaceVert.add(iEspaceVert);
        containerActivite.add(containerEspaceVert);

        final WebMarkupContainer containerDecorationMaconnerie = new WebMarkupContainer("containerDecorationMaconnerie");
        final WebMarkupContainer iDecorationMaconnerie = new WebMarkupContainer("iDecorationMaconnerie");
        final AjaxCheckBox checkBoxDecorationMaconnerie = new AjaxCheckBox("decorationMaconnerie", new Model<Boolean>()) {

            private static final long serialVersionUID = -4193158638385896517L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    categoriesSelectionnees.add(CategorieLoader.getCategorieDecorationMaconnerie());
                    checked(containerDecorationMaconnerie, iDecorationMaconnerie, this);
                } else {
                    categoriesSelectionnees.remove(CategorieLoader.getCategorieDecorationMaconnerie());
                    unChecked(containerDecorationMaconnerie, iDecorationMaconnerie, this);
                    sendUncheckedAllCheckBox(target);
                }
                target.add(containerActivite);
            }
        };

        checkBoxDecorationMaconnerie.setMarkupId("decorationMaconnerieCheck");
        containerDecorationMaconnerie.setOutputMarkupId(true);
        containerDecorationMaconnerie.setMarkupId("containerDecorationMaconnerie");
        containerDecorationMaconnerie.add(checkBoxDecorationMaconnerie);
        containerDecorationMaconnerie.add(iDecorationMaconnerie);
        containerActivite.add(containerDecorationMaconnerie);

        final WebMarkupContainer containerToutesLesActivites = new WebMarkupContainer("containerToutesLesActivites");
        containerToutesLesActivites.setOutputMarkupId(true);

        final WebMarkupContainer iToutesActivite = new WebMarkupContainer("iToutesLesActivites");
        final AjaxCheckBox checkBoxTouteslesActivites = new AjaxCheckBox("touteslesActivites", new Model<Boolean>()) {

            private static final long serialVersionUID = 7283874612963022578L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    categoriesSelectionnees.addAll(CategorieLoader.getAllCategories());
                    checked(containerElectricite, iElectricite, checkBoxElectricite);
                    checked(containerToutesLesActivites, iToutesActivite, this);
                    checked(containerPlomberie, iPlomberie, checkBoxPlomberie);
                    checked(containerEspaceVert, iEspaceVert, checkBoxEspaceVert);
                    checked(containerDecorationMaconnerie, iDecorationMaconnerie, checkBoxDecorationMaconnerie);
                } else {
                    categoriesSelectionnees.removeAll(CategorieLoader.getAllCategories());
                    unChecked(containerElectricite, iElectricite, checkBoxElectricite);
                    unChecked(containerToutesLesActivites, iToutesActivite, this);
                    unChecked(containerPlomberie, iPlomberie, checkBoxPlomberie);
                    unChecked(containerEspaceVert, iEspaceVert, checkBoxEspaceVert);
                    unChecked(containerDecorationMaconnerie, iDecorationMaconnerie, checkBoxDecorationMaconnerie);

                }
                target.add(containerActivite);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent
             * )
             */
            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event.getPayload() instanceof UncheckedEvent) {
                    this.setModelObject(Boolean.FALSE);
                    unChecked(containerToutesLesActivites, iToutesActivite, this);
                }
            }

        };
        checkBoxTouteslesActivites.setMarkupId("toutesLesActivitesCheck");
        containerToutesLesActivites.setMarkupId("checkBoxTouteslesActivites");
        containerToutesLesActivites.add(checkBoxTouteslesActivites);
        containerToutesLesActivites.add(iToutesActivite);
        containerActivite.add(containerToutesLesActivites);
    }

    private void unChecked(WebMarkupContainer containerPrincipal, WebMarkupContainer iCheck, AjaxCheckBox checkBox) {
        containerPrincipal.add(new AttributeModifier("class", "checkbox-custom checkbox inline"));
        iCheck.add(new AttributeModifier("class", "checkbox"));
        checkBox.setModelObject(Boolean.FALSE);
    }

    private void checked(WebMarkupContainer containerPrincipal, WebMarkupContainer iCheck, AjaxCheckBox checkBox) {
        containerPrincipal.add(new AttributeModifier("class", "checkbox-custom checkbox inline checked"));
        iCheck.add(new AttributeModifier("class", "checkbox checked"));
        checkBox.setModelObject(Boolean.TRUE);
    }

    private void sendUncheckedAllCheckBox(AjaxRequestTarget target) {
        UncheckedEvent uncheckedEvent = new UncheckedEvent(target);
        this.send(target.getPage(), Broadcast.BREADTH, uncheckedEvent);
    }
}
