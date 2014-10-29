package fr.batimen.core.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdaworks.crypto.SCryptUtil;

/**
 * Classe d'aide au hash (mot de passe, activation compte, etc)
 * 
 * @author Casaucau Cyril
 * 
 */
public class HashHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashHelper.class);

    private HashHelper() {

    }

    private static int CPU_COST = 32;
    private static int MEMORY_COST = 64;
    private static int PARALLELIZATION_COST = 64;

    // TODO Pas sur que ces valeurs soient correct en 2013, a checker.

    /**
     * Génère un hash de manière sécurisée
     * 
     * @param input
     *            La chaine de caractere que l'on veut hasher
     * @return Le hash
     */
    public static String hashString(String input) {
        return SCryptUtil.scrypt(input, CPU_COST, MEMORY_COST, PARALLELIZATION_COST);
    }

    /**
     * Regarde si le hash corresponds au password.
     * 
     * @param password
     *            le password de l'utilisateur
     * @param hash
     *            le hash qui se trouve dans la BDD
     * @return True si le password correspond au hash
     */
    public static boolean check(String password, String hash) {
        return SCryptUtil.check(password, hash);
    }

    /**
     * Converti une chaine de caractére en Base64
     * 
     * @param chaineAEncoder
     *            La chaine a transformer
     * @return La chaine de caractére en Base64
     */
    public static String convertToBase64(String chaineAEncoder) {
        String chaineEncoder;
        try {
            chaineEncoder = new String(Base64.encodeBase64(chaineAEncoder.getBytes(Charset.forName("UTF-8"))), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Impossible d'encoder la chaine de caractere en UTF-8", e);
            }
            return "";
        }

        return chaineEncoder;
    }

    /**
     * Hash une chaine de caractére en SHA-256
     * 
     * @param chaineAEncoder
     *            La chaine a hasher
     * @return La chaine en SHA-256
     */
    public static String hashSHA256(String chaineAEncoder) {
        return DigestUtils.sha256Hex(chaineAEncoder);
    }

}
