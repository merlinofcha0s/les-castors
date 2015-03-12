package fr.batimen.web.client.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;

import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.web.client.component.ModalCastor;
import fr.batimen.web.client.event.SelectionEntrepriseEvent;

public class SelectionEntrepriseModal extends ModalCastor {

    private static final long serialVersionUID = 1L;

    private EntrepriseDTO entreprise;

    public SelectionEntrepriseModal(String id) {
        super(id, "Sélection d'une entreprise", "400");

        AjaxFallbackLink<Void> yes = new AjaxFallbackLink<Void>("yes") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(getPage(), Broadcast.BREADTH, new SelectionEntrepriseEvent(target, entreprise));
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.batimen.web.client.component.ModalCastor#open(org.apache.wicket.ajax
     * .AjaxRequestTarget)
     */
    public void open(AjaxRequestTarget target, EntrepriseDTO entrepriseSelectionnee) {
        super.open(target);
        entreprise = entrepriseSelectionnee;
    }

}
