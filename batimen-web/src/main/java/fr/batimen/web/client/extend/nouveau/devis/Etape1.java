package fr.batimen.web.client.extend.nouveau.devis;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Permet à l'utilisateur de saisir son code postal et d'en déduire la ville et le département.
 */
public class Etape1 extends Panel {

    private TextField<String> codePostalAutoComplete;
    private AjaxSubmitLink valideCodePostal;
    private StatelessForm formCodePostal;

    private static String initTypeAhead = "$('#codePostalTypeAhead').typeahead({" +
            "source: ['Audi', 'BMW', 'Bugatti', 'Ferrari', 'Ford', 'Lamborghini', 'Mercedes Benz', 'Porsche', 'Rolls-Royce', 'Volkswagen']" +
            "" +
            "});";

    public Etape1(String id) {
        super(id);
        codePostalAutoComplete = new TextField<>("codePostalTypeAhead");
        codePostalAutoComplete.setMarkupId("codePostalTypeAhead");

        formCodePostal = new StatelessForm("formCodePostal");

        valideCodePostal = new AjaxSubmitLink("valideCodePostal") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }
        };
        formCodePostal.add(codePostalAutoComplete, valideCodePostal);
        add(formCodePostal);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(initTypeAhead));
    }
}