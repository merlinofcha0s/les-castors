package fr.batimen.web.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import fr.batimen.core.constant.Constant;

/**
 * Classe qui permet de préparer les requètes qui seront envoyées pour
 * interroger le webservice
 * 
 * @author Casaucau Cyril
 * 
 */
public class WsConnector {

	private static WsConnector wsConnector;
	private static Client client;
	private String ipServeur;
	private String portServeur;
	private String nomWs;

	private static final Logger LOGGER = LoggerFactory.getLogger(WsConnector.class);

	private WsConnector() {
		getWsProperties();
	}

	/**
	 * Instancie une seule fois le Wsconnector et le configure dans le but
	 * d'appeler le webservice de batimen
	 * 
	 * @return le WsConnector correctement configuré
	 */
	public static WsConnector getInstance() {
		if (wsConnector == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Initialisation du singleton....");
			}
			wsConnector = new WsConnector();
			// Consomme bcp de ressource pour creer le client, il est thread
			// safe. Voir si ca ne pose pas de probleme de perf de l'avoir mis
			// en singleton
			client = Client.create(configSSL());
			client.setFollowRedirects(true);
			// Authentification du client
			client.addFilter(new HTTPBasicAuthFilter(Constant.BATIMEN_USERS_WS, Constant.BATIMEN_PWD_WS));
			client.setConnectTimeout(Constant.CONNECT_TIMEOUT);
		}
		return wsConnector;
	}

	/**
	 * Permet de requeter le Webservice, toutes les requetes passent en POST
	 * 
	 * @param controller
	 *            Le controlleur que l'on veut interroger (voir WsPath)
	 * @param method
	 *            La methode que l'on veut interroger (voir WsPath)
	 * @param object
	 *            l'objet que l'on veut transmettre au WS
	 * @return Une chaine de caractere encodée JSON
	 */
	public String sendRequest(String controller, String method, Object object) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Construction de la requete ws....");
		}

		StringBuilder adresseService = new StringBuilder("https://");
		adresseService.append(ipServeur);
		adresseService.append(":");
		adresseService.append(portServeur);
		adresseService.append("/");
		adresseService.append(nomWs);
		adresseService.append("/");
		adresseService.append(controller);
		adresseService.append("/");
		adresseService.append(method);

		WebResource call = client.resource(adresseService.toString());

		String json = serializeToJSON(object);

		String reponse = call.accept(MediaType.APPLICATION_JSON_TYPE).header("ACCEPT-CHARSET", "UTF-8")
				.entity(json, MediaType.APPLICATION_JSON_TYPE).post(String.class);

		// Au cas ou : dans le cas d'une requete en get ne pas oublier de faire
		// un response.readentity pour fermer la requete HTTP
		return reponse;

	}

	private String serializeToJSON(Object object) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Serialization en json.....");
		}

		// Preparation à la serialization en JSON
		GsonBuilder builder = new GsonBuilder().serializeNulls();
		builder.setDateFormat(DateFormat.LONG);

		builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {

			public JsonElement serialize(Date t, Type type, JsonSerializationContext jsc) {
				return new JsonPrimitive(t.getTime());
			}
		});

		Gson gson = builder.create();
		String jsonRequest = gson.toJson(object);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Serialization en json.....OK");
		}

		return jsonRequest;

	}

	private void getWsProperties() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Récuperation des properties....");
		}
		Properties wsProperties = new Properties();
		try {
			wsProperties.load(getClass().getClassLoader().getResourceAsStream("ws.properties"));
			ipServeur = wsProperties.getProperty("ws.ip");
			portServeur = wsProperties.getProperty("ws.port");
			nomWs = wsProperties.getProperty("ws.name");
		} catch (IOException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erreur de récupération des properties ws", e);
			}
		}
	}

	/**
	 * Configure et initialise les differents composants permettant de
	 * comuniquer en SSL avec le webservice
	 * 
	 * @return Le ClientConfig avec le SSL correctement configurer
	 */
	private static ClientConfig configSSL() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Début de la configuration du SSL (init du context).....");
		}

		SSLContext context = null;
		try {
			context = SSLContext.getInstance("SSLv3");
		} catch (NoSuchAlgorithmException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Algorithme SSL introuvable", e);
			}
		}

		// On initialise le context avec le bon trust manager qui activera ou
		// non la verification du certificat.
		try {
			context.init(null, TrustManagerSingleton.getTrustedCertificate(), TrustManagerSingleton.secureRandomOrNot());
		} catch (KeyManagementException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Problème de chargement de certificat", e);
			}
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Config de client config.....");
		}

		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(new HostnameVerifier() {
					@Override
					public boolean verify(String s, SSLSession sslSession) {
						return true;
					}
				}, context));

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Config de client config.....OK");
		}

		return config;
	}
}
