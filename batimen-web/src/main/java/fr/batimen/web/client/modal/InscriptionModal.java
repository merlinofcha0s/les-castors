package fr.batimen.web.client.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;

import fr.batimen.web.client.component.ModalCastor;
import fr.batimen.web.client.event.InscriptionArtisanEvent;

public class InscriptionModal extends ModalCastor {

    private static final long serialVersionUID = 1615403190862019400L;

    public InscriptionModal(String id) {
        super(id, "Inscription à une annonce", "400");

        AjaxFallbackLink<Void> yes = new AjaxFallbackLink<Void>("yes") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(target.getPage(), Broadcast.BREADTH, new InscriptionArtisanEvent(target));
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
