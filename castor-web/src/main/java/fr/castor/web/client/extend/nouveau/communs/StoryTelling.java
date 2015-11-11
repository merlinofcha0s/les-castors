package fr.castor.web.client.extend.nouveau.communs;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ContextRelativeResource;

/**
 * Affiche un castor et un racoon avec un bulle qui fait parler le castor
 */
public class StoryTelling extends Panel {

    public StoryTelling(String id, String bubbleSpeech, int sizeCastor, int sizeRacoon) {
        super(id);
        Label bubbleSpeechLabel = new Label("bubbleSpeechLabel", bubbleSpeech);

        StringBuilder chaineSizeCastor = new StringBuilder();
        chaineSizeCastor.append(sizeCastor).append("px");

        StringBuilder chaineSizeRacoon = new StringBuilder();
        chaineSizeRacoon.append(sizeRacoon).append("px");

        Image castor = new Image("castor", new ContextRelativeResource("img/person.png"));
        castor.add(new AttributeModifier("width", chaineSizeCastor.toString()));

        Image racoon = new Image("racoon", new ContextRelativeResource("img/raccoon.png"));
        racoon.add(new AttributeModifier("width", chaineSizeRacoon.toString()));

        add(bubbleSpeechLabel, castor, racoon);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("css/bubble.css"));
    }
}