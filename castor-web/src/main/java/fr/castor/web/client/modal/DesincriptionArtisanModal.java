package fr.castor.web.client.modal;

import fr.castor.web.client.component.ModalCastor;
import fr.castor.web.client.event.DesinscriptionArtisanAnnonceEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;

import fr.castor.dto.ClientDTO;

/**
 * Modal permettant à un artisan de se desinscrire d'une annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class DesincriptionArtisanModal extends ModalCastor {

    private static final long serialVersionUID = 1L;

    private ClientDTO artisan;

    public DesincriptionArtisanModal(String id) {
        super(id, "Suppression d'une entreprise de votre annonce", "400");

        AjaxFallbackLink<Void> yes = new AjaxFallbackLink<Void>("yes") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(getPage(), Broadcast.BREADTH, new DesinscriptionArtisanAnnonceEvent(target, artisan));
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

    public void open(AjaxRequestTarget target, ClientDTO artisan) {
        super.open(target);
        this.artisan = artisan;
    }

}
