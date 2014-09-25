package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
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

        final CheckBox checkBoxElectricite = new CheckBox("electricite", new Model<Boolean>());
        final WebMarkupContainer iElectricite = new WebMarkupContainer("iElectricite");
        final WebMarkupContainer containerElectricite = new WebMarkupContainer("containerElectricite");
        containerElectricite.setOutputMarkupId(true);
        containerElectricite.add(checkBoxElectricite);
        containerElectricite.add(iElectricite);
        containerActivite.add(containerElectricite);

        final CheckBox checkBoxPlomberie = new CheckBox("plomberie", new Model<Boolean>());
        final WebMarkupContainer containerPlomberie = new WebMarkupContainer("containerPlomberie");
        containerPlomberie.setOutputMarkupId(true);
        containerPlomberie.add(checkBoxPlomberie);
        containerActivite.add(containerPlomberie);

        final CheckBox checkBoxEspaceVert = new CheckBox("espaceVert", new Model<Boolean>());
        final WebMarkupContainer containerEspaceVert = new WebMarkupContainer("containerEspaceVert");
        containerEspaceVert.setOutputMarkupId(true);
        containerEspaceVert.add(checkBoxEspaceVert);
        containerActivite.add(containerEspaceVert);

        final CheckBox checkBoxDecorationMaconnerie = new CheckBox("decorationMaconnerie", new Model<Boolean>());
        final WebMarkupContainer containerDecorationMaconnerie = new WebMarkupContainer("containerDecorationMaconnerie");
        containerDecorationMaconnerie.setOutputMarkupId(true);
        containerDecorationMaconnerie.add(checkBoxDecorationMaconnerie);
        containerActivite.add(containerDecorationMaconnerie);

        final CheckBox checkBoxGrosOeuvre = new CheckBox("grosOeuvre", new Model<Boolean>());
        final WebMarkupContainer containerGrosOeuvre = new WebMarkupContainer("containerGrosOeuvre");
        containerGrosOeuvre.setOutputMarkupId(true);
        containerGrosOeuvre.add(checkBoxGrosOeuvre);
        containerActivite.add(containerGrosOeuvre);

        final CheckBox checkBoxEquipement = new CheckBox("equipement", new Model<Boolean>());
        final WebMarkupContainer containerEquipement = new WebMarkupContainer("containerEquipement");
        containerEquipement.setOutputMarkupId(true);
        containerEquipement.add(checkBoxEquipement);
        containerActivite.add(containerEquipement);

        final AjaxCheckBox checkBoxTouteslesActivites = new AjaxCheckBox("touteslesActivites", new Model<Boolean>()) {

            private static final long serialVersionUID = 7283874612963022578L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                containerElectricite.add(new AttributeModifier("class", "checkbox-custom checkbox inline checked"));
                iElectricite.add(new AttributeModifier("class", "checkbox checked"));
                target.add(containerActivite);
            }

        };
        final WebMarkupContainer containerToutesLesActivites = new WebMarkupContainer("containerToutesLesActivites");
        containerToutesLesActivites.setOutputMarkupId(true);
        containerToutesLesActivites.add(checkBoxTouteslesActivites);
        containerActivite.add(containerToutesLesActivites);

    }
}
