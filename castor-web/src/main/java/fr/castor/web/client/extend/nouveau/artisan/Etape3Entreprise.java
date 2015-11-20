package fr.castor.web.client.extend.nouveau.artisan;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.dto.CaptchaDTO;
import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.LocalisationDTO;
import fr.castor.dto.aggregate.CreationPartenaireDTO;
import fr.castor.dto.constant.Categorie;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.enums.Etape;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.app.enums.PropertiesFileWeb;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.app.utils.codepostal.CSVCodePostalReader;
import fr.castor.web.client.component.ReCaptcha;
import fr.castor.web.client.event.FeedBackPanelEvent;
import fr.castor.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.castor.web.client.extend.nouveau.communs.JSCommun;
import fr.castor.web.client.extend.nouveau.devis.NouveauUtils;
import fr.castor.web.client.master.MasterPage;
import fr.castor.ws.client.service.ArtisanServiceREST;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.List;

/**
 * Etape 3 de l'inscription d'un nouvel artisan : Informations sur l'entreprise
 *
 * @author Casaucau Cyril
 */
public class Etape3Entreprise extends Panel {

    private static final long serialVersionUID = -4959756477938900372L;

    private Etape3EntrepriseForm etape3EntrepriseForm;
    private Form<CreationPartenaireDTO> etape3FormGeneral;
    private List<CategorieMetierDTO> categoriesSelectionnees;
    private WebMarkupContainer containerActivite;
    private CreationPartenaireDTO nouveauPartenaire;
    private String INIT_VILLE_TYPE_AHEAD;
    private StringBuilder INIT_MULTI_CATEGORIE_CHECKBOX;
    private StringBuilder INIT_TOOLTIP_CATEGORIE;
    private String classCSSTooltip;
    private ReCaptcha reCaptcha;
    private Model<Boolean> electriciteModel;
    private Model<Boolean> plomberieModel;
    private Model<Boolean> maconnerieModel;
    private Model<Boolean> espaceVertModel;
    private Model<Boolean> menuiserieModel;
    private CheckBox electricite;
    private CheckBox plomberie;
    private CheckBox espaceVert;
    private CheckBox maconnerie;
    private CheckBox menuiserie;
    private CheckBox multiCategories;

    @Inject
    private Authentication authentication;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    @Inject
    private CSVCodePostalReader csvCodePostalReader;

    private RolesUtils rolesUtils;

    private Boolean isInModification;

    public Etape3Entreprise(String id, IModel<?> model) {
        super(id, model);
    }

    public Etape3Entreprise(String id, IModel<?> model, final CreationPartenaireDTO nouveauPartenaire, final boolean isInModification) {
        this(id, model);
        this.nouveauPartenaire = nouveauPartenaire;
        this.isInModification = isInModification;

        Model<String> titreModificationEntrepriseModel = new Model<>();

        rolesUtils = new RolesUtils();

        if (rolesUtils.checkRoles(TypeCompte.ARTISAN)) {
            if (isInModification) {
                titreModificationEntrepriseModel.setObject("Modifier mon entreprise");
                List<LocalisationDTO> localisationDTOs = csvCodePostalReader.getLocalisationDTOs().get(nouveauPartenaire.getAdresse().getCodePostal());
                for (LocalisationDTO localisationDTO : localisationDTOs) {
                    nouveauPartenaire.getVillesPossbles().add(localisationDTO.getCommune());
                }
            } else {
                titreModificationEntrepriseModel.setObject("Renseignez les informations de l'entreprise");
            }
        }

        Label titreModificationEntreprise = new Label("titreModificationEntreprise", titreModificationEntrepriseModel);

        etape3EntrepriseForm = new Etape3EntrepriseForm("etape3EntrepriseForm",
                new CompoundPropertyModel<>(nouveauPartenaire), isInModification);

        initModelCheckBox();
        initCheckBoxCategorie();

        categoriesSelectionnees = nouveauPartenaire.getEntreprise().getCategoriesMetier();

        etape3FormGeneral = new Form<CreationPartenaireDTO>("etape3FormGeneral", new CompoundPropertyModel<>(nouveauPartenaire)) {
            @Override
            public boolean isRootForm() {
                return true;
            }

            @Override
            public boolean wantSubmitOnNestedFormSubmit() {
                return true;
            }
        };

        initEtapePrecedenteNouveauArtisan3();

        AjaxSubmitLink validateEtape3Partenaire = new AjaxSubmitLink("validateEtape3Partenaire") {

            private static final long serialVersionUID = 4945314422581299777L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(etape3EntrepriseForm.getFieldContainer());
                refreshJS(target);
                JSCommun.scrollToTop(target);
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String siretTrimed = StringUtils.deleteWhitespace(nouveauPartenaire.getEntreprise().getSiret());
                nouveauPartenaire.getEntreprise().setSiret(siretTrimed);

                recordCategories();

                if (isInModification) {
                    nouveauPartenaire.getEntreprise().setAdresseEntreprise(nouveauPartenaire.getAdresse());
                    Integer codeRetour = artisanServiceREST.saveEntrepriseInformation(nouveauPartenaire.getEntreprise());
                    if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                        authentication.setEntrepriseUserInfo(nouveauPartenaire.getEntreprise());
                        MasterPage.triggerEventFeedBackPanel(target, "Profil mis à jour avec succés", FeedbackMessageLevel.SUCCESS);
                    } else {
                        MasterPage.triggerEventFeedBackPanel(target, "Problème durant l'appel au service de mise à jour, veuillez réessayer ultérieurement ", FeedbackMessageLevel.ERROR);
                    }

                    JSCommun.scrollToTop(target);
                } else {
                    CaptchaDTO captchaDTO = reCaptcha.verifyCaptcha();

                    if (!Boolean.valueOf(captchaDTO.getSuccess()) && Boolean.valueOf(PropertiesFileWeb.APP.getProperties().getProperty("app.activate.captcha"))) {
                        MasterPage.triggerEventFeedBackPanel(target, "Veuillez cocher le recaptcha avant de pouvoir continuer", FeedbackMessageLevel.ERROR);
                    } else {
                        nouveauPartenaire.setNumeroEtape(4);
                        ChangementEtapeEventArtisan changementEtapeEvent = new ChangementEtapeEventArtisan(target,
                                nouveauPartenaire);
                        refreshJS(target);
                        this.send(target.getPage(), Broadcast.EXACT, changementEtapeEvent);
                    }
                }
            }
        };

        validateEtape3Partenaire.setMarkupId("validateEtape3Partenaire");

        WebMarkupContainer terminerInscriptionPartenaire = new WebMarkupContainer("terminerInscriptionPartenaire") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (isInModification) {
                    if (tag.getAttribute("class") != null) {
                        StringBuilder classCSS = new StringBuilder(tag.getAttribute("class"));
                        classCSS.append(" offset3");
                        tag.remove("class");
                        tag.put("class", classCSS.toString());
                    }
                } else {
                    if (tag.getAttribute("class") != null) {
                        StringBuilder classCSS = new StringBuilder(tag.getAttribute("class"));
                        classCSS.append(" nouveauPartenaire-btn");
                        tag.remove("class");
                        tag.put("class", classCSS.toString());
                    } else {
                        tag.put("class", "nouveauPartenaire-btn");
                    }

                }
            }
        };

        Label validateEtape3Name = new Label("validateEtape3Name", Model.of(""));
        validateEtape3Partenaire.add(validateEtape3Name);
        terminerInscriptionPartenaire.add(validateEtape3Partenaire);

        if (!isInModification) {
            terminerInscriptionPartenaire.setOutputMarkupId(true);
            terminerInscriptionPartenaire.setMarkupId("terminerInscriptionPartenaire-nouveau");
            validateEtape3Name.setDefaultModelObject("Terminer");
        } else {
            validateEtape3Name.setDefaultModelObject("Sauvegarder");
        }

        initRecaptcha();

        etape3FormGeneral.add(containerActivite, etape3EntrepriseForm, terminerInscriptionPartenaire);
        add(titreModificationEntreprise, etape3FormGeneral);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));

        if (isInModification) {
            classCSSTooltip = "checkbox-tooltip-modif";
        } else {
            classCSSTooltip = "checkbox-tooltip";
        }

        INIT_TOOLTIP_CATEGORIE = new StringBuilder();
        INIT_TOOLTIP_CATEGORIE.append("$('.").append(classCSSTooltip).append("').tooltip()");


        INIT_MULTI_CATEGORIE_CHECKBOX = new StringBuilder();
        INIT_MULTI_CATEGORIE_CHECKBOX.append("$(function () {");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("$('#multiCategorie').on('click', function () {");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("if ($(this).prop('checked')) {");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("$('.sr-only').checkbox('check');");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("} else {");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("$('.sr-only').checkbox('uncheck');");
        INIT_MULTI_CATEGORIE_CHECKBOX.append(" }");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("});");
        INIT_MULTI_CATEGORIE_CHECKBOX.append("});");

        INIT_VILLE_TYPE_AHEAD = JSCommun.buildSourceTypeAhead(nouveauPartenaire.getVillesPossbles(), "#villeField");

        response.render(OnDomReadyHeaderItem.forScript(INIT_TOOLTIP_CATEGORIE.toString()));
        response.render(OnDomReadyHeaderItem.forScript(INIT_MULTI_CATEGORIE_CHECKBOX.toString()));
        response.render(OnDomReadyHeaderItem.forScript(INIT_VILLE_TYPE_AHEAD));
    }

    private void getCSSClassForCategorie(ComponentTag tag, boolean isInModification) {
        if (isInModification) {
            String classCss = tag.getAttribute("class");

            if (classCss.contains("checkbox-tooltip")) {
                classCss = StringUtils.replace(classCss, "checkbox-tooltip", "checkbox-tooltip-modif");
            }
            if (classCss.contains("non-icon8")) {
                classCss = StringUtils.replace(classCss, "non-icon8", "non-icon8-modif");
            }

            tag.put("class", classCss);
        }
    }

    private void refreshJS(AjaxRequestTarget target) {
        target.appendJavaScript(INIT_VILLE_TYPE_AHEAD);
        target.appendJavaScript(INIT_MULTI_CATEGORIE_CHECKBOX);
        target.appendJavaScript("$('.checkbox').checkbox()");
        target.appendJavaScript(INIT_TOOLTIP_CATEGORIE);
    }

    @Override
    public boolean isVisible() {
        return rolesUtils.checkRoles(TypeCompte.ARTISAN);
    }

    private void initModelCheckBox() {
        electriciteModel = new Model<>();
        plomberieModel = new Model<>();
        maconnerieModel = new Model<>();
        espaceVertModel = new Model<>();
        menuiserieModel = new Model<>();

        for (CategorieMetierDTO categorieMetierDTO : nouveauPartenaire.getEntreprise().getCategoriesMetier()) {
            switch (categorieMetierDTO.getCategorieMetier()) {
                case Categorie.ELECTRICITE_CODE:
                    electriciteModel.setObject(Boolean.TRUE);
                    break;
                case Categorie.PLOMBERIE_CODE:
                    plomberieModel.setObject(Boolean.TRUE);
                    break;
                case Categorie.DECORATION_MACONNERIE_CODE:
                    maconnerieModel.setObject(Boolean.TRUE);
                    break;
                case Categorie.ESPACE_VERT_CODE:
                    espaceVertModel.setObject(Boolean.TRUE);
                    break;
                case Categorie.MENUISERIE_CODE:
                    menuiserieModel.setObject(Boolean.TRUE);
                    break;
                default:
                    throw new NullPointerException();
            }
        }
    }

    private void initCheckBoxCategorie() {
        containerActivite = new WebMarkupContainer("containerActivite");
        containerActivite.setOutputMarkupId(true);
        containerActivite.setMarkupId("containerActivite");

        electricite = new CheckBox("electricite", electriciteModel);
        electricite.setMarkupId("electricite");
        plomberie = new CheckBox("plomberie", plomberieModel);
        plomberie.setMarkupId("plomberie");
        espaceVert = new CheckBox("espaceVert", espaceVertModel);
        espaceVert.setMarkupId("espaceVert");
        maconnerie = new CheckBox("decorationMaconnerie", maconnerieModel);
        maconnerie.setMarkupId("decorationMaconnerie");
        menuiserie = new CheckBox("menuiserie", menuiserieModel);
        menuiserie.setMarkupId("menuiserie");
        multiCategories = new CheckBox("multiCategories", Model.of(Boolean.FALSE));
        multiCategories.setMarkupId("multiCategorie");

        WebMarkupContainer electriciteContainer = new WebMarkupContainer("eletriciteContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };
        WebMarkupContainer plomberieContainer = new WebMarkupContainer("plomberieContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };
        WebMarkupContainer espaceVertContainer = new WebMarkupContainer("espaceVertContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };
        WebMarkupContainer maconnerieContainer = new WebMarkupContainer("maconnerieContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };

        WebMarkupContainer menuiserieContainer = new WebMarkupContainer("menuiserieContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };

        WebMarkupContainer multicategorieContainer = new WebMarkupContainer("multicategorieContainer") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                getCSSClassForCategorie(tag, isInModification);
            }
        };

        electriciteContainer.add(electricite);
        plomberieContainer.add(plomberie);
        espaceVertContainer.add(espaceVert);
        maconnerieContainer.add(maconnerie);
        menuiserieContainer.add(menuiserie);
        multicategorieContainer.add(multiCategories);
        containerActivite.add(electriciteContainer, plomberieContainer, espaceVertContainer, maconnerieContainer, menuiserieContainer, multicategorieContainer);
    }

    private void initEtapePrecedenteNouveauArtisan3() {
        final AjaxLink<Void> etapePrecedenteNouveauArtisan3 = new AjaxLink<Void>("etapePrecedenteNouveauArtisan3") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_3.ordinal() + 1);
            }

            @Override
            public boolean isVisible() {
                return !isInModification;
            }
        };

        etapePrecedenteNouveauArtisan3.setOutputMarkupId(true);
        etapePrecedenteNouveauArtisan3.setMarkupId("etapePrecedenteNouveauArtisan3");

        WebMarkupContainer containerEtapePrecedenteNouveauArtisan3 = new WebMarkupContainer("containerEtapePrecedenteNouveauArtisan3") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (isInModification) {
                    tag.remove("class");
                    tag.put("class", "span4");
                }
            }
        };

        if (!isInModification) {
            containerEtapePrecedenteNouveauArtisan3.setOutputMarkupId(true);
            containerEtapePrecedenteNouveauArtisan3.setMarkupId("containerEtapePrecedenteNouveauArtisan3-nouveau-partenaire");
        }

        containerEtapePrecedenteNouveauArtisan3.add(etapePrecedenteNouveauArtisan3);
        etape3FormGeneral.add(containerEtapePrecedenteNouveauArtisan3);
    }

    private void initRecaptcha() {
        reCaptcha = new ReCaptcha("recaptchaInscription") {
            @Override
            public boolean isVisible() {
                return !isInModification;
            }
        };

        etape3EntrepriseForm.add(reCaptcha);
    }

    private void recordCategories() {
        checkAndRecordCategorie(electricite.getConvertedInput(), Categorie.ELECTRICITE_CODE, Categorie.getElectricite());
        checkAndRecordCategorie(plomberie.getConvertedInput(), Categorie.PLOMBERIE_CODE, Categorie.getPlomberie());
        checkAndRecordCategorie(espaceVert.getConvertedInput(), Categorie.ESPACE_VERT_CODE, Categorie.getEspaceVert());
        checkAndRecordCategorie(maconnerie.getConvertedInput(), Categorie.DECORATION_MACONNERIE_CODE, Categorie.getMaconnerie());
        checkAndRecordCategorie(menuiserie.getConvertedInput(), Categorie.MENUISERIE_CODE, Categorie.getMenuiserie());
    }

    /**
     * Méthode générique de vérification si la checkbox est cochée,
     * si c'est le cas et qu'elle n'est pas deja presente dans l'entreprise on la rajoute sinon on la supprime
     *
     * @param checked            si la checkbox est cochée
     * @param categorieCode      Le code catégorie a laquelle appartient la checkbox
     * @param categorieMetierDTO La catégorie a laquelle appartient la checkbox
     */
    private void checkAndRecordCategorie(Boolean checked, Short categorieCode, CategorieMetierDTO categorieMetierDTO) {
        if (checked) {
            //Si elle existe, on en fait rien
            Boolean possedeCategorie = categoriesSelectionnees.stream().anyMatch(categorieMetierDTOToTest -> categorieMetierDTOToTest.getCategorieMetier().equals(categorieCode));
            if (!possedeCategorie) {
                categoriesSelectionnees.add(categorieMetierDTO);
            }
            //Si elle n'est pas coché mais qu'elle est présente
        } else {
            categoriesSelectionnees.removeIf(categorieMetierDTOToTest -> categorieMetierDTOToTest.getCategorieMetier().equals(categorieCode));
        }
    }
}