package fr.castor.web.client.extend;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.app.constants.JSConstant;
import fr.castor.web.client.component.ContactezNous;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Page nos objectif
 */
public class Objectif extends MasterPage {

    public Objectif() {
        super("Nos objectifs", "Objectif plateforme recherche contact artisan particulier", "Nos objectifs", true, "img/bg_title1.jpg");
        ContactezNous contactezNousComposant = new ContactezNous("contactezNous");
        add(contactezNousComposant);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JSConstant.fontAwesome);

        StringBuilder adresseNosObjectifs = new StringBuilder();
        adresseNosObjectifs.append("https://lescastors.fr").append(UrlPage.NOS_OBJECTIFS);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseNosObjectifs.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Nos objectifs"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Quelles sont les objectifs de la plateforme lescastors.fr ?"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", UrlPage.LOGO));
    }
}
