package fr.batimen.web.client.extend.nouveau.devis;

import fr.batimen.dto.CategorieDTO;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.helper.CategorieService;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.client.extend.nouveau.communs.JSCommun;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel de l'Etape 2 de la creation d'annonce. Permet à l'utilisateur de
 * choisir la catégorie qu'il desire dans sa demande de devis.
 *
 * @author Casaucau Cyril
 */
public class Etape2Categorie extends Panel {

    private static final long serialVersionUID = -3950302126805043243L;

    @Inject
    private CategorieService categorieService;

    private List<String> motClesToString;
    private List<CategorieDTO> categoriesSelectionnee = new ArrayList<>();

    private WebMarkupContainer categoriesChoisiesContainer;

    public Etape2Categorie(String id) {
        super(id);

        motClesToString = categorieService.getAllCategories().stream().map(c -> c.getMotCle()).collect(Collectors.toList());

        TextField<String> motCleCategorie = new TextField<>("motCleField", new Model<>());
        /*motCleCategorie.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String motcle = motCleCategorie.getModelObject();
                Optional<CategorieDTO> categorieDTOSelectionnee = categorieService.getCategorieByMotCle(motcle);

                if (categorieDTOSelectionnee.isPresent() && !categoriesSelectionnee.stream().filter(cat -> cat.equals(categorieDTOSelectionnee.get())).findAny().isPresent()) {
                    categoriesSelectionnee.add(categorieDTOSelectionnee.get());
                }

                target.add(categoriesChoisiesContainer);
            }
        });*/
        /*motCleCategorie.add(new AjaxFormComponentUpdatingBehavior("onblur") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

            }
        });*/

        motCleCategorie.setOutputMarkupId(true);
        motCleCategorie.setMarkupId("motCleField");

        Form<Void> motCleForm = new Form<>("motCleForm");

        motCleForm.add(motCleCategorie);

        AjaxLink<Void> etapePrecedente2 = new AjaxLink<Void>("etapePrecedente2") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_2.ordinal() + 1);
            }
        };

        etapePrecedente2.setOutputMarkupId(true);
        etapePrecedente2.setMarkupId("etapePrecedente2");

        add(motCleForm, etapePrecedente2);

        initCategoriesChoisie();
    }

    private void createAndTriggerEvent(AjaxRequestTarget target, CategorieMetierDTO categorieMetier) {
        // On crée l'event qui sera envoyé à la page de nouveau devis
        CategorieEvent categorieEvent = new CategorieEvent(target, categorieMetier);
        // On trigger l'event
        target.getPage().send(target.getPage(), Broadcast.BREADTH, categorieEvent);
    }

    private void initCategoriesChoisie() {
        categoriesChoisiesContainer = new WebMarkupContainer("categoriesChoisiesContainer");
        categoriesChoisiesContainer.setOutputMarkupId(true);

        ListView<CategorieDTO> categorieDTOListView = new ListView<CategorieDTO>("categorieDTOListView", categoriesSelectionnee) {
            @Override
            protected void populateItem(ListItem<CategorieDTO> item) {
                CategorieDTO categorieDTO = item.getModelObject();

                Label motCle = new Label("motCle", categorieDTO.getMotCle());
                AjaxLink<CategorieDTO> cross = new AjaxLink<CategorieDTO>("cross", new Model<>(categorieDTO)) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        categoriesSelectionnee.remove(categorieDTO);
                        target.add(categoriesChoisiesContainer);
                    }
                };

                item.add(motCle, cross);
            }
        };

        categoriesChoisiesContainer.add(categorieDTOListView);
        add(categoriesChoisiesContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        String initMotClesTypeAhead = JSCommun.buildSourceTypeAheadForMotCles(motClesToString, "#motCleField");
        response.render(OnDomReadyHeaderItem.forScript(initMotClesTypeAhead));
    }

}
