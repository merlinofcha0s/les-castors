package fr.castor.web.client.extend.nouveau.artisan;

import fr.castor.web.client.extend.Accueil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

/**
 * Etape 4 : Confirme ou informe si il y a eu des erreurs lors de
 * l'enregistrement de ce compte par le webservice.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape4Confirmation extends Panel {

    private static final long serialVersionUID = 6182955802651062198L;

    private final Label messageConfirmation1;
    private final Label messageConfirmation2;
    private final Image imageConfirmation;

    public Etape4Confirmation(String id) {
        super(id);

        messageConfirmation1 = new Label("messageConfirmation1", new Model<String>());

        messageConfirmation2 = new Label("messageConfirmation2", new Model<String>());

        imageConfirmation = new Image("imageConfirmation", new ContextRelativeResource("img/ok.png"));
        imageConfirmation.add(new AttributeModifier("alt", "ok"));

        Link<Accueil> accueilLink = new Link<Accueil>("retourAccueil") {

            private static final long serialVersionUID = -732671761506748377L;

            @Override
            public void onClick() {
                this.setResponsePage(Accueil.class);
            }
        };

        this.add(messageConfirmation1);
        this.add(messageConfirmation2);
        this.add(imageConfirmation);
        this.add(accueilLink);
    }

    public void succesInscription() {
        messageConfirmation1.setDefaultModelObject("Votre compte a été créé avec succés.");
        messageConfirmation2
                .setDefaultModelObject("Les équipes de les castors vont valider votre dossier, nous vous tiendrons informé dès que votre compte sera activé.");
    }

    public void failureInscription() {
        messageConfirmation1.setDefaultModelObject("Un problème est survenue lors de la création de votre compte.");
        messageConfirmation2.setDefaultModelObject("Veuillez nous excuser de la gène occasionée.");
        imageConfirmation.setImageResource(new ContextRelativeResource("img/error.png"));
        imageConfirmation.add(new AttributeModifier("alt", "error"));
        this.addOrReplace(imageConfirmation);
    }
}
