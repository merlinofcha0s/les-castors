package fr.batimen.web.client.extend.nouveau.communs;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.utils.codepostal.CSVCodePostalReader;
import fr.batimen.dto.LocalisationDTO;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.LocalisationEvent;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.enums.PropertiesFileWeb;
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
                    //Chargement des départements que l'on autorise
                    Properties departementProperties = PropertiesFileWeb.DEPARTEMENT_ALLOWED.getProperties();
                    String[] departementsAllowed = departementProperties.getProperty("departement.allowed").split(",");
                    boolean departementIsAllowed = false;

                    //Récuperation des informations de localisation
                    List<LocalisationDTO> localisationsDTO = localisationDTOMap.get(codePostal.getDefaultModelObject());

                    //On compare le departement de l'utilisateur avec ceux du fichier
                    for(String departementAllowed : departementsAllowed){
                        if(localisationsDTO.size() != 0 && localisationsDTO.get(0).getDepartement().equals(departementAllowed)){
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
        add(formCodePostal);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}