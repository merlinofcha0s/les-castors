package fr.batimen.web.server;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Classe qui instantie un trustmanager qui verifira l'indentité du serveur
 * grace à son certificat / keystore.
 * 
 * A utiliser uniquement en production
 * 
 * TODO Il faudra rajouter l'instantiation du certificat et le passer en
 * parametre au keystore
 * 
 * @author Casaucau Cyril
 * 
 */
public class TrustManagerForProduction implements X509TrustManager {

	X509TrustManager pkixTrustManager;

	public TrustManagerForProduction() throws Exception {
		// String certFile = "/certificates/MyCertFile.cer";
		String keystoreFile = "";
		String password = "lolmdr";
		/*
		 * Certificate myCert =
		 * CertificateFactory.getInstance("X509").generateCertificate(
		 * this.getClass().getResourceAsStream(valicertFile));
		 */

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(keystoreFile), password.toCharArray());
		// keyStore.setCertificateEntry("myCert", myCert);

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
		trustManagerFactory.init(keyStore);

		TrustManager trustManagers[] = trustManagerFactory.getTrustManagers();

		for (TrustManager trustManager : trustManagers) {
			if (trustManager instanceof X509TrustManager) {
				pkixTrustManager = (X509TrustManager) trustManager;
				return;
			}
		}

		throw new Exception("Couldn't initialize");
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		pkixTrustManager.checkServerTrusted(chain, authType);

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		pkixTrustManager.checkServerTrusted(chain, authType);

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return pkixTrustManager.getAcceptedIssuers();
	}

}
