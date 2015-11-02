package fr.batimen.web.client.component;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * Link qui permet de joindre un label sans ajout supplementaire d'une autre
 * balise.
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class LinkLabel extends Link<Void> {

    private static final long serialVersionUID = 1846555084490851850L;

    private final IModel<String> modelString;

    public LinkLabel(String id, IModel<String> modelString) {
        super(id);
        this.modelString = modelString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.markup.html.link.AbstractLink#onComponentTagBody(org
     * .apache.wicket.markup.MarkupStream,
     * org.apache.wicket.markup.ComponentTag)
     */
    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, modelString.getObject().toString());
    }

}
