package fr.batimen.web.client.component;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.FrontEndException;
import fr.batimen.web.client.master.MasterPage;

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

        wizard = new WebMarkupContainer("wizard") {

            private static final long serialVersionUID = -8397930891418180306L;

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.Component#renderHead(org.apache.wicket.markup
             * .head.IHeaderResponse)
             */
            @Override
            public void renderHead(IHeaderResponse response) {
                StringBuilder jsAvoidStepClick = new StringBuilder("<script type=\"text/javascript\">");
                jsAvoidStepClick.append("$('.wizard').on('stepclick', function (e, data) {");
                jsAvoidStepClick.append(" return e.preventDefault();");
                jsAvoidStepClick.append(" });");
                jsAvoidStepClick.append(" </script>");

                response.render(StringHeaderItem.forString(jsAvoidStepClick.toString()));
                super.renderHead(response);
            }

        };

        wizard.add(etape1Li);
        wizard.add(etape2Li);
        wizard.add(etape3Li);
        wizard.add(etape4Li);
        wizard.add(etape5Li);

        this.add(wizard);
    }

    public NavigationWizard(String id, List<String> etapes) throws FrontEndException {
        this(id);

        for (int i = 0; i < etapes.size(); i++) {
            switch (i) {
            case 0:
                etape1.setDefaultModelObject(etapes.get(i));
                break;
            case 1:
                etape2.setDefaultModelObject(etapes.get(i));
                break;
            case 2:
                etape3.setDefaultModelObject(etapes.get(i));
                break;
            case 3:
                etape4.setDefaultModelObject(etapes.get(i));
                break;
            case 4:
                etape5.setDefaultModelObject(etapes.get(i));
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
