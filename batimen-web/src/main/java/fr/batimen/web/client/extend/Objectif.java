package fr.batimen.web.client.extend;

import fr.batimen.core.constant.UrlPage;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Created by Casaucau on 28/10/2015.
 */
public class Objectif extends MasterPage {

    public Objectif() {
        super("Nos objectifs", "Objectif plateforme recherche contact professionnel particulier", "Nos objectifs", true, "img/bg_title1.jpg");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
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
