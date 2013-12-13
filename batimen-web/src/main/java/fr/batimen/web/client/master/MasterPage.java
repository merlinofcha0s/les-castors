package fr.batimen.web.client.master;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page principal de l'application dans laquelle tous les autres panels seront
 * contenues
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class MasterPage extends WebPage {

	private static final long serialVersionUID = 6955108821767948992L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterPage.class);
	private String metaDescription = "";
	private String metaKeywords = "";

	public MasterPage() {
		super();
		// Fix wicket : prends en charge les <!--[if IE 7 ]><html
		// class="ie ie7" lang="en"> <![endif]--> du template de maniere
		// programmatic
		TransparentWebMarkupContainer htmlTag = new TransparentWebMarkupContainer("htmlTag");
		add(htmlTag);
		htmlTag.add(AttributeAppender.replace("class", getHtmlTagClass()));

	}

	public MasterPage(PageParameters params) {
		this();
	}

	public MasterPage(String metaDescription, String metaKeywords) {
		this();
		this.metaDescription = metaDescription;
		this.metaKeywords = metaKeywords;
	}

	private IModel<String> getHtmlTagClass() {
		return new LoadableDetachableModel<String>() {

			private static final long serialVersionUID = 1502969877304945599L;

			@Override
			protected String load() {
				StringBuilder sb = new StringBuilder("");
				WebSession session = (WebSession) getSession();

				ClientProperties clientProperties = session.getClientInfo().getProperties();
				int majorVersion = clientProperties.getBrowserVersionMajor();

				if (clientProperties.isBrowserInternetExplorer()) {
					sb.append("ie ").append("ie").append(majorVersion);
				} else {
					sb.append("ie ").append("ie9");
				}

				return sb.toString();
			}
		};
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

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Ajout des resources dans le header.....");
		}

		// Balise meta
		response.render(addStringToMetaResourcesToHeader("text/html; charset=utf-8", "content-type", ""));
		response.render(addStringToMetaResourcesToHeader(metaDescription, "", "description"));
		response.render(addStringToMetaResourcesToHeader(metaKeywords, "", "keywords"));

		// Custom css File
		response.render(addCssFileToHeader("css/page.css"));

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Ajout des resources dans le header.....OK");
		}
	}

	/**
	 * Ajoute les fichier JS pour qu'il soit global à l'application
	 * 
	 * @param address
	 *            localisation du fichier js
	 * 
	 * @return objet wicket qui permet de generer la balise link
	 */
	private JavaScriptHeaderItem addJsFileToHeader(String address) {

		PackageResourceReference jsFile = new PackageResourceReference(fr.batimen.web.client.master.MasterPage.class,
				address);

		return JavaScriptHeaderItem.forReference(jsFile);
	}

	/**
	 * Ajoute les fichier CSS pour qu'il soit global à l'application
	 * 
	 * @param address
	 *            localisation du fichier css
	 * @param isUrlCSS
	 *            Est ce que le css se trouve autre part que dans l'application
	 *            ?
	 * @return objet wicket qui permet de generer la balise link
	 */
	private CssHeaderItem addCssFileToHeader(String address) {

		PackageResourceReference cssFile = new PackageResourceReference(fr.batimen.web.client.master.MasterPage.class,
				address);

		return CssHeaderItem.forReference(cssFile);
	}

	/**
	 * Génére une balise meta de maniere custom.
	 * 
	 * @param content
	 *            Le type de contenu (en general text/html)
	 * @param httpEquiv
	 * @param name
	 *            Type de la balise meta
	 * @return objet wicket qui permet de generer la balise link
	 */
	private StringHeaderItem addStringToMetaResourcesToHeader(String content, String httpEquiv, String name) {

		// On crée la balise de type : <meta content="text/html; charset=utf-8"
		// http-equiv="content-type" />
		StringBuilder balise = new StringBuilder("<meta content=\"");
		balise.append(content);
		balise.append("\"");
		if (!"".equals(httpEquiv)) {
			balise.append(generateAttribute("http-equiv", httpEquiv));
		}
		if (!"".equals(name)) {
			balise.append(generateAttribute("name", name));
		}
		balise.append("/>");

		// On dit à wicket de la placer dans le header de la master page
		return StringHeaderItem.forString(balise.toString());
	}

	private String generateAttribute(String paramName, String value) {
		StringBuilder attribute = new StringBuilder(paramName);
		attribute.append("=\"");
		attribute.append(value);
		attribute.append("\"");
		return attribute.toString();
	}
}
