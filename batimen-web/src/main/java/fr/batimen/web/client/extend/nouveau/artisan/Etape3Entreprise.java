package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.client.component.BatimenToolTip;

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
                    checked(containerElectricite, iElectricite);
                } else {
                    unChecked(containerElectricite, iElectricite);
                }
                target.add(containerActivite);
            }

        };

        containerElectricite.setOutputMarkupId(true);
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
                    checked(containerPlomberie, iPlomberie);
                } else {
                    unChecked(containerPlomberie, iPlomberie);
                }
                target.add(containerActivite);
            }

        };

        containerPlomberie.setOutputMarkupId(true);
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
                    checked(containerEspaceVert, iEspaceVert);
                } else {
                    unChecked(containerEspaceVert, iEspaceVert);
                }
                target.add(containerActivite);
            }
        };

        containerEspaceVert.setOutputMarkupId(true);
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
                    checked(containerDecorationMaconnerie, iDecorationMaconnerie);
                } else {
                    unChecked(containerDecorationMaconnerie, iDecorationMaconnerie);
                }
                target.add(containerActivite);
            }
        };

        containerDecorationMaconnerie.setOutputMarkupId(true);
        containerDecorationMaconnerie.add(checkBoxDecorationMaconnerie);
        containerDecorationMaconnerie.add(iDecorationMaconnerie);
        containerActivite.add(containerDecorationMaconnerie);

        final WebMarkupContainer containerGrosOeuvre = new WebMarkupContainer("containerGrosOeuvre");
        final WebMarkupContainer iGrosOeuvre = new WebMarkupContainer("iGrosOeuvre");
        final AjaxCheckBox checkBoxGrosOeuvre = new AjaxCheckBox("grosOeuvre", new Model<Boolean>()) {

            private static final long serialVersionUID = 4680845561587623070L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    checked(containerGrosOeuvre, iGrosOeuvre);
                } else {
                    unChecked(containerGrosOeuvre, iGrosOeuvre);
                }
                target.add(containerActivite);
            }
        };

        containerGrosOeuvre.setOutputMarkupId(true);
        containerGrosOeuvre.add(checkBoxGrosOeuvre);
        containerGrosOeuvre.add(iGrosOeuvre);
        containerActivite.add(containerGrosOeuvre);

        final WebMarkupContainer containerEquipement = new WebMarkupContainer("containerEquipement");
        final WebMarkupContainer iEquipement = new WebMarkupContainer("iEquipement");
        final AjaxCheckBox checkBoxEquipement = new AjaxCheckBox("equipement", new Model<Boolean>()) {

            private static final long serialVersionUID = -6356759804303259948L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    checked(containerEquipement, iEquipement);
                } else {
                    unChecked(containerEquipement, iEquipement);
                }
                target.add(containerActivite);
            }
        };

        containerEquipement.setOutputMarkupId(true);
        containerEquipement.add(checkBoxEquipement);
        containerEquipement.add(iEquipement);
        containerActivite.add(containerEquipement);

        final WebMarkupContainer containerToutesLesActivites = new WebMarkupContainer("containerToutesLesActivites");
        containerToutesLesActivites.setOutputMarkupId(true);
        final WebMarkupContainer iToutesActivite = new WebMarkupContainer("iToutesLesActivites");
        final AjaxCheckBox checkBoxTouteslesActivites = new AjaxCheckBox("touteslesActivites", new Model<Boolean>()) {

            private static final long serialVersionUID = 7283874612963022578L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (this.getModelObject()) {
                    checked(containerElectricite, iElectricite);
                    checked(containerPlomberie, iPlomberie);
                    checked(containerEspaceVert, iEspaceVert);
                    checked(containerDecorationMaconnerie, iDecorationMaconnerie);
                    checked(containerGrosOeuvre, iGrosOeuvre);
                    checked(containerEquipement, iEquipement);
                    checked(containerToutesLesActivites, iToutesActivite);
                } else {
                    unChecked(containerElectricite, iElectricite);
                    unChecked(containerPlomberie, iPlomberie);
                    unChecked(containerEspaceVert, iEspaceVert);
                    unChecked(containerDecorationMaconnerie, iDecorationMaconnerie);
                    unChecked(containerGrosOeuvre, iGrosOeuvre);
                    unChecked(containerEquipement, iEquipement);
                    unChecked(containerToutesLesActivites, iToutesActivite);
                }
                target.add(containerActivite);

            }

        };
        containerToutesLesActivites.add(checkBoxTouteslesActivites);
        containerToutesLesActivites.add(iToutesActivite);
        containerActivite.add(containerToutesLesActivites);
    }

    private void unChecked(WebMarkupContainer containerPrincipal, WebMarkupContainer iCheck) {
        containerPrincipal.add(new AttributeModifier("class", "checkbox-custom checkbox inline"));
        iCheck.add(new AttributeModifier("class", "checkbox"));
    }

    private void checked(WebMarkupContainer containerPrincipal, WebMarkupContainer iCheck) {
        containerPrincipal.add(new AttributeModifier("class", "checkbox-custom checkbox inline checked"));
        iCheck.add(new AttributeModifier("class", "checkbox checked"));
    }
}
