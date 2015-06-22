package fr.batimen.web.client.extend.nouveau.devis;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.utils.codepostal.CSVCodePostalReader;
import fr.batimen.web.app.utils.codepostal.LocalisationDTO;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.LocalisationEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.PatternValidator;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Permet à l'utilisateur de saisir son code postal et d'en déduire la ville et le département.
 */
public class Etape1 extends Panel {

    private TextField<String> codePostal;
    private AjaxSubmitLink valideCodePostal;
    private StatelessForm formCodePostal;

    @Inject
    private CSVCodePostalReader csvCodePostalReader;

    public Etape1(String id) {
        super(id);
        codePostal = new TextField<>("codePostal", new Model<String>());
        codePostal.setRequired(true);
        codePostal.setMarkupId("codePostal");
        codePostal.add(new PatternValidator(ValidatorConstant.CODE_POSTAL_REGEX));
        codePostal.add(new ErrorHighlightBehavior());
        codePostal.add(new RequiredBorderBehaviour());

        formCodePostal = new StatelessForm("formCodePostal");

        valideCodePostal = new AjaxSubmitLink("valideCodePostal") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                Map<String, List<LocalisationDTO>> localisationDTOMap = csvCodePostalReader.getLocalisationDTOs();

                if(localisationDTOMap.containsKey(codePostal.getDefaultModelObject())){
                    //TODO Faire passer dans le filtre avant de vraiment fire l'event
                    //TODO : penser au filtre par département (fichier properties avec les numéros de département)
                    LocalisationEvent localisationEvent = new LocalisationEvent(target, localisationDTOMap.get(codePostal.getDefaultModelObject()));
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, localisationEvent);
                }else{
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Nous n'avons pas pu trouver votre code postal, veuillez réessayer ultérieurement ou contactez nous", FeedbackMessageLevel.ERROR));
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }
        };
        formCodePostal.add(codePostal, valideCodePostal);
        add(formCodePostal);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}