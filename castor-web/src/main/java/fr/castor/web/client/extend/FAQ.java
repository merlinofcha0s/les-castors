package fr.castor.web.client.extend;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Page de foire aux questions
 *
 * @author Casaucau Cyril
 */
public class FAQ extends MasterPage {

    public FAQ() {
        super("Foire aux questions", "FAQ questions lescastors", "Foire aux questions", true, "img/bg_title1.jpg");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        StringBuilder adresseFAQ = new StringBuilder();
        adresseFAQ.append("https://lescastors.fr").append(UrlPage.FAQ);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseFAQ.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Foire aux questions du site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Consultez les questions les plus fréquentes"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", UrlPage.LOGO));
    }
}
