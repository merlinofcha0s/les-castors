package fr.batimen.ws.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.*;
import javax.inject.Inject;

import fr.batimen.dto.ImageDTO;
import fr.batimen.ws.dao.ImageDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Image;
import fr.batimen.ws.utils.RolesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudinary.Cloudinary;

/**
 * Classe de gestion des photos
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "PhotoService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    @Inject
    private CloudinaryService cloudinaryService;

    @Inject
    private ImageDAO imageDAO;

    @Inject
    private RolesUtils rolesUtils;

    /**
     * Envoi les photos vers le cloud de cloudinary
     *
     * @param photos Liste des photos sous forme de file
     * @return Les adresses internet des photos une fois uploadées
     */
    public List<String> sendPhotoToCloud(List<File> photos) {

        List<String> urlPhotosInCloud = new LinkedList<String>();

        Cloudinary cloudinary = cloudinaryService.getCloudinaryInstance();

        Map<String, Object> options = new HashMap<String, Object>();

        if (cloudinaryService.isInTestMode()) {
            options.put(CloudinaryService.FOLDER_PARAM, "test");
        }

        for (File photo : photos) {
            Map<String, String> uploadResults = null;
            try {
                uploadResults = cloudinary.uploader().upload(photo, options);
                urlPhotosInCloud.add(uploadResults.get(CloudinaryService.SECURE_URL_PARAM));
            } catch (IOException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Probleme durant l'upload de la photo vers le cloud", e);
                }
            }
        }
        return urlPhotosInCloud;
    }

    /**
     * Persist l'url des images en BDD
     *
     * @param annonce l'annonce auquel sont rattachés les images
     * @param imageUrls La liste des urls des images
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void persistPhoto(Annonce annonce, List<String> imageUrls) {
        for (String url : imageUrls) {
            Image nouvelleImage = new Image();
            nouvelleImage.setUrl(url);
            nouvelleImage.setAnnonce(annonce);
            annonce.getImages().add(nouvelleImage);
            imageDAO.createMandatory(nouvelleImage);
        }
    }

    /**
     * Récupère les images d'une annonces
     * <p/>
     * Check les droits du demandeur et dans le cas d'un client verifie qu'il possede bien l'annonce.
     *
     * @param rolesDemandeur Le role du demandeur
     * @param hashID L'identifiant unique de l'annonce
     * @param loginDemandeur Le login du demandeur de l'operation
     * @return L'ensemble des images de l'annonce si pas les droits => null
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<Image> getImagesByHashIDByLoginDemandeur(String rolesDemandeur, String hashID, String loginDemandeur) {
        List<Image> images = null;
        if (rolesUtils.checkIfClientWithString(rolesDemandeur)) {
            images = imageDAO.getImageByHashIDByClient(hashID, loginDemandeur);
        } else if (rolesUtils.checkIfAdminWithString(rolesDemandeur)) {
            images = imageDAO.getImageByHashID(hashID);
        }
        return images;
    }
}