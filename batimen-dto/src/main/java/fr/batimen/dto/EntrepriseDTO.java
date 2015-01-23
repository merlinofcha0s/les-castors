package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_NOM_COMPLET_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SIRET_REGEXP;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MIN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;

public class EntrepriseDTO extends AbstractDTO {

    private static final long serialVersionUID = 6640996682839550750L;

    @NotNull
    @Pattern(message = "Format siret invalide", regexp = ENTREPRISE_SIRET_REGEXP)
    private String siret;

    @NotNull
    private final List<CategorieMetierDTO> categoriesMetier = new ArrayList<CategorieMetierDTO>();

    @NotNull
    @Size(min = ValidatorConstant.ENTREPRISE_NOM_COMPLET_MIN, max = ENTREPRISE_NOM_COMPLET_MAX)
    private String nomComplet;

    @NotNull
    private StatutJuridique statutJuridique;

    private Integer nbEmployees;
    private String logo;

    @NotNull
    @Past
    private Date dateCreation;
    @Size(min = ENTREPRISE_SPECIALITE_MIN, max = ENTREPRISE_SPECIALITE_MAX)
    private String specialite;

    private ClientDTO artisan;

    /**
     * @return the dateCreation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * @return the specialite
     */
    public String getSpecialite() {
        return specialite;
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @param specialite
     *            the specialite to set
     */
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public StatutJuridique getStatutJuridique() {
        return statutJuridique;
    }

    public Integer getNbEmployees() {
        return nbEmployees == null ? Integer.valueOf(0) : nbEmployees;
    }

    public String getLogo() {
        return logo;
    }

    public void setStatutJuridique(StatutJuridique statutJuridique) {
        this.statutJuridique = statutJuridique;
    }

    public void setNbEmployees(Integer nbEmployees) {
        this.nbEmployees = nbEmployees;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getSiret() {
        if (siret == null) {
            return "";
        } else {
            return siret;
        }
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public List<CategorieMetierDTO> getCategoriesMetier() {
        return categoriesMetier;
    }

    /**
     * @return the artisan
     */
    public ClientDTO getArtisan() {
        return artisan;
    }

    /**
     * @param artisan
     *            the artisan to set
     */
    public void setArtisan(ClientDTO artisan) {
        this.artisan = artisan;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.getSiret(), this.getNomComplet()));
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

        if (object instanceof EntrepriseDTO) {
            EntrepriseDTO other = (EntrepriseDTO) object;
            return Objects.equals(this.getSiret(), other.getSiret())
                    && Objects.equals(this.getNomComplet(), other.getNomComplet());
        }
        return false;
    }
}
