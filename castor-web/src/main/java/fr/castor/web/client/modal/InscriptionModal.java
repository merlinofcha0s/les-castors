package fr.castor.web.client.modal;

import fr.castor.web.client.component.ModalCastor;
import fr.castor.web.client.event.InscriptionArtisanEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;

/**
 * Modal permettant a un artisan de confirmer / payer son inscription
 * 
 * @author Casaucau Cyril
 * 
 */
public class InscriptionModal extends ModalCastor {

    private static final long serialVersionUID = 1615403190862019400L;

    public InscriptionModal(String id) {
        super(id, "Inscription à une annonce", "400");

        AjaxFallbackLink<Void> yes = new AjaxFallbackLink<Void>("yes") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(getPage(), Broadcast.BREADTH, new InscriptionArtisanEvent(target));
                close(target);
            }

        };

        AjaxFallbackLink<Void> no = new AjaxFallbackLink<Void>("no") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                close(target);
            }
        };

        add(yes, no);
    }
}
