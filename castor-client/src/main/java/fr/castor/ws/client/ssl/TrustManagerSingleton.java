package fr.castor.ws.client.ssl;

import fr.castor.ws.client.enums.PropertiesFileWsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;

public class TrustManagerSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrustManagerSingleton.class);
    private static TrustManagerSingleton trustManagerSingleton;
    private static TrustManager[] trustedCertificate;
    private static Boolean isProd;

    private TrustManagerSingleton() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Constructeur singleton trust manager....");
        }

        Properties wsProperties = PropertiesFileWsClient.WS.getProperties();
        isProd = Boolean.valueOf(wsProperties.getProperty("ws.isprod"));

    }

    private static void initTrustManagerSingleton() {
        if (trustManagerSingleton == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Instantiation du trust manager....");
            }
            trustManagerSingleton = new TrustManagerSingleton();

            trustedCertificate = new TrustManager[1];
            TrustManagerSingleton.setTrustedCertificate(trustedCertificate);

            // on est pas en prod donc : on desactive la vérification des
            // certificats
            if (!isProd) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Pas en prod => pas de verification du certificat");
                }
                trustedCertificate[0] = trustManagerWithoutCertificatCheck();
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
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[1];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        };

        return trustAllCerts;
    }

    public static SecureRandom secureRandomOrNot() {
        initTrustManagerSingleton();
        return new SecureRandom();
    }

    /**
     * Renvoi un trust manager en fonction de l'environement pour verifier ou
     * non l'identité du serveur suivant que l'on soit en prod ou pas
     * <p>
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
     * @param trustedCertificate the trustedCertificate to set
     */
    private static void setTrustedCertificate(TrustManager[] trustedCertificate) {
        // Même raison que pour le getter on enregistre seulement la copie
        TrustManagerSingleton.trustedCertificate = trustedCertificate.clone();
    }

}