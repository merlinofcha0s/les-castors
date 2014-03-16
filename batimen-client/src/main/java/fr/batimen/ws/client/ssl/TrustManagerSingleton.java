package fr.batimen.ws.client.ssl;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustManagerSingleton {

	private static TrustManagerSingleton trustManagerSingleton;
	private static TrustManager[] trustedCertificate;
	private static Boolean isProd;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrustManagerSingleton.class);

	private TrustManagerSingleton() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructeur singleton trust manager....");
		}
		// On regarde dans quelle environement on se trouve
		Properties wsProperties = new Properties();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation de la properties pour connaitre l'environement..");
			}
			wsProperties.load(getClass().getClassLoader().getResourceAsStream("ws.properties"));
			isProd = Boolean.valueOf(wsProperties.getProperty("ws.isprod"));
		} catch (IOException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erreur de récupération de la properties ws.isprod", e);
			}
		}
	}

	private static void initTrustManagerSingleton() {
		if (trustManagerSingleton == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Instantiation du trust manager....");
			}
			trustManagerSingleton = new TrustManagerSingleton();

			trustedCertificate = new TrustManager[1];
			TrustManagerSingleton.setTrustedCertificate(trustedCertificate);

			// Si on est en prod, on active la verification de l'identité du
			// serveur (ws)
			if (isProd) {

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Prod => la verif du certificat est active");
				}

				try {
					trustedCertificate[0] = new TrustManagerForProduction();
				} catch (Exception e) {
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error("Problème pendant l'initialisation du trust manager for production", e);
					}
				}
				// on est pas en prod donc : on desactive la vérification des
				// certificats
			} else if (!isProd) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Pas en prod => pas de verification du certificat");
				}
				trustedCertificate[0] = (TrustManager) trustManagerWithoutCertificatCheck();
			}
		}
	}

	/**
	 * Renvoi un TrustManager qui ne verifie pas l'identité du serveur
	 * 
	 * @return trust manager qui ne verifie pas l'identité du serveur
	 */
	private static X509TrustManager trustManagerWithoutCertificatCheck() {
		X509TrustManager trustAllCerts = new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[1];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		};

		return trustAllCerts;
	}

	public static SecureRandom secureRandomOrNot() {
		initTrustManagerSingleton();
		if (isProd) {
			return null;
		} else {
			return new SecureRandom();
		}
	}

	/**
	 * Renvoi un trust manager en fonction de l'environement pour verifier ou
	 * non l'identité du serveur suivant que l'on soit en prod ou pas
	 * 
	 * S'occupe également d'intialiser le singleton
	 * 
	 * @return the trustedCertificate
	 */
	public static TrustManager[] getTrustedCertificate() {
		initTrustManagerSingleton();
		// Clone car sinon probleme de sécurité : le tableau pourrait etre
		// modifié plus tard
		return trustedCertificate.clone();
	}

	/**
	 * @param trustedCertificate
	 *            the trustedCertificate to set
	 */
	private static void setTrustedCertificate(TrustManager[] trustedCertificate) {
		// Même raison que pour le getter on enregistre seulement la copie
		TrustManagerSingleton.trustedCertificate = trustedCertificate.clone();
	}

}
