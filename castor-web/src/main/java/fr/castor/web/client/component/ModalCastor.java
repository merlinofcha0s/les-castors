package fr.castor.web.client.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Fenetre modal boostrap
 * 
 * @author Casaucau Cyril
 * 
 */
public class ModalCastor extends Panel {

    private static final long serialVersionUID = -1420068924107876280L;

    private final TransparentWebMarkupContainer modalCastorContainer;
    private final Label title;
    private final String id;
    private final StringBuilder fctJsNameOpen;
    private final StringBuilder fctJsNameClose;

    public ModalCastor(String id, String title, String size) {
        super(id);
        this.id = id;
        StringBuilder sizeModalCSS = new StringBuilder("width: ");
        sizeModalCSS.append(size).append("px");
        // MASTER CONTAINER
        modalCastorContainer = new TransparentWebMarkupContainer("modalCastor");
        modalCastorContainer.setOutputMarkupId(true);
        modalCastorContainer.setMarkupId(id);
        modalCastorContainer.add(new AttributeModifier("style", sizeModalCSS.toString()));

        this.title = new Label("title", title);

        add(modalCastorContainer, this.title);
        // NOM DES FCT JAVASCRIPT POUR L OUVERTURE / FERMETURE DE LA MODAL
        fctJsNameOpen = new StringBuilder("showModal");
        fctJsNameOpen.append(id).append("Open()");

        fctJsNameClose = new StringBuilder("showModal");
        fctJsNameClose.append(id).append("Close()");
    }

    /**
     * Génére la fonction js qui permet d'ouvrir / fermer la popup
     * 
     * @param open
     * @return
     */
    private String getJSForOpenCloseModal(boolean open) {
        StringBuilder openCloseModalJSBody = new StringBuilder("function showModal");
        openCloseModalJSBody.append(id);

        if (open) {
            openCloseModalJSBody.append("Open");
        } else {
            openCloseModalJSBody.append("Close");
        }

        openCloseModalJSBody.append("(){ $('#").append(id).append("').modal('");

        if (open) {
            openCloseModalJSBody.append("show');}");
        } else {
            openCloseModalJSBody.append("hide');}");
        }

        return openCloseModalJSBody.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.
     * IHeaderResponse)
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        StringBuilder idOpenScript = new StringBuilder(id);
        idOpenScript.append("openJS");
        StringBuilder idCloseScript = new StringBuilder(id);
        idCloseScript.append("closeJS");
        response.render(JavaScriptHeaderItem.forScript(getJSForOpenCloseModal(true), idOpenScript.toString()));
        response.render(JavaScriptHeaderItem.forScript(getJSForOpenCloseModal(false), idCloseScript.toString()));
    }

    /**
     * Ouvre la poppup de suppression d'une annonce
     * 
     * @param target
     */
    public void open(AjaxRequestTarget target) {
        target.appendJavaScript(fctJsNameOpen.toString());
    }

    /**
     * Ferme la popup de suppression d'une annonce
     * 
     * @param target
     */
    public void close(AjaxRequestTarget target) {
        target.appendJavaScript(fctJsNameClose.toString());
    }
}