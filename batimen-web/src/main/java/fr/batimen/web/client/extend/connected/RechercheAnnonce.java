package fr.batimen.web.client.extend.connected;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.aggregate.SearchAnnonceDTOIn;
import fr.batimen.dto.aggregate.SearchAnnonceDTOOut;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.*;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
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
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Page de recherche des annonce, destinée au Artisan
 *
 * @author Casaucau Cyril
 */
public class RechercheAnnonce extends MasterPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(RechercheAnnonce.class);

    private static final String REFRESH_TOOLTIP_HELP_SEARCH = "$('#helper-search').tooltip()";
    private static final Integer NB_ANNONCE_PAR_PAGE = 2;
    private final SearchAnnonceDTOIn searchAnnonceDTO = new SearchAnnonceDTOIn();
    @Inject
    private Authentication authentication;
    @Inject
    private AnnonceServiceREST annonceServiceREST;
    private CastorDatePicker castorDatePicker;
    private WebMarkupContainer resultatContainer;
    private WebMarkupContainer containerPlusAnnonce;
    private List<AnnonceDTO> annonceDTOList = new ArrayList<>();
    private long nbTotaleAnnonceRecherche;
    private Label infoNbAnnonce;

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
        final CheckBox electricite = new CheckBox("electricite", Model.of(Boolean.FALSE));
        final CheckBox plomberie = new CheckBox("plomberie", Model.of(Boolean.FALSE));
        final CheckBox espaceVert = new CheckBox("espaceVert", Model.of(Boolean.FALSE));
        final CheckBox maconnerie = new CheckBox("maconnerie", Model.of(Boolean.FALSE));

        castorDatePicker = new CastorDatePicker("aPartirdu", "rechercheDate", true);
        castorDatePicker.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));

        final TextField<Integer> departement = new TextField<>("departement");
        departement.add(RangeValidator.minimum(ValidatorConstant.DEPARTEMENT_MIN));
        departement.add(RangeValidator.maximum(ValidatorConstant.DEPARTEMENT_MAX));

        AjaxSubmitLink rechercher = new AjaxSubmitLink("rechercher") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                annonceDTOList.clear();
                searchAnnonceDTO.getCategoriesMetierDTO().clear();

                if (electricite.getConvertedInput()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieElectricite());
                }
                if (plomberie.getConvertedInput()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategoriePlomberie());
                }
                if (espaceVert.getConvertedInput()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieEspaceVert());
                }
                if (maconnerie.getConvertedInput()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().add(CategorieLoader.getCategorieDecorationMaconnerie());
                }
                if (!electricite.getConvertedInput() && !plomberie.getModelObject() && !espaceVert.getModelObject() && !maconnerie.getModelObject()) {
                    searchAnnonceDTO.getCategoriesMetierDTO().addAll(CategorieLoader.getAllCategories());
                }

                searchAnnonceDTO.setLoginDemandeur(authentication.getCurrentUserInfo().getLogin());

                if (searchAnnonceDTO.getDepartement() == null) {
                    searchAnnonceDTO.setDepartement(authentication.getEntrepriseUserInfo().getAdresseEntreprise().getDepartement());
                }

                if (searchAnnonceDTO.getaPartirdu() == null) {
                    searchAnnonceDTO.setaPartirdu(new Date());
                }

                searchAnnonceDTO.setRangeDebut(annonceDTOList.size());
                searchAnnonceDTO.setRangeFin(NB_ANNONCE_PAR_PAGE);

                SearchAnnonceDTOOut searchAnnonceDTOOut = annonceServiceREST.searchAnnonce(searchAnnonceDTO);

                nbTotaleAnnonceRecherche = searchAnnonceDTOOut.getNbTotalResultat();

                annonceDTOList.addAll(searchAnnonceDTOOut.getAnnonceDTOList());

                updateModelInfoNbAnnonce();

                target.add(resultatContainer, containerPlusAnnonce);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedBackPanelGeneral);
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
                //classCssIcon.append(" ").append(CategorieLoader.getIconForCategorie(annonce.getCategorieMetier()));

                //WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
                //iconCategorie.add(new AttributeModifier("class", classCssIcon.toString()));

                /*Label categorie = new Label("categorie", CategorieLoader.getCategorieByCode(annonce
                        .getCategorieMetier()));*/
                Label delaiIntervention = new Label("delaiIntervention", annonce.getDelaiIntervention().getText());
                Label dateCreation = new Label("dateCreation", dateCreationFormat.format(annonce.getDateCreation()));
                Label typeTravaux = new Label("typeTravaux", annonce.getTypeTravaux().getText());

                LinkLabel voirAnnonce = new LinkLabel("voirAnnonce", new Model<>("Accéder à l'annonce")) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add(ParamsConstant.ID_ANNONCE_PARAM, annonce.getHashID());
                        this.setResponsePage(Annonce.class, params);
                    }
                };

                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        PageParameters params = new PageParameters();
                        params.add(ParamsConstant.ID_ANNONCE_PARAM, annonce.getHashID());
                        setResponsePage(Annonce.class, params);
                    }
                });
                voirAnnonce.setOutputMarkupId(true);

                item.add(/*iconCategorie, categorie,*/ delaiIntervention, dateCreation, typeTravaux, voirAnnonce);
            }
        };

        AjaxLink<Void> afficherPlus = new AjaxLink<Void>("afficherPlus") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                searchAnnonceDTO.setRangeDebut(annonceDTOList.size());
                searchAnnonceDTO.setRangeFin(NB_ANNONCE_PAR_PAGE);

                SearchAnnonceDTOOut searchAnnonceDTOOut = annonceServiceREST.searchAnnonce(searchAnnonceDTO);
                annonceDTOList.addAll(searchAnnonceDTOOut.getAnnonceDTOList());

                updateModelInfoNbAnnonce();
                target.add(resultatContainer, containerPlusAnnonce);
            }

            @Override
            public boolean isVisible() {
                return annonceDTOList.size() != nbTotaleAnnonceRecherche || (!annonceDTOList.isEmpty() && annonceDTOList.size() != nbTotaleAnnonceRecherche);
            }
        };


        infoNbAnnonce = new Label("infoNbAnnonce", new Model<>()) {
            @Override
            public boolean isVisible() {
                return !annonceDTOList.isEmpty();
            }
        };

        updateModelInfoNbAnnonce();
        infoNbAnnonce.setOutputMarkupId(true);
        infoNbAnnonce.setMarkupId("infoNbAnnonce");

        containerPlusAnnonce = new WebMarkupContainer("containerPlusAnnonce");
        containerPlusAnnonce.setOutputMarkupId(true);
        containerPlusAnnonce.setMarkupId("containerPlusAnnonce");

        containerPlusAnnonce.add(afficherPlus, infoNbAnnonce);

        resultatContainer.add(resultat);
        add(resultatContainer, containerPlusAnnonce);
    }

    private void updateModelInfoNbAnnonce() {
        StringBuilder infoNbAnnonceValeur = new StringBuilder();
        infoNbAnnonceValeur.append(annonceDTOList.size()).append(" annonces affichées sur ").append(nbTotaleAnnonceRecherche);
        infoNbAnnonce.setDefaultModelObject(infoNbAnnonceValeur.toString());
    }
}