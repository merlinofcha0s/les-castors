package fr.castor.web.client.extend;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Page de description de nos conditions générales de vente et d'utilisation.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CGU extends MasterPage {

	private static final long serialVersionUID = -4859858357188558602L;

	public CGU() {
        super("Conditions générales d'utilisation de lescastors.fr", "Conditions générales d'utilisation CGU", "Nos conditions générales d'utilisation", true,
                "img/bg_title1.jpg");
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        StringBuilder adresseCGU = new StringBuilder();
        adresseCGU.append("https://lescastors.fr").append(UrlPage.CGU_URL);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseCGU.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "CGU du site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Accéder à nos conditions générales d'utilisation"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", UrlPage.LOGO));
    }
}
