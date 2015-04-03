package fr.batimen.web.client.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;

import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.BatimenFeedbackPanel;
import fr.batimen.web.client.component.ModalCastor;
import fr.batimen.web.client.component.RaterCastor;
import fr.batimen.web.client.event.NoterArtisanEventClose;
import fr.batimen.web.client.event.NoterArtisanEventOpen;

/**
 * Modal permettant à un client de noter un artisan une fois que les travaux
 * sont terminés.
 * 
 * @author Casaucau Cyril
 * 
 */
public class NotationArtisanModal extends ModalCastor {

    private static final long serialVersionUID = 1L;

    private final RaterCastor raterCastor;
    private final TextArea<String> textAreaCommentaire;

    private final BatimenFeedbackPanel feedBackPanel;

    public NotationArtisanModal(String id) {
        super(id, "Evaluation de votre artisan", "550");

        feedBackPanel = new BatimenFeedbackPanel("feedbackPanelNotationArtisan");
        feedBackPanel.setOutputMarkupId(true);

        raterCastor = new RaterCastor("raterCastor", 5, true);
        raterCastor.setMarkupId("raterCastor");

        textAreaCommentaire = new TextArea<String>("textAreaCommentaireNotation", new Model<String>());
        textAreaCommentaire.setMarkupId("textAreaCommentaireNotation");
        textAreaCommentaire.setRequired(true);
        textAreaCommentaire.add(new RequiredBorderBehaviour());

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
