package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.ADRESSE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ADRESSE_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.CODE_POSTAL_REGEX;
import static fr.batimen.dto.constant.ValidatorConstant.COMPLEMENT_ADRESSE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.VILLE_MAX;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AdresseDTO extends AbstractDTO {

    private static final long serialVersionUID = 3446506172762761335L;

    @NotNull
    @Size(min = ADRESSE_MIN, max = ADRESSE_MAX)
    private String adresse;
    @Size(max = COMPLEMENT_ADRESSE_MAX)
    private String complementAdresse;
    @NotNull
    @Pattern(message = "Format code postal invalide", regexp = CODE_POSTAL_REGEX)
    private String codePostal;
    @NotNull
    @Size(max = VILLE_MAX)
    private String ville;
    @NotNull
    @Min(value = 01)
    @Max(value = 100)
    private Integer departement;

    /**
     * @return the adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * @return the complementAdresse
     */
    public String getComplementAdresse() {
        return complementAdresse;
    }

    /**
     * @return the codePostal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * @return the ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * @return the departement
     */
    public Integer getDepartement() {
        return departement;
    }

    /**
     * @param adresse
     *            the adresse to set
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @param complementAdresse
     *            the complementAdresse to set
     */
    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    /**
     * @param codePostal
     *            the codePostal to set
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * @param ville
     *            the ville to set
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * @param departement
     *            the departement to set
     */
    public void setDepartement(Integer departement) {
        this.departement = departement;
    }

}
