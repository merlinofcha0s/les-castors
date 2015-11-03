package fr.castor.ws.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import fr.castor.core.enums.PropertiesFileGeneral;
import fr.castor.core.exception.BackendException;
import fr.castor.dto.ImageDTO;
import fr.castor.dto.aggregate.SuppressionPhotoDTO;
import fr.castor.ws.dao.ImageDAO;
import fr.castor.ws.entity.Annonce;
import fr.castor.ws.entity.Entreprise;
import fr.castor.ws.entity.Image;
import fr.castor.ws.utils.FluxUtils;
import fr.castor.ws.utils.RolesUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

        Map<String, Object> options = new HashMap<String, Object>();

        Cloudinary cloudinary = cloudinaryService.getCloudinaryInstance();
        if (cloudinaryService.isInTestMode()) {
            options.put(CloudinaryService.FOLDER_PARAM, "test");
        }

        for (File photo : photos) {
            Map<String, String> uploadResults = null;
            try {
                uploadResults = cloudinary.uploader().upload(photo, options);
                urlPhotosInCloud.add(uploadResults.get(CloudinaryService.SECURE_URL_PARAM));
            } catch (IOException | NullPointerException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Problème durant l'upload de la photo vers le cloud", e);
                }
            }
        }
        return urlPhotosInCloud;
    }

    /**
     * Supprime une image de cloudinary
     * <p/>
     *
     * @param imageASupprimer L'entité a supprimer.
     * @return True si la suppression c'est bien passé ou true si on est en test mode.
     */
    public boolean supprimerPhotoDansCloud(Image imageASupprimer) {
        List<String> imageSupprToCloud = new LinkedList<>();

        String idImage = cloudinaryService.getImageID(imageASupprimer);

        imageSupprToCloud.add(idImage);

        Cloudinary cloudinary = cloudinaryService.getCloudinaryInstance();

        ApiResponse response;
        try {
            response = cloudinary.api().deleteResources(imageSupprToCloud, new HashMap());
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Un problème est survenue lors de la suppression d'une photo dans cloudinary {}", imageASupprimer.toString(), e);
            }
            return false;
        }

        return cloudinaryService.verificationSuppressionCloudinary(response, idImage);
    }


    /**
     * Persist l'url des images en BDD
     *
     * @param annonce   l'annonce auquel sont rattachés les images
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
     * Persist l'url des images en BDD
     *
     * @param entreprise l'entreprise auquel sont rattachées les images
     * @param imageUrls  La liste des urls des images
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void persistPhoto(Entreprise entreprise, List<String> imageUrls) {
        for (String url : imageUrls) {
            Image nouvelleImage = new Image();
            nouvelleImage.setUrl(url);
            nouvelleImage.setEntreprise(entreprise);
            entreprise.getImagesChantierTemoin().add(nouvelleImage);
            imageDAO.createMandatory(nouvelleImage);
        }
    }

    /**
     * Récupère les images d'une annonces
     * <p/>
     * Check les droits du demandeur et dans le cas d'un client verifie qu'il possede bien l'annonce.
     *
     * @param rolesDemandeur Le role du demandeur
     * @param hashID         L'identifiant unique de l'annonce
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

    /**
     * Transform une liste d'entité image en liste de DTO image
     *
     * @param images La liste d'image a transformer
     * @return La liste d'image DTO
     */
    public List<ImageDTO> imageToImageDTO(Set<Image> images) {
        List<ImageDTO> imageDTOs = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        for (Image image : images) {
            ImageDTO imageDTO = new ImageDTO();
            mapper.map(image, imageDTO);
            imageDTOs.add(imageDTO);
        }

        return imageDTOs;
    }

    /**
     * Transform les fichiers qui sont de type FormDataBodyPart en Image
     * <p/>
     * Appel également le service qui envoi les photos sur cloudinary
     *
     * @param files  La liste de fichier provenant du form
     * @param images La liste des images deja persisté dans la BDD.
     * @return La liste des URLS des images qui se trouvent dans le cloud.
     */
    public List<String> transformAndSendToCloud(List<InputPart> files, Set<Image> images) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calcul du nombre de photos qui peuvent etre uploader avant d'atteindre la limite");
        }

        Integer nbPhotosTotale = images.size() + files.size();
        Integer nbPhotosMaxParAnnonce = Integer.valueOf(PropertiesFileGeneral.GENERAL.getProperties().getProperty("gen.max.number.file.annonce"));

        if (nbPhotosTotale > nbPhotosMaxParAnnonce) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Dépassement du nombre de photos autorisées {}", nbPhotosTotale);
            }
            return new ArrayList<>();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Transformation des forms data body parts en files");
        }
        List<File> photos = FluxUtils.transformInputPartsToFiles(files);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Envoi des photos vers le service de cloud");
        }
        return sendPhotoToCloud(photos);
    }

    /**
     * Récupère les images d'une annonces
     * <p/>
     * Check les droits du demandeur et dans le cas d'un artisan verifie qu'il possede bien l'entreprise.
     *
     * @param rolesDemandeur Le role du demandeur
     * @param siret          L'identifiant unique de l'entreprise
     * @param loginDemandeur Le login du demandeur de l'operation
     * @return L'ensemble des images de l'annonce si pas les droits => null
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<Image> getImagesBySiretByLoginDemandeur(String rolesDemandeur, String siret, String loginDemandeur) {
        List<Image> images = null;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Role demandeur : " + rolesDemandeur);
            LOGGER.debug("SIRET demandeur : " + siret);
            LOGGER.debug("Login demandeur : " + loginDemandeur);
        }
        if (rolesUtils.checkIfArtisanWithString(rolesDemandeur)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Artisan detecté");
            }
            images = imageDAO.getImageBySiretByArtisan(siret, loginDemandeur);
        } else if (rolesUtils.checkIfAdminWithString(rolesDemandeur)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Admin detecté");
            }
            images = imageDAO.getImageBySiret(siret);
        } else {
            LOGGER.error("Pas le bon role pour appeler le service, impossible");
        }
        return images;
    }

    /**
     * Suppression d'une photo a la fois dans la base de données et dans cloudinary
     *
     * @param images la liste des images de l'utilisateurs
     * @param suppressionPhotoDTO Les infos permettant de supprimer la photo
     * @return La liste des images apres suppression.
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<ImageDTO> suppressionPhoto(List<Image> images, SuppressionPhotoDTO suppressionPhotoDTO){
        List<ImageDTO> imageDTOs = new LinkedList<>();
        //Si cette liste d'images est null c'est que l'utilisateur n'a pas les droits
        if (images == null) {
            return new ArrayList<>();
        }

        boolean deleteAtLeastOne = false;

        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);

            if (image.getUrl().equals(suppressionPhotoDTO.getImageASupprimer().getUrl())) {
                imageDAO.delete(image);
                images.remove(i);
                supprimerPhotoDansCloud(image);
                deleteAtLeastOne = true;
            }
        }

        if (!deleteAtLeastOne) {
            try {
                throw new BackendException("Aucune image supprimée, ca ne doit pas arriver");
            } catch (BackendException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Problème avec le service de suppresson d'image", e);
                }
                return imageDTOs;
            }
        } else {
            imageDTOs = imageToImageDTO(new HashSet<>(images));
        }
        return imageDTOs;
    }
}