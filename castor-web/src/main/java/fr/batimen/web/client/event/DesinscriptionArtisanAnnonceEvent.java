package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import fr.batimen.dto.ClientDTO;

/**
 * Event permettant la desinscription d'un artisan sur annonce précise
 * 
 * @author Casaucau
 * 
 */
public class DesinscriptionArtisanAnnonceEvent extends AbstractEvent {

    private final ClientDTO artisan;

    public DesinscriptionArtisanAnnonceEvent(AjaxRequestTarget target, ClientDTO artisan) {
        super(target);
        this.artisan = artisan;
    }

    /**
     * @return the artisan
     */
    public ClientDTO getArtisan() {
        return artisan;
    }

}
