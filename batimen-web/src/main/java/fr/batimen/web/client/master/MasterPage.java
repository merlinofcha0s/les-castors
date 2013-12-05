package fr.batimen.web.client.master;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page principal de l'application dans laquelle tous les autres panels seront
 * contenues
 * 
 * @author Casaucau
 * 
 */
public abstract class MasterPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6955108821767948992L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterPage.class);

	public MasterPage() {
		super();
	}

	public MasterPage(PageParameters params) {
		this();
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

		// Ajouter les fichier CSS / JS ici pour qu'il soit global à
		// l'application
		PackageResourceReference cssFile = new PackageResourceReference(fr.batimen.web.client.master.MasterPage.class,
				"css/page.css");
		CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
		response.render(cssItem);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Ajout du css.....OK");
		}
	}

}
