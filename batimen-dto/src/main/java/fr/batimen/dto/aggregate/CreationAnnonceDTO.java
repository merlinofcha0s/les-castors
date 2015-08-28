package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.MotCleDTO;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.*;

/**
 * Objet d'échange permettant la creation d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CreationAnnonceDTO extends AbstractDTO {

    private static final long serialVersionUID = -7316855280979589583L;
    private final List<String> villesPossbles = new ArrayList<>();
    // Annonce
    @Valid
    @NotNull
    private List<MotCleDTO> motCles = new ArrayList<>();
    @NotNull
    @Size(min = ANNONCE_DESCRIPTION_MIN, max = ANNONCE_DESCRIPTION_MAX)
    private String description;
    @NotNull
    private TypeContact typeContact;
    @NotNull
    private DelaiIntervention delaiIntervention;
    private transient List<File> photos = new ArrayList<File>();
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
    @NotNull
    private TypeTravaux typeTravaux;
    // Inscription
    @Valid
    private ClientDTO client = new ClientDTO();
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
     * @param client
     *            the client to set
     */
    public void setClient(ClientDTO client) {
        this.client = client;
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
     * @return the typeTravaux
     */
    public TypeTravaux getTypeTravaux() {
        return typeTravaux;
    }

    /**
     * @param typeTravaux
     *            the typeTravaux to set
     */
    public void setTypeTravaux(TypeTravaux typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public List<String> getVillesPossbles() {
        return villesPossbles;
    }


    public List<MotCleDTO> getMotCles() {
        return motCles;
    }

    public void setMotCles(List<MotCleDTO> motCles) {
        this.motCles = motCles;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreationAnnonceDTO{");
        sb.append("categoriesMetier=").append(motCles);
        sb.append(", description='").append(description).append('\'');
        sb.append(", typeContact=").append(typeContact);
        sb.append(", delaiIntervention=").append(delaiIntervention);
        sb.append(", photos=").append(photos);
        sb.append(", adresse='").append(adresse).append('\'');
        sb.append(", complementAdresse='").append(complementAdresse).append('\'');
        sb.append(", codePostal='").append(codePostal).append('\'');
        sb.append(", ville='").append(ville).append('\'');
        sb.append(", numeroEtape=").append(numeroEtape);
        sb.append(", departement=").append(departement);
        sb.append(", typeTravaux=").append(typeTravaux);
        sb.append(", client=").append(client);
        sb.append(", isSignedUp=").append(isSignedUp);
        sb.append(", villesPossbles=").append(villesPossbles);
        sb.append('}');
        return sb.toString();
    }
}