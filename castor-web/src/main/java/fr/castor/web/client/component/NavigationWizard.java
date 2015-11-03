package fr.castor.web.client.component;

import java.util.List;

import fr.castor.web.client.behaviour.AjaxCastorWizardBehaviour;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.castor.core.exception.FrontEndException;

/**
 * Affiche les differentes etapes à l'utilisateur qu'il va devoir faire
 * 
 * @author Casaucau Cyril
 * 
 */
public class NavigationWizard extends Panel {

    private static final long serialVersionUID = -4561122813846106343L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterPage.class);

    private final WebMarkupContainer wizard;

    private final Label etape1;
    private final Label etape2;
    private final Label etape3;
    private final Label etape4;
    private final Label etape5;

    private final WebMarkupContainer badgeEtape1;
    private final WebMarkupContainer badgeEtape2;
    private final WebMarkupContainer badgeEtape3;
    private final WebMarkupContainer badgeEtape4;
    private final WebMarkupContainer badgeEtape5;

    private final WebMarkupContainer etape1Li;
    private final WebMarkupContainer etape2Li;
    private final WebMarkupContainer etape3Li;
    private final WebMarkupContainer etape4Li;
    private final WebMarkupContainer etape5Li;

    private final AjaxCastorWizardBehaviour ajaxCastorWizardBehaviour;
    private String callbackURL;
    private StringBuilder jsCastorWizard;
    private final Model<String> jsCastorWizardModel;

    public NavigationWizard(String id) {
        super(id);

        etape1 = new Label("etape1", "");
        etape2 = new Label("etape2", "");
        etape3 = new Label("etape3", "");
        etape4 = new Label("etape4", "");
        etape5 = new Label("etape5", "");

        badgeEtape1 = new WebMarkupContainer("badgeEtape1");
        badgeEtape2 = new WebMarkupContainer("badgeEtape2");
        badgeEtape3 = new WebMarkupContainer("badgeEtape3");
        badgeEtape4 = new WebMarkupContainer("badgeEtape4");
        badgeEtape5 = new WebMarkupContainer("badgeEtape5");

        etape1Li = new WebMarkupContainer("etape1Li");
        etape2Li = new WebMarkupContainer("etape2Li");
        etape3Li = new WebMarkupContainer("etape3Li");
        etape4Li = new WebMarkupContainer("etape4Li");
        etape5Li = new WebMarkupContainer("etape5Li");
        etape1Li.setVisible(false);
        etape2Li.setVisible(false);
        etape3Li.setVisible(false);
        etape4Li.setVisible(false);
        etape5Li.setVisible(false);

        etape1Li.add(etape1);
        etape2Li.add(etape2);
        etape3Li.add(etape3);
        etape4Li.add(etape4);
        etape5Li.add(etape5);

        etape1Li.add(badgeEtape1);
        etape2Li.add(badgeEtape2);
        etape3Li.add(badgeEtape3);
        etape4Li.add(badgeEtape4);
        etape5Li.add(badgeEtape5);

        wizard = new WebMarkupContainer("wizard");
        wizard.setMarkupId("batimenWizard");

        ajaxCastorWizardBehaviour = new AjaxCastorWizardBehaviour();

        wizard.add(etape1Li, etape2Li, etape3Li, etape4Li, etape5Li);
        wizard.add(ajaxCastorWizardBehaviour);

        // Pour le moment, pas de javascript : il est calculé dans le
        // onBeforeRender car il faut attendre que tout le reste de la page soit
        // chargé sinon erreur sur le get call back script
        jsCastorWizard = new StringBuilder("");
        jsCastorWizardModel = new Model<String>(jsCastorWizard.toString());

        Label jsCastorWizardContainer = new Label("jsCastorWizard", jsCastorWizardModel);
        jsCastorWizardContainer.setEscapeModelStrings(false);

        this.add(wizard, jsCastorWizardContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onBeforeRender()
     */
    @Override
    protected void onBeforeRender() {
        // Création du callback qui va renvoyer le numero de l'etape lorsque
        // l'utilisateur clique sur une étape du wizard.
        callbackURL = ajaxCastorWizardBehaviour.getCallbackScript().toString();
        jsCastorWizard = new StringBuilder("$(document).ready(function(){ $('#");
        jsCastorWizard.append(wizard.getMarkupId()).append("').on('stepclick', function (e, data) { ")
                .append("var stepNumber = data.step;").append(callbackURL).append("}); });");
        jsCastorWizardModel.setObject(jsCastorWizard.toString());
        super.onBeforeRender();
    }

    public void next(AjaxRequestTarget target) {
        StringBuilder jsNextStep = new StringBuilder("$('#");
        jsNextStep.append(wizard.getMarkupId()).append("').wizard('next');");
        target.appendJavaScript(jsNextStep.toString());
    }

    public void previous(AjaxRequestTarget target) {
        StringBuilder jsPreviousStep = new StringBuilder("$('#");
        jsPreviousStep.append(wizard.getMarkupId()).append("').wizard('previous');");
        target.appendJavaScript(jsPreviousStep.toString());
    }

    public NavigationWizard(String id, List<String> etapes) throws FrontEndException {
        this(id);

        for (int i = 0; i < etapes.size(); i++) {
            switch (i) {
            case 0:
                etape1.setDefaultModelObject(etapes.get(i));
                etape1Li.setVisible(true);
                break;
            case 1:
                etape2.setDefaultModelObject(etapes.get(i));
                etape2Li.setVisible(true);
                break;
            case 2:
                etape3.setDefaultModelObject(etapes.get(i));
                etape3Li.setVisible(true);
                break;
            case 3:
                etape4.setDefaultModelObject(etapes.get(i));
                etape4Li.setVisible(true);
                break;
            case 4:
                etape5.setDefaultModelObject(etapes.get(i));
                etape5Li.setVisible(true);
                break;
            default:
                throw new FrontEndException("Trop d'étape pour le composant Navigation wizard (max 5 etapes)");
            }
        }
    }

    /**
     * Fais passer le navigation wizard à l'étape demandée
     * 
     * @param step
     *            l'etape demandée
     */
    public void setStep(int step) {
        switch (step) {
        case 1:
            loadEtape1();
            break;
        case 2:
            loadEtape2();
            break;
        case 3:
            loadEtape3();
            break;
        case 4:
            loadEtape4();
            break;
        case 5:
            loadEtape5();
            break;
        default:
            try {
                throw new FrontEndException("Numéro d'étape invalide");
            } catch (FrontEndException fee) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Le numéro de l'étape demandé n'est pas valide (pas au dessus de 5)", fee);
                }
            }
        }
    }

    private void loadEtape5() {
        etape1Li.add(new AttributeModifier("class", "complete"));
        etape2Li.add(new AttributeModifier("class", "complete"));
        etape3Li.add(new AttributeModifier("class", "complete"));
        etape4Li.add(new AttributeModifier("class", "complete"));
        etape5Li.add(new AttributeModifier("class", "active"));
        badgeEtape1.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape2.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape3.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape4.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape5.add(new AttributeModifier("class", "badge badge-info"));
    }

    private void loadEtape4() {
        etape1Li.add(new AttributeModifier("class", "complete"));
        etape2Li.add(new AttributeModifier("class", "complete"));
        etape3Li.add(new AttributeModifier("class", "complete"));
        etape4Li.add(new AttributeModifier("class", "active"));
        etape5Li.add(new AttributeModifier("class", ""));
        badgeEtape1.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape2.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape3.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape4.add(new AttributeModifier("class", "badge badge-info"));
        badgeEtape5.add(new AttributeModifier("class", "badge"));
    }

    private void loadEtape3() {
        etape1Li.add(new AttributeModifier("class", "complete"));
        etape2Li.add(new AttributeModifier("class", "complete"));
        etape3Li.add(new AttributeModifier("class", "active"));
        etape4Li.add(new AttributeModifier("class", ""));
        etape5Li.add(new AttributeModifier("class", ""));
        badgeEtape1.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape2.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape3.add(new AttributeModifier("class", "badge badge-info"));
        badgeEtape4.add(new AttributeModifier("class", "badge"));
        badgeEtape5.add(new AttributeModifier("class", "badge"));
    }

    private void loadEtape2() {
        etape1Li.add(new AttributeModifier("class", "complete"));
        etape2Li.add(new AttributeModifier("class", "active"));
        etape3Li.add(new AttributeModifier("class", ""));
        etape4Li.add(new AttributeModifier("class", ""));
        etape5Li.add(new AttributeModifier("class", ""));

        badgeEtape1.add(new AttributeModifier("class", "badge badge-success"));
        badgeEtape2.add(new AttributeModifier("class", "badge badge-info"));
        badgeEtape3.add(new AttributeModifier("class", "badge"));
        badgeEtape4.add(new AttributeModifier("class", "badge"));
        badgeEtape5.add(new AttributeModifier("class", "badge"));
    }

    private void loadEtape1() {
        etape1Li.add(new AttributeModifier("class", "active"));
        etape2Li.add(new AttributeModifier("class", ""));
        etape3Li.add(new AttributeModifier("class", ""));
        etape4Li.add(new AttributeModifier("class", ""));
        etape5Li.add(new AttributeModifier("class", ""));
        badgeEtape1.add(new AttributeModifier("class", "badge badge-info"));
        badgeEtape2.add(new AttributeModifier("class", "badge"));
        badgeEtape3.add(new AttributeModifier("class", "badge"));
        badgeEtape4.add(new AttributeModifier("class", "badge"));
        badgeEtape5.add(new AttributeModifier("class", "badge"));
    }

}