package fr.batimen.web.client.extend.nouveau.artisan;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauUtils;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ArtisanServiceREST;
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
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * Etape 3 de l'inscription d'un nouvel artisan : Informations sur l'entreprise
 *
 * @author Casaucau Cyril
 */
public class Etape3Entreprise extends Panel {

    private static final long serialVersionUID = -4959756477938900372L;

    private static final String LOAD_TOOLTIP_CATEGORIE = "$('.checkbox-tooltip').tooltip()";

    private Etape3EntrepriseForm etape3EntrepriseForm;
    private Form<CreationPartenaireDTO> etape3FormGeneral;
    private List<CategorieMetierDTO> categoriesSelectionnees;
    private CheckGroup<Boolean> containerActivite;

    @Inject
    private Authentication authentication;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    public Etape3Entreprise(String id, IModel<?> model) {
        super(id, model);
    }

    public Etape3Entreprise(String id, IModel<?> model, final CreationPartenaireDTO nouveauPartenaire, final boolean isInModification) {
        this(id, model);
        Model<String> titreModificationEntrepriseModel = new Model<>();

        if (isInModification) {
            titreModificationEntrepriseModel.setObject("Modifier mon entreprise");
        } else {
            titreModificationEntrepriseModel.setObject("Renseignez les informations de l'entreprise");
        }

        Label titreModificationEntreprise = new Label("titreModificationEntreprise", titreModificationEntrepriseModel);

        containerActivite = new CheckGroup("allCategoriesCheck", Arrays.asList(false, false, false, false, false));

        final CheckBox electricite = new CheckBox("electricite", Model.of(Boolean.FALSE));
        final CheckBox plomberie = new CheckBox("plomberie", Model.of(Boolean.FALSE));
        final CheckBox espaceVert = new CheckBox("espaceVert", Model.of(Boolean.FALSE));
        final CheckBox maconnerie = new CheckBox("decorationMaconnerie", Model.of(Boolean.FALSE));

        containerActivite.add(electricite, plomberie, espaceVert, maconnerie);

        containerActivite.add(new CheckGroupSelector("multiCategories") {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean wantAutomaticUpdate() {
                return true;
            }
        });


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

        final AjaxLink<Void> etapePrecedenteNouveauArtisan3 = new AjaxLink<Void>("etapePrecedenteNouveauArtisan3") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_3.ordinal() + 1);
            }
        };

        etapePrecedenteNouveauArtisan3.setOutputMarkupId(true);
        etapePrecedenteNouveauArtisan3.setMarkupId("etapePrecedenteNouveauArtisan3");

        WebMarkupContainer containerEtapePrecedenteNouveauArtisan3 = new WebMarkupContainer("containerEtapePrecedenteNouveauArtisan3") {
            @Override
            public boolean isVisible() {
                return !isInModification;
            }
        };

        containerEtapePrecedenteNouveauArtisan3.add(etapePrecedenteNouveauArtisan3);

        AjaxSubmitLink validateEtape3Partenaire = new AjaxSubmitLink("validateEtape3Partenaire") {

            private static final long serialVersionUID = 4945314422581299777L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm());
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (isInModification) {
                    nouveauPartenaire.getEntreprise().setAdresseEntreprise(nouveauPartenaire.getAdresse());
                    Integer codeRetour = artisanServiceREST.saveEntrepriseInformation(nouveauPartenaire.getEntreprise());
                    if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                        authentication.setEntrepriseUserInfo(nouveauPartenaire.getEntreprise());
                        MasterPage.triggerEventFeedBackPanel(target, "Profil mis à jour avec succés", FeedbackMessageLevel.SUCCESS);
                    } else {
                        MasterPage.triggerEventFeedBackPanel(target, "Problème durant l'appel au service de mise à jour, veuillez réessayer ultérieurement ", FeedbackMessageLevel.ERROR);
                    }

                } else {
                    if (electricite.getConvertedInput()) {
                        categoriesSelectionnees.add(CategorieLoader.getCategorieElectricite());
                    }
                    if (plomberie.getConvertedInput()) {
                        categoriesSelectionnees.add(CategorieLoader.getCategoriePlomberie());
                    }
                    if (espaceVert.getConvertedInput()) {
                        categoriesSelectionnees.add(CategorieLoader.getCategorieEspaceVert());
                    }
                    if (maconnerie.getConvertedInput()) {
                        categoriesSelectionnees.add(CategorieLoader.getCategorieDecorationMaconnerie());
                    }

                    nouveauPartenaire.setNumeroEtape(4);
                    ChangementEtapeEventArtisan changementEtapeEvent = new ChangementEtapeEventArtisan(target,
                            nouveauPartenaire);
                    this.send(target.getPage(), Broadcast.EXACT, changementEtapeEvent);
                }
            }
        };

        validateEtape3Partenaire.setMarkupId("validateEtape3Partenaire");

        WebMarkupContainer terminerInscriptionPartenaire = new WebMarkupContainer("terminerInscriptionPartenaire") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (isInModification) {
                    StringBuilder classCSS = new StringBuilder(tag.getAttribute("class"));
                    classCSS.append(" offset3");
                    tag.remove("class");
                    tag.put("class", classCSS.toString());
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

        terminerInscriptionPartenaire.add(validateEtape3Partenaire);
        containerActivite.setOutputMarkupId(true);
        containerActivite.setMarkupId("containerActivite");

        etape3EntrepriseForm = new Etape3EntrepriseForm("etape3EntrepriseForm",
                new CompoundPropertyModel<>(nouveauPartenaire), isInModification);

        etape3FormGeneral.add(containerActivite, etape3EntrepriseForm, terminerInscriptionPartenaire, containerEtapePrecedenteNouveauArtisan3);
        add(titreModificationEntreprise, etape3FormGeneral);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));
        response.render(OnDomReadyHeaderItem.forScript(LOAD_TOOLTIP_CATEGORIE));
    }
}