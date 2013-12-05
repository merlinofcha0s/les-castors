package fr.batimen.web.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

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

	public WsConnector() {
		getWsProperties();
	}

	public static WsConnector getInstance() {
		if (wsConnector == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Initialisation du singleton....");
			}
			wsConnector = new WsConnector();
			// Consomme bcp de ressource pour creer le client, il est thread
			// safe. Voir si ca ne pose pas de probleme de perf de l'avoir mis
			// en singleton
			client = Client.create();
			client.setFollowRedirects(true);
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

		StringBuilder adresseService = new StringBuilder("http://");
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

		return call.accept(MediaType.APPLICATION_JSON_TYPE).header("ACCEPT-CHARSET", "UTF-8")
				.entity(serializeToJSON(object), MediaType.APPLICATION_JSON_TYPE).post(String.class);

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
}
