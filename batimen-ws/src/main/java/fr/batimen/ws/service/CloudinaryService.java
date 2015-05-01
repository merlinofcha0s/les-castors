package fr.batimen.ws.service;

import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

import fr.batimen.ws.entity.Image;
import fr.batimen.ws.enums.PropertiesFileWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton permettant l'appel a cloudinary (cloud de stockage des photos)
 *
 * @author Casaucau Cyril
 */
@Singleton
public class CloudinaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinaryInstance;

    public final static String FOLDER_PARAM = "folder";
    public final static String SECURE_URL_PARAM = "secure_url";

    public CloudinaryService() {
        Properties imagesProperties = PropertiesFileWS.IMAGE.getProperties();
        cloudinaryInstance = new Cloudinary(ObjectUtils.asMap("cloud_name", imagesProperties.getProperty("cloud.name"),
                "api_key", imagesProperties.getProperty("api.key"), "api_secret",
                imagesProperties.getProperty("api.secret")));
    }

    /**
     * @return the cloudinaryInstance
     */
    public Cloudinary getCloudinaryInstance() {
        return cloudinaryInstance;
    }

    public boolean isInTestMode() {
        Properties imageProperties = PropertiesFileWS.IMAGE.getProperties();
        return Boolean.valueOf(imageProperties.getProperty("is.test"));
    }

    /**
     * Méthode qui extrait l'id cloudinary d'une image
     * <p/>
     * Exemple d'URL : http://res.cloudinary.com/lescastors/image/upload/v1430483107/test/urhjqs35iuyukbb8ov2m.jpg
     * <p/>
     * Attention : l'id comprend aussi le dossier ou se trouve l'image voir le cas ci dessus => test/urhjqs35iuyukbb8ov2m.jpg
     *
     * @param imageASupprimer L'image d'où l'on doit extraire l'ID
     * @return L'id cloudinary
     */
    public String getImageID(Image imageASupprimer) {
        //On decoupe l'url pour en extraire l'id de l'image
        String[] morceauURL = imageASupprimer.getUrl().split("/");
        String idImage = morceauURL[8].replace(".jpg", "");

        if (isInTestMode()) {
            StringBuilder testIdImage = new StringBuilder("test/");
            testIdImage.append(idImage);
            idImage = testIdImage.toString();
        }
        return idImage;
    }

    /**
     * Vérifie que l'image a bien été supprimée de cloudinary
     * <p/>
     * Voici à quoi ressemble le json que doit répondre cloudinary.
     * <p/>
     * {
     * "deleted": {
     * "cb4eaaf650": "deleted",
     * "a7b2a2756a": "deleted"
     * }
     * }
     *
     * @param response La map contenant le retour du webservice de cloudinary
     * @param idImage  l'id unique de l'image
     * @return True si la suppression est positive
     */
    public boolean verificationSuppressionCloudinary(ApiResponse response, String idImage) {
        //On test si il y a le mot clé deleted dans le json de sortie (voir javadoc au dessus pour plus d'info)
        Map<String, String> resultMapImageDeleted = (Map) response.get("deleted");
        String deletedResult = resultMapImageDeleted.get(idImage);
        if (deletedResult.equals("deleted")) {
            return true;
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("L'image n'a pas été supprimée, ne doit pas arriver", idImage.toString());
            }
            return false;
        }
    }
}