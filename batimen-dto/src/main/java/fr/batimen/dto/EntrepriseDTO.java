package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_NOM_COMPLET_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_NOM_COMPLET_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SIRET_REGEXP;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ENTREPRISE_SPECIALITE_MIN;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.dto.helper.SiretValidator;

public class EntrepriseDTO extends AbstractDTO {

    private static final long serialVersionUID = 6640996682839550750L;

    @NotNull
    @Pattern(message = "Format siret invalide", regexp = ENTREPRISE_SIRET_REGEXP)
    private String siret;
    @AssertTrue(message = "Siret invalide")
    private final boolean isSiretValide = SiretValidator.isSiretValide(siret);
    @NotNull
    private CategorieMetierDTO categorieMetier;
    @Size(min = ENTREPRISE_NOM_COMPLET_MIN, max = ENTREPRISE_NOM_COMPLET_MAX)
    private String nomComplet;
    @NotNull
    private StatutJuridique statutJuridique;
    private Integer nbEmployees;
    private String logo;
    @NotNull
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
        return nbEmployees;
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
        this.nbEmployees = nbEmployees;
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
        return siret;
    }

    /**
     * @return the categorieMetier
     */
    public CategorieMetierDTO getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @param siret
     *            the siret to set
     */
    public void setSiret(String siret) {
        this.siret = siret;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(CategorieMetierDTO categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

}
