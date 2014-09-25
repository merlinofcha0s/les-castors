package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

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
    private WebMarkupContainer containerCategorieSelection;
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

        initActiviteSelection();

        this.add(etape3EntrepriseForm);
        this.add(containerActivite);

    }

    private void initActiviteSelection() {
        CheckBox checkBoxElectricite = new CheckBox("electricite");
        containerActivite.add(checkBoxElectricite);
    }
}
