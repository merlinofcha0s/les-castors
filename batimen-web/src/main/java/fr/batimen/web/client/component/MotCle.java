package fr.batimen.web.client.component;

import fr.batimen.dto.MotCleDTO;
import fr.batimen.dto.helper.CategorieService;
import fr.batimen.web.client.behaviour.AjaxMotCleBehaviour;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.MotCleEvent;
import fr.batimen.web.client.extend.nouveau.communs.JSCommun;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Casaucau on 28/08/2015.
 */
public class MotCle extends Panel {

    @Inject
    private CategorieService categorieService;

    private List<String> motClesToString;
    private List<MotCleDTO> categoriesSelectionnees = new ArrayList<>();
    private String initMotClesTypeAheadJS;

    private WebMarkupContainer categoriesChoisiesContainer;
    private TextField<String> motCleCategorie;
    private AjaxMotCleBehaviour ajaxMotCleBehaviour;

    public MotCle(String id) {
        super(id);

        initMotCleTextField();
        initCategoriesChoisie();
    }

    public MotCle(String id, List<MotCleDTO> categoriesSelectionnees) {
        super(id);
        this.categoriesSelectionnees = categoriesSelectionnees;
        initMotCleTextField();
        initCategoriesChoisie();
    }

    private void initMotCleTextField() {
        motClesToString = categorieService.getAllCategories().stream().map(c -> c.getMotCle()).collect(Collectors.toList());

        motCleCategorie = new TextField<>("motCleField", new Model<>());
        motCleCategorie.add(new RequiredBorderBehaviour());
        motCleCategorie.setOutputMarkupId(true);
        motCleCategorie.setMarkupId("motCleField");

        ajaxMotCleBehaviour = new AjaxMotCleBehaviour();
        motCleCategorie.add(ajaxMotCleBehaviour);

        Form<Void> motCleForm = new Form<>("motCleForm");

        motCleForm.add(motCleCategorie);

        add(motCleForm);
    }

    private void initCategoriesChoisie() {
        categoriesChoisiesContainer = new WebMarkupContainer("categoriesChoisiesContainer");
        categoriesChoisiesContainer.setOutputMarkupId(true);
        categoriesChoisiesContainer.setMarkupId("categoriesChoisiesContainer");

        ListView<MotCleDTO> categorieDTOListView = new ListView<MotCleDTO>("categorieDTOListView", categoriesSelectionnees) {
            @Override
            protected void populateItem(ListItem<MotCleDTO> item) {
                MotCleDTO categorieDTO = item.getModelObject();

                Label motCle = new Label("motCle", categorieDTO.getMotCle());
                AjaxLink<MotCleDTO> cross = new AjaxLink<MotCleDTO>("cross", new Model<>(categorieDTO)) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        categoriesSelectionnees.remove(categorieDTO);
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
        initMotClesTypeAheadJS = JSCommun.buildSourceTypeAheadForMotCles(motClesToString, "#motCleField", ajaxMotCleBehaviour.getCallbackScript().toString());
        response.render(OnDomReadyHeaderItem.forScript(initMotClesTypeAheadJS));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        if (event.getPayload() instanceof MotCleEvent) {
            MotCleEvent motCleEvent = (MotCleEvent) event.getPayload();

            String motcle = motCleEvent.getMotCle();
            Optional<MotCleDTO> motCleSelectionne = categorieService.getCategorieByMotCle(motcle);

            if (motCleSelectionne.isPresent() && !categoriesSelectionnees.stream().filter(cat -> cat.equals(motCleSelectionne.get())).findAny().isPresent()) {
                categoriesSelectionnees.add(motCleSelectionne.get());
            }

            motCleCategorie.clearInput();

            motCleEvent.getTarget().add(motCleCategorie);
            motCleEvent.getTarget().appendJavaScript(initMotClesTypeAheadJS);
            motCleEvent.getTarget().add(categoriesChoisiesContainer);
        }
    }

    public List<MotCleDTO> getCategoriesSelectionnees() {
        return categoriesSelectionnees;
    }
}
