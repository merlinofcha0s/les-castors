package fr.castor.web.client.modal;

import fr.castor.dto.constant.ValidatorConstant;
import fr.castor.web.app.constants.JSConstant;
import fr.castor.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.castor.web.client.component.CastorFeedbackPanel;
import fr.castor.web.client.component.ModalCastor;
import fr.castor.web.client.component.RaterStarsCastor;
import fr.castor.web.client.event.NoterArtisanEventClose;
import fr.castor.web.client.event.NoterArtisanEventOpen;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Modal permettant à un client de noter un artisan une fois que les travaux
 * sont terminés.
 * 
 * @author Casaucau Cyril
 * 
 */
public class DonnerAvisArtisanModal extends ModalCastor {

    private static final long serialVersionUID = 1L;

    private final RaterStarsCastor raterCastor;
    private final TextArea<String> textAreaCommentaire;

    private final CastorFeedbackPanel feedBackPanel;

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JSConstant.fontAwesome);
    }

    public DonnerAvisArtisanModal(String id) {
        super(id, "Avis sur votre artisan", "550");

        feedBackPanel = new CastorFeedbackPanel("feedbackPanelNotationArtisan");
        feedBackPanel.setOutputMarkupId(true);

        raterCastor = new RaterStarsCastor("raterCastor", true);
        raterCastor.setMarkupId("raterCastor");

        textAreaCommentaire = new TextArea<>("textAreaCommentaireNotation", new Model<String>());
        textAreaCommentaire.setMarkupId("textAreaCommentaireNotation");
        textAreaCommentaire.setRequired(true);
        textAreaCommentaire.add(new RequiredBorderBehaviour());
        textAreaCommentaire.add(StringValidator.lengthBetween(ValidatorConstant.NOTATION_MIN_COMMENTAIRE,
                ValidatorConstant.NOTATION_MAX_COMMENTAIRE));

        Form<Void> formNotationCastor = new Form<Void>("formNotationCastor");

        AjaxSubmitLink submitNotation = new AjaxSubmitLink("submitNotation") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onSubmit
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                NoterArtisanEventClose noterArtisanEventClose = new NoterArtisanEventClose(target,
                        raterCastor.getScore(), textAreaCommentaire.getConvertedInput());
                this.send(target.getPage(), Broadcast.BREADTH, noterArtisanEventClose);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onError
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedBackPanel);
            }

        };

        formNotationCastor.add(textAreaCommentaire, submitNotation, raterCastor);

        add(formNotationCastor, feedBackPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof NoterArtisanEventOpen) {
            NoterArtisanEventOpen noterArtisanEventOpen = (NoterArtisanEventOpen) event.getPayload();
            open(noterArtisanEventOpen.getTarget());
        }

        if (event.getPayload() instanceof NoterArtisanEventClose) {
            NoterArtisanEventClose noterArtisanEventClose = (NoterArtisanEventClose) event.getPayload();
            close(noterArtisanEventClose.getTarget());
        }
    }
}