package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_NOM_COMPLET_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_NOM_COMPLET_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SIRET_REGEXP;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MIN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.dto.helper.SiretValidatorHelper;

public class EntrepriseDTO extends AbstractDTO {

    private static final long serialVersionUID = 6640996682839550750L;

    @NotNull
    @Pattern(message = "Format siret invalide", regexp = ENTREPRISE_SIRET_REGEXP)
    private String siret;
    @AssertTrue(message = "Siret invalide")
    private final boolean isSiretValide = SiretValidatorHelper.isSiretValide(getSiret());
    @NotNull
    private List<CategorieMetierDTO> categoriesMetier = new ArrayList<CategorieMetierDTO>();
    @Size(min = ENTREPRISE_NOM_COMPLET_MIN, max = ENTREPRISE_NOM_COMPLET_MAX)
    private String nomComplet;
    @NotNull
    private StatutJuridique statutJuridique;
    private Integer nbEmploye;
    private String logo;
    @NotNull
    @Past
    private Date dateCreation;
    @Size(min = ENTREPRISE_SPECIALITE_MIN, max = ENTREPRISE_SPECIALITE_MAX)
    private String specialité;

    /**
     * @return the nomComplet
     */
    public String getNomComplet() {
        return nomComplet;
    }

    /**
     * @return the statutJuridique
     */
    public StatutJuridique getStatutJuridique() {
        return statutJuridique;
    }

    /**
     * @return the nbEmployees
     */
    public Integer getNbEmployees() {
        return nbEmploye;
    }

    /**
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param nomComplet
     *            the nomComplet to set
     */
    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    /**
     * @param statutJuridique
     *            the statutJuridique to set
     */
    public void setStatutJuridique(StatutJuridique statutJuridique) {
        this.statutJuridique = statutJuridique;
    }

    /**
     * @param nbEmployees
     *            the nbEmployees to set
     */
    public void setNbEmployees(Integer nbEmployees) {
        this.nbEmploye = nbEmployees;
    }

    /**
     * @param logo
     *            the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the siret
     */
    public String getSiret() {
        if (siret == null) {
            return "";
        } else {
            return siret;
        }
    }

    /**
     * @param siret
     *            the siret to set
     */
    public void setSiret(String siret) {
        this.siret = siret;
    }

    /**
     * @return the categorieMetier
     */
    public List<CategorieMetierDTO> getCategorieMetier() {
        return categoriesMetier;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(List<CategorieMetierDTO> categorieMetier) {
        this.categoriesMetier = categorieMetier;
    }

}
