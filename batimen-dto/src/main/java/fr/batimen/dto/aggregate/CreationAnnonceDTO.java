package fr.batimen.dto.aggregate;

import static fr.batimen.dto.constant.ValidatorConstant.ADRESSE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ADRESSE_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.CODE_POSTAL_REGEX;
import static fr.batimen.dto.constant.ValidatorConstant.COMPLEMENT_ADRESSE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ANNONCE_DESCRIPTION_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ANNONCE_DESCRIPTION_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.VILLE_MAX;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.SousCategorieMetierDTO;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.helper.DeserializeJsonHelper;

/**
 * Objet d'échange permettant la creation d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CreationAnnonceDTO extends AbstractDTO {

    private static final long serialVersionUID = -7316855280979589583L;

    // Annonce
    @Valid
    @NotNull
    private CategorieMetierDTO categorieMetier;
    @Valid
    @NotNull
    private SousCategorieMetierDTO sousCategorie;
    @NotNull
    @Size(min = ANNONCE_DESCRIPTION_MIN, max = ANNONCE_DESCRIPTION_MAX)
    private String description;
    @NotNull
    private TypeContact typeContact;
    @NotNull
    private DelaiIntervention delaiIntervention;
    private List<File> photos = new ArrayList<File>();
    @NotNull
    @Size(min = ADRESSE_MIN, max = ADRESSE_MAX)
    private String adresse;
    @NotNull
    @Size(max = COMPLEMENT_ADRESSE_MAX)
    private String complementAdresse;
    @NotNull
    @Pattern(message = "Format code postal invalide", regexp = CODE_POSTAL_REGEX)
    private String codePostal;
    @NotNull
    @Size(max = VILLE_MAX)
    private String ville;
    @NotNull
    private Integer numeroEtape = 1;
    @NotNull
    @Min(value = 01)
    @Max(value = 100)
    private Integer departement;

    // Inscription
    @Valid
    private final ClientDTO client = new ClientDTO();

    private Boolean isSignedUp = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeContact getTypeContact() {
        return typeContact;
    }

    public void setTypeContact(TypeContact typeContact) {
        this.typeContact = typeContact;
    }

    public DelaiIntervention getDelaiIntervention() {
        return delaiIntervention;
    }

    public void setDelaiIntervention(DelaiIntervention delaiIntervention) {
        this.delaiIntervention = delaiIntervention;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * @return the numeroEtape
     */
    public Integer getNumeroEtape() {
        return numeroEtape;
    }

    /**
     * @param numeroEtape
     *            the numeroEtape to set
     */
    public void setNumeroEtape(Integer numeroEtape) {
        this.numeroEtape = numeroEtape;
    }

    public Integer getDepartement() {
        return departement;
    }

    public void setDepartement(Integer departement) {
        this.departement = departement;
    }

    /**
     * @return the photos
     */
    public List<File> getPhotos() {
        return photos;
    }

    /**
     * @param photos
     *            the photos to set
     */
    public void setPhotos(List<File> photos) {
        this.photos = photos;
    }

    /**
     * @return the client
     */
    public ClientDTO getClient() {
        return client;
    }

    /**
     * @return the isSignedUp
     */
    public Boolean getIsSignedUp() {
        return isSignedUp;
    }

    /**
     * @param isSignedUp
     *            the isSignedUp to set
     */
    public void setIsSignedUp(Boolean isSignedUp) {
        this.isSignedUp = isSignedUp;
    }

    /**
     * @return the categorieMetier
     */

    public CategorieMetierDTO getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */

    public void setCategorieMetier(CategorieMetierDTO categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    /**
     * @return the sousCategorie
     */

    public SousCategorieMetierDTO getSousCategorie() {
        return sousCategorie;
    }

    /**
     * 
     * @param sousCategorie
     *            the sousCategorie to set
     */

    public void setSousCategorie(SousCategorieMetierDTO sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.description, this.codePostal, this.ville));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof CreationAnnonceDTO) {
            CreationAnnonceDTO other = (CreationAnnonceDTO) object;
            return Objects.equals(this.description, other.description)
                    && Objects.equals(this.codePostal, other.codePostal) && Objects.equals(this.ville, other.ville);
        }
        return false;
    }

    public static CreationAnnonceDTO deserializeCreationAnnonceDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, CreationAnnonceDTO.class);
    }

    public static List<CreationAnnonceDTO> deserializeCreationAnnonceDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<CreationAnnonceDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
