package fr.batimen.dto;

import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.ANNONCE_DESCRIPTION_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.ANNONCE_DESCRIPTION_MIN;

/**
 * DTO de l'annonce
 * 
 * @author Casaucau Cyril
 * 
 */
public class AnnonceDTO extends AbstractDTO {

    private static final long serialVersionUID = -647384595187488520L;

    @NotNull
    @Size(min = ANNONCE_DESCRIPTION_MIN, max = ANNONCE_DESCRIPTION_MAX)
    private String description;
    @NotNull
    private TypeContact typeContact;
    private DelaiIntervention delaiIntervention;
    @NotNull
    private Date dateCreation;
    @NotNull
    private Date dateMAJ;
    private Integer nbConsultation;
    @NotNull
    private Short categorieMetier;
    @NotNull
    private String sousCategorieMetier;
    @NotNull
    private EtatAnnonce etatAnnonce;
    private Long nbDevis;
    @NotNull
    private TypeTravaux typeTravaux;
    private String hashID;

    // Rempli que pour l'affichage
    private String loginOwner;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the hashID
     */
    public String getHashID() {
        return hashID;
    }

    /**
     * @param hashID
     *            the hashID to set
     */
    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    /**
     * @return the typeContact
     */
    public TypeContact getTypeContact() {
        return typeContact;
    }

    /**
     * @param typeContact
     *            the typeContact to set
     */
    public void setTypeContact(TypeContact typeContact) {
        this.typeContact = typeContact;
    }

    /**
     * @return the delaiIntervention
     */
    public DelaiIntervention getDelaiIntervention() {
        return delaiIntervention;
    }

    /**
     * @param delaiIntervention
     *            the delaiIntervention to set
     */
    public void setDelaiIntervention(DelaiIntervention delaiIntervention) {
        this.delaiIntervention = delaiIntervention;
    }

    /**
     * @return the dateCreation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @return the dateMAJ
     */
    public Date getDateMAJ() {
        return dateMAJ;
    }

    /**
     * @param dateMAJ
     *            the dateMAJ to set
     */
    public void setDateMAJ(Date dateMAJ) {
        this.dateMAJ = dateMAJ;
    }

    /**
     * @return the nbConsultation
     */
    public Integer getNbConsultation() {
        return nbConsultation;
    }

    /**
     * @param nbConsultation
     *            the nbConsultation to set
     */
    public void setNbConsultation(Integer nbConsultation) {
        this.nbConsultation = nbConsultation;
    }

    /**
     * @return the categorieMetier
     */
    public Short getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(Short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    /**
     * @return the sousCategorieMetier
     */
    public String getSousCategorieMetier() {
        return sousCategorieMetier;
    }

    /**
     * @param sousCategorieMetier
     *            the sousCategorieMetier to set
     */
    public void setSousCategorieMetier(String sousCategorieMetier) {
        this.sousCategorieMetier = sousCategorieMetier;
    }

    /**
     * @return the etatAnnonce
     */
    public EtatAnnonce getEtatAnnonce() {
        return etatAnnonce;
    }

    /**
     * @param etatAnnonce
     *            the etatAnnonce to set
     */
    public void setEtatAnnonce(EtatAnnonce etatAnnonce) {
        this.etatAnnonce = etatAnnonce;
    }

    /**
     * @return the nbDevis
     */
    public Long getNbDevis() {
        return nbDevis;
    }

    /**
     * @param nbDevis
     *            the nbDevis to set
     */
    public void setNbDevis(Long nbDevis) {
        this.nbDevis = nbDevis;
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

    /**
     * @return the loginOwner
     */
    public String getLoginOwner() {
        return loginOwner;
    }

    /**
     * @param loginOwner
     *            the loginOwner to set
     */
    public void setLoginOwner(String loginOwner) {
        this.loginOwner = loginOwner;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.typeContact, this.delaiIntervention, this.dateCreation, this.hashID,
                this.description));
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

        if (object instanceof AnnonceDTO) {
            AnnonceDTO other = (AnnonceDTO) object;
            return Objects.equals(this.typeContact, other.typeContact) && Objects.equals(this.hashID, other.hashID)
                    && Objects.equals(this.delaiIntervention, other.delaiIntervention)
                    && Objects.equals(this.dateCreation, other.dateCreation)
                    && Objects.equals(this.description, other.description);
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnnonceDTO{");
        sb.append("description='").append(description).append('\'');
        sb.append(", typeContact=").append(typeContact);
        sb.append(", delaiIntervention=").append(delaiIntervention);
        sb.append(", dateCreation=").append(dateCreation);
        sb.append(", dateMAJ=").append(dateMAJ);
        sb.append(", nbConsultation=").append(nbConsultation);
        sb.append(", categorieMetier=").append(categorieMetier);
        sb.append(", sousCategorieMetier='").append(sousCategorieMetier).append('\'');
        sb.append(", etatAnnonce=").append(etatAnnonce);
        sb.append(", nbDevis=").append(nbDevis);
        sb.append(", typeTravaux=").append(typeTravaux);
        sb.append(", hashID='").append(hashID).append('\'');
        sb.append(", loginOwner='").append(loginOwner).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
