package fr.castor.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Event d'ouverture de la popup permettant de noter l'artisan
 * 
 * @author Casaucau Cyril
 * 
 */
public class NoterArtisanEventOpen extends AbstractEvent {

    public NoterArtisanEventOpen(AjaxRequestTarget target) {
        super(target);
    }

}
