package fr.castor.web.client.extend.nouveau.communs;

import fr.castor.dto.LocalisationDTO;
import fr.castor.dto.constant.ValidatorConstant;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.app.enums.PropertiesFileWeb;
import fr.castor.web.app.utils.codepostal.CSVCodePostalReader;
import fr.castor.web.client.behaviour.ErrorHighlightBehavior;
import fr.castor.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.castor.web.client.event.FeedBackPanelEvent;
import fr.castor.web.client.extend.nouveau.devis.event.LocalisationEvent;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.validation.validator.PatternValidator;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Permet à l'utilisateur de saisir son code postal et d'en déduire la ville et le département.
 *
 * @author Casaucau Cyril
 */
public class Etape1 extends Panel {

    private TextField<String> codePostal;
    private AjaxSubmitLink valideCodePostal;
    private StatelessForm formCodePostal;
    private Label subtitle;

    @Inject
    private CSVCodePostalReader csvCodePostalReader;

    public Etape1(String id, String subtitleValue) {
        super(id);

        subtitle = new Label("subtitle", subtitleValue);

        codePostal = new TextField<>("codePostal", new Model<>());
        codePostal.setRequired(true);
        codePostal.setMarkupId("codePostal");
        codePostal.add(new PatternValidator(ValidatorConstant.CODE_POSTAL_REGEX));
        codePostal.add(new ErrorHighlightBehavior());
        codePostal.add(new RequiredBorderBehaviour());

        formCodePostal = new StatelessForm("formCodePostal");
        formCodePostal.setDefaultButton(valideCodePostal);

        StoryTelling storyTelling = new StoryTelling("storyTelling", "C'est le moment de saisir votre code postal", 120, 120);

        valideCodePostal = new AjaxSubmitLink("valideCodePostal") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                Map<String, List<LocalisationDTO>> localisationDTOMap = csvCodePostalReader.getLocalisationDTOs();

                if(localisationDTOMap.containsKey(codePostal.getDefaultModelObject())){
                    //Chargement des départements que l'on autorise
                    Properties departementProperties = PropertiesFileWeb.DEPARTEMENT_ALLOWED.getProperties();
                    String[] departementsAllowed = departementProperties.getProperty("departement.allowed").split(",");
                    boolean departementIsAllowed = false;

                    //Récuperation des informations de localisation
                    List<LocalisationDTO> localisationsDTO = localisationDTOMap.get(codePostal.getDefaultModelObject());

                    //On compare le departement de l'utilisateur avec ceux du fichier
                    for(String departementAllowed : departementsAllowed){
                        if(!localisationsDTO.isEmpty() && localisationsDTO.get(0).getDepartement().equals(departementAllowed)){
                            departementIsAllowed = true;
                        }
                    }

                    if(departementIsAllowed){
                        LocalisationEvent localisationEvent = new LocalisationEvent(target, localisationsDTO);
                        target.getPage().send(target.getPage(), Broadcast.BREADTH, localisationEvent);
                    }else{
                        target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Pour le moment, notre service n'est pas disponible dans votre département, veuillez nous contacter pour des amples informations.", FeedbackMessageLevel.ERROR));
                    }
                }else{
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Nous n'avons pas pu trouver votre code postal, veuillez réessayer ultérieurement ou contactez nous", FeedbackMessageLevel.ERROR));
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                MasterPage.triggerEventFeedBackPanel(target, "", FeedbackMessageLevel.ERROR);
            }
        };

        valideCodePostal.setMarkupId("valideCodePostal");

        formCodePostal.add(codePostal, valideCodePostal);
        add(subtitle, formCodePostal, storyTelling);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("css/bubble.css"));
    }
}