package fr.batimen.ws.service;

import java.util.Properties;

import javax.inject.Singleton;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import fr.batimen.ws.enums.PropertiesFileWS;

/**
 * Singleton permettant l'appel a cloudinary (cloud de stockage des photos)
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
@Singleton
public class CloudinaryService {

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
}