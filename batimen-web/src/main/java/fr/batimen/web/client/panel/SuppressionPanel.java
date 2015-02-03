package fr.batimen.web.client.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.event.SuppressionCloseEvent;
import fr.batimen.web.client.event.SuppressionOpenEvent;

public class SuppressionPanel extends Panel {

    private static final long serialVersionUID = -2013052713793815773L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionPanel.class);

    private static final String SHOW_SUPPRESSION_MODAL = "showSuppressionModal()";
    private static final String HIDE_SUPPRESSION_MODAL = "hideSuppressionModal()";

    private final AjaxFallbackLink<Void> yes;
    private final AjaxFallbackLink<Void> no;

    public SuppressionPanel(String id) {
        super(id);
        yes = new AjaxFallbackLink<Void>("yes") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                // TODO Faire l'appel du service de suppression de l'annonce.
                close(target);
            }
        };
        yes.setOutputMarkupId(true);
        yes.setMarkupId("yes");

        no = new AjaxFallbackLink<Void>("no") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                close(target);
            }
        };
        no.setOutputMarkupId(true);
        no.setMarkupId("no");

        add(yes, no);
    }

    /**
     * Ouvre la poppup de suppression d'une annonce
     * 
     * @param target
     */
    public void open(AjaxRequestTarget target) {
        target.appendJavaScript(SHOW_SUPPRESSION_MODAL);
    }

    /**
     * Ferme la popup de suppression d'une annonce
     * 
     * @param target
     */
    public void close(AjaxRequestTarget target) {
        target.appendJavaScript(HIDE_SUPPRESSION_MODAL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {

        if (event.getPayload() instanceof SuppressionOpenEvent) {
            SuppressionOpenEvent suppressionOpenEvent = (SuppressionOpenEvent) event.getPayload();
            open(suppressionOpenEvent.getTarget());
        }

        if (event.getPayload() instanceof SuppressionCloseEvent) {
            SuppressionCloseEvent suppressionCloseEvent = (SuppressionCloseEvent) event.getPayload();
            close(suppressionCloseEvent.getTarget());
        }
    }
}
