package fr.castor.web.client.component;

import fr.castor.dto.MotCleDTO;
import fr.castor.dto.helper.CategorieService;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.client.behaviour.AjaxMotCleBehaviour;
import fr.castor.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.castor.web.client.event.MotCleEvent;
import fr.castor.web.client.extend.nouveau.communs.JSCommun;
import fr.castor.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
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
 * Composant qui permet d'ajouter un mot clé à l'annonce.
 * Ce mot clé est lié a des catégories
 *
 * On utilise le type ahead pour proposer des résultats à l'utilisateur
 *
 * Quand l'utilisateur clique sur un résultat proposé, on declenche un MotCleEvent grace au callback script qu'on charge
 * dans initMotClesTypeAheadJS dans le render head
 *
 * Il y a deux modes de recherche soit l'utilisateur clique sur une proposition, soit il tape le mot entier et valide grace au bouton etape suivante.
 * Dans ce cas la la logique se passe sur le formulaire d'au dessus (Etape2Categorie)
 */
public class MotCle extends Panel {

    public static final String ERROR_MESSAGE = "Mot clé non reconnu, veuillez selectionner au moins un mot clé";
    @Inject
    private CategorieService categorieService;
    private List<String> motClesToString;
    private List<MotCleDTO> categoriesSelectionnees;
    private String initMotClesTypeAheadJS;
    private WebMarkupContainer categoriesChoisiesContainer;
    private TextField<String> motCleCategorie;
    private AjaxMotCleBehaviour ajaxMotCleBehaviour;
    private ListView<MotCleDTO> categorieDTOListView;

    public MotCle(String id) {
        super(id);
        setOutputMarkupId(true);

        initMotCleTextField();
        initCategoriesChoisie();
    }

    public MotCle(String id, List<MotCleDTO> categoriesSelectionnees) {
        super(id);
        if(categoriesSelectionnees != null){
            this.categoriesSelectionnees = categoriesSelectionnees;
        } else {
            this.categoriesSelectionnees = new ArrayList<>();
        }

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

        categorieDTOListView = new ListView<MotCleDTO>("categorieDTOListView", categoriesSelectionnees) {
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
            AjaxRequestTarget target = motCleEvent.getTarget();

            String motcle = motCleEvent.getMotCle();
            rechercheEtChargeMotCle(motcle);
            refreshUI(motCleEvent.getTarget());
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new CategorieEvent(target, categoriesSelectionnees, false));
        }
    }

    public void refreshUI(AjaxRequestTarget target){
        motCleCategorie.clearInput();
        target.add(motCleCategorie);
        target.appendJavaScript(initMotClesTypeAheadJS);
        target.add(categoriesChoisiesContainer);

    }

    private void rechercheEtChargeMotCle(String motcle){
        //On recherche si il y a au moins une occurence qui correspond à la chaine de caractere de l'utilisateur
        Optional<MotCleDTO> motCleSelectionne = categorieService.getCategorieByMotCle(motcle);

        //Si un mot clé a été trouvé et qu'il n'a pas deja été ajouter à la liste alors on l'ajoute
        if (motCleSelectionne.isPresent() && !categoriesSelectionnees.stream().filter(cat -> cat.equals(motCleSelectionne.get())).findAny().isPresent()) {
            categoriesSelectionnees.add(motCleSelectionne.get());
        }
    }

    public void rechercheWithInputTextField(){
        rechercheEtChargeMotCle(motCleCategorie.getInput());
    }

    public List<MotCleDTO> getCategoriesSelectionnees() {
        return categoriesSelectionnees;
    }
}