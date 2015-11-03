package fr.castor.web.client.extend;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Page qui permettra à l'équipe de se présenter.
 * 
 * @author Casaucau Cyril
 * 
 */
public class QuiSommeNous extends MasterPage {

	private static final long serialVersionUID = -3366422085477423896L;

	public QuiSommeNous() {
        super("Qui sommes nous ?", "Equipe lescastors présentation", "Qui sommes nous ?", true, "img/bg_title1.jpg");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        StringBuilder adresseQuiSommesNous = new StringBuilder();
        adresseQuiSommesNous.append("https://lescastors.fr").append(UrlPage.QUI_SOMMES_NOUS_URL);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseQuiSommesNous.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Présentation de l'équipe du site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Petite présentation de notre équipe"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", UrlPage.LOGO));
    }
}
