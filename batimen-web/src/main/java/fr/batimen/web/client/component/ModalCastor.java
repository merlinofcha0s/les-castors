package fr.batimen.web.client.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class ModalCastor extends Panel {

    private static final long serialVersionUID = -1420068924107876280L;

    private final WebMarkupContainer modalCastor;
    private final String id;
    private final StringBuilder fctJsNameOpen;
    private final StringBuilder fctJsNameClose;

    public ModalCastor(String id) {
        super(id);
        this.id = id;
        modalCastor = new WebMarkupContainer("modalCastor");
        modalCastor.setOutputMarkupId(true);
        modalCastor.setMarkupId(id);
        add(modalCastor);

        fctJsNameOpen = new StringBuilder("showModal");
        fctJsNameOpen.append(id).append("Open()");

        fctJsNameClose = new StringBuilder("showModal");
        fctJsNameClose.append(id).append("Close()");

    }

    private String getJSForOpenCloseModal(boolean open) {
        StringBuilder openCloseModalJSBody = new StringBuilder("function showModal");
        openCloseModalJSBody.append(id);

        if (open) {
            openCloseModalJSBody.append("Open");
        } else {
            openCloseModalJSBody.append("Close");
        }

        openCloseModalJSBody.append("(){ $('#showModal").append(id).append("').modal('");

        if (open) {
            openCloseModalJSBody.append(" show');");
        } else {
            openCloseModalJSBody.append(" hide');");
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
        response.render(JavaScriptHeaderItem.forScript(getJSForOpenCloseModal(false), idOpenScript.toString()));
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