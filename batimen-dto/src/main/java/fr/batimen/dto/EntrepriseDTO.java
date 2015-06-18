package fr.batimen.dto;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.*;

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

    private AdresseDTO adresseEntreprise;

    private List<AvisDTO> notationsDTO = new ArrayList<>();

    private Boolean isVerifier;

    private Double moyenneAvis;

    private Integer nbAnnonce;

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

    public AdresseDTO getAdresseEntreprise() {
        return adresseEntreprise;
    }

    public void setAdresseEntreprise(AdresseDTO adresseEntreprise) {
        this.adresseEntreprise = adresseEntreprise;
    }

    public List<AvisDTO> getNotationsDTO() {
        return notationsDTO;
    }

    public void setNotationsDTO(List<AvisDTO> notationsDTO) {
        this.notationsDTO = notationsDTO;
    }

    public Boolean getIsVerifier() {
        return isVerifier;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerifier = isVerified;
    }

    public Double getMoyenneAvis() {
        return moyenneAvis;
    }

    public void setMoyenneAvis(Double moyenneAvis) {
        this.moyenneAvis = moyenneAvis;
    }
    

    public Integer getNbAnnonce() {
        return nbAnnonce;
    }

    public void setNbAnnonce(Integer nbAnnonce) {
        this.nbAnnonce = nbAnnonce;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntrepriseDTO{");
        sb.append("siret='").append(siret).append('\'');
        sb.append(", categoriesMetier=").append(categoriesMetier);
        sb.append(", nomComplet='").append(nomComplet).append('\'');
        sb.append(", statutJuridique=").append(statutJuridique);
        sb.append(", nbEmployees=").append(nbEmployees);
        sb.append(", logo='").append(logo).append('\'');
        sb.append(", dateCreation=").append(dateCreation);
        sb.append(", specialite='").append(specialite).append('\'');
        sb.append(", artisan=").append(artisan);
        sb.append(", adresseEntreprise=").append(adresseEntreprise);
        sb.append(", notationsDTO=").append(notationsDTO);
        sb.append(", isVerified=").append(isVerifier);
        sb.append('}');
        return sb.toString();
    }

    public static EntrepriseDTO copy(EntrepriseDTO entrepriseSource) {
        ModelMapper mapper = new ModelMapper();
        return  mapper.map(entrepriseSource, EntrepriseDTO.class);
    }
}