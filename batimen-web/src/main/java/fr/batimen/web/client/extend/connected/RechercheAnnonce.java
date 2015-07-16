package fr.batimen.web.client.extend.connected;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.aggregate.SearchAnnonceDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.*;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.DateValidator;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Page de recherche des annonce, destinée au Artisan
 *
 * @author Casaucau Cyril
 */
public class RechercheAnnonce extends MasterPage {

    @Inject
    private Authentication authentication;

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    private CastorDatePicker castorDatePicker;

    private WebMarkupContainer resultatContainer;

    private List<AnnonceDTO> annonceDTOList = new ArrayList<>();

    private static final String REFRESH_TOOLTIP_HELP_SEARCH = "$('#helper-search').tooltip()";
    private static final Integer NB_ANNONCE_PAR_PAGE = 20;

    public RechercheAnnonce() {
        super("", "", "Recherche d'annonce", true, "img/bg_title1.jpg");
        initComposants();
        initVosCriteres();
        initResultat();
    }

    public RechercheAnnonce(PageParameters params) {
        this();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(CssHeaderItem.forUrl("//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"));

        //FuelUX Librairie
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));
        //Datepicker, on ne le mets pas dans le composant car probleme de conflit avec fuelux
        response.render(JavaScriptHeaderItem.forUrl("js/bootstrap-datepicker.js"));
        response.render(CssContentHeaderItem.forUrl("css/datepicker.css"));

        response.render(OnDomReadyHeaderItem.forScript(REFRESH_TOOLTIP_HELP_SEARCH));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", authentication.getEntrepriseUserInfo().getIsVerifier());
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        add(profil, contactezNous, commentaire);
    }

    private void initVosCriteres() {
        final SearchAnnonceDTO searchAnnonceDTO = new SearchAnnonceDTO();

        final CheckBox electricite = new CheckBox("electricite", Model.of(Boolean.FALSE));
        final CheckBox plomberie = new CheckBox("plomberie", Model.of(Boolean.FALSE));
        final CheckBox espaceVert = new CheckBox("espaceVert", Model.of(Boolean.FALSE));
        final CheckBox maconnerie = new CheckBox("maconnerie", Model.of(Boolean.FALSE));

        castorDatePicker = new CastorDatePicker("aPartirdu", "rechercheDate", true);
        castorDatePicker.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));

        TextField<Integer> departement = new TextField<>("departement");

        AjaxLink<Void> rechercher = new AjaxLink<Void>("rechercher") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                annonceDTOList.clear();
                searchAnnonceDTO.clear();

                if (electricite.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieElectricite());
                } else if (plomberie.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategoriePlomberie());
                } else if (espaceVert.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieEspaceVert());
                } else if (maconnerie.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieDecorationMaconnerie());
                } else if (!electricite.getModelObject() && !plomberie.getModelObject() && !espaceVert.getModelObject() && !maconnerie.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().addAll(CategorieLoader.getAllCategories());
                }

                searchAnnonceDTO.setLoginDemandeur(authentication.getCurrentUserInfo().getLogin());

                searchAnnonceDTO.setRangeDebut(annonceDTOList.size());
                searchAnnonceDTO.setRangeFin(annonceDTOList.size() + NB_ANNONCE_PAR_PAGE);

                annonceDTOList.addAll(annonceServiceREST.searchAnnonce(searchAnnonceDTO));
                target.add(resultatContainer);
            }
        };

        Form searchForm = new Form<>("searchForm", new CompoundPropertyModel<>(searchAnnonceDTO));
        searchForm.add(electricite, plomberie, espaceVert, maconnerie, castorDatePicker, departement, rechercher);

        add(searchForm);
    }

    private void initResultat() {
        resultatContainer = new WebMarkupContainer("resultatContainer");
        resultatContainer.setOutputMarkupId(true);

        ListView<AnnonceDTO> resultat = new ListView<AnnonceDTO>("resultat", annonceDTOList) {
            @Override
            protected void populateItem(ListItem<AnnonceDTO> item) {
                final AnnonceDTO annonce = item.getModelObject();
                SimpleDateFormat dateCreationFormat = new SimpleDateFormat("dd/MM/yyyy");

                StringBuilder classCssIcon = new StringBuilder("iconsMesDevis");
                classCssIcon.append(" ").append(CategorieLoader.getIconForCategorie(annonce.getCategorieMetier()));

                WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
                iconCategorie.add(new AttributeModifier("class", classCssIcon.toString()));

                Label categorie = new Label("categorie", CategorieLoader.getCategorieByCode(annonce
                        .getCategorieMetier()));
                Label delaiIntervention = new Label("delaiIntervention", annonce.getDelaiIntervention().getText());
                Label dateCreation = new Label("dateCreation", dateCreationFormat.format(annonce.getDateCreation()));
                Label typeTravaux = new Label("typeTravaux", annonce.getTypeTravaux().getText());

                LinkLabel voirAnnonce = new LinkLabel("voirAnnonce", new Model<>("Voir annonce")) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add(ParamsConstant.ID_ANNONCE_PARAM, annonce.getHashID());
                        this.setResponsePage(Annonce.class, params);
                    }
                };
                voirAnnonce.setOutputMarkupId(true);

                item.add(iconCategorie, categorie, delaiIntervention, dateCreation, typeTravaux, voirAnnonce);
            }
        };

        resultatContainer.add(resultat);
        add(resultatContainer);
    }

}