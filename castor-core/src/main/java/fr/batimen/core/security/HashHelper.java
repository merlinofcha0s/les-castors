package fr.batimen.core.security;

import com.lambdaworks.crypto.SCryptUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Classe d'aide au hash (mot de passe, activation compte, etc)
 * 
 * @author Casaucau Cyril
 * 
 */
public class HashHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashHelper.class);

    private static String CHARSET_UTF_8 = "UTF-8";
    private static int CPU_COST = 64;
    private static int MEMORY_COST = 64;
    private static int PARALLELIZATION_COST = 64;
    private HashHelper() {

    }

    // TODO Pas sur que ces valeurs soient correct en 2013, a checker.

    /**
     * Génère un hash de manière sécurisée
     * 
     * @param input
     *            La chaine de caractere que l'on veut hasher
     * @return Le hash
     */
    public static String hashScrypt(String input) {
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
            chaineEncoder = new String(Base64.encodeBase64(chaineAEncoder.getBytes(Charset.forName(CHARSET_UTF_8))),
                    CHARSET_UTF_8);
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

    /**
     * Hash une chaine de caractére en SHA-512
     * 
     * @param chaineAEncoder
     *            La chaine a hasher
     * @return La chaine en SHA-512
     */
    public static String hashSHA512(String chaineAEncoder) {
        return DigestUtils.sha512Hex(chaineAEncoder);
    }

    /**
     * Génération d'un sel 32 Bytes
     * 
     * @return Sel random encodé en UTF-8
     */
    public static String generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        salt = Base64.encodeBase64(salt);

        ByteArrayInputStream bisSalt = new ByteArrayInputStream(salt);

        String saltString = "";
        try {
            saltString = IOUtils.toString(bisSalt, CHARSET_UTF_8);
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme d'encodage avec le sel", e);
            }
        }

        return saltString;
    }

    /**
     * Hash l'id d'une table pour la stocker en bdd, on s'en sert principalement
     * pour generer des urls qui ne montre pas le nombre d'annonce ou de profil
     * que l'on a dans la BDD
     * 
     * 
     * @param id
     *            l'id a hasher
     * @return L'id hashé
     */
    public static String hashID(Long id, String salt) {
        StringBuilder idTohash = new StringBuilder(String.valueOf(id));
        idTohash.append(salt);
        return hashSHA512(idTohash.toString());
    }
}
