package fr.castor.dto.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.AdresseDTO;
import fr.castor.dto.AnnonceDTO;
import fr.castor.dto.EntrepriseDTO;
import fr.castor.dto.ImageDTO;

public class AnnonceAffichageDTO extends AbstractDTO {

    private static final long serialVersionUID = 2099389809460398917L;

    @NotNull
    @Valid
    private AnnonceDTO annonce;
    @NotNull
    @Valid
    private AdresseDTO adresse;
    // Entreprise Inscrite avec leurs artisans respectifs
    @Valid
    private final List<EntrepriseDTO> entreprises = new ArrayList<EntrepriseDTO>();
    // Entreprise qui a été choisi pour réaliser les travaux.
    @Valid
    private EntrepriseDTO entrepriseSelectionnee;

    @NotNull
    private Boolean isArtisanInscrit;
    @Size(max = 10)
    private String telephoneClient;
    @Email
    private String emailClient;
    @Valid
    private final List<ImageDTO> images = new ArrayList<ImageDTO>();

    /**
     * @return the annonce
     */
    public AnnonceDTO getAnnonce() {
        return annonce;
    }

    /**
     * @return the adresse
     */
    public AdresseDTO getAdresse() {
        return adresse;
    }

    /**
     * @return the entreprises
     */
    public List<EntrepriseDTO> getEntreprises() {
        return entreprises;
    }

    /**
     * @return the entrepriseSelectionnee
     */
    public EntrepriseDTO getEntrepriseSelectionnee() {
        return entrepriseSelectionnee;
    }

    /**
     * @param annonce
     *            the annonce to set
     */
    public void setAnnonce(AnnonceDTO annonce) {
        this.annonce = annonce;
    }

    /**
     * @param adresse
     *            the adresse to set
     */
    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    /**
     * @param entrepriseSelectionnee
     *            the entrepriseSelectionnee to set
     */
    public void setEntrepriseSelectionnee(EntrepriseDTO entrepriseSelectionnee) {
        this.entrepriseSelectionnee = entrepriseSelectionnee;
    }

    /**
     * @return the isArtisanInscrit
     */
    public Boolean getIsArtisanInscrit() {
        return isArtisanInscrit;
    }

    /**
     * @param isArtisanInscrit
     *            the isArtisanInscrit to set
     */
    public void setIsArtisanInscrit(Boolean isArtisanInscrit) {
        this.isArtisanInscrit = isArtisanInscrit;
    }

    /**
     * @return the telephoneClient
     */
    public String getTelephoneClient() {
        return telephoneClient;
    }

    /**
     * @return the emailClient
     */
    public String getEmailClient() {
        return emailClient;
    }

    /**
     * @param telephoneClient
     *            the telephoneClient to set
     */
    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    /**
     * @param emailClient
     *            the emailClient to set
     */
    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    /**
     * @return the images
     */
    public List<ImageDTO> getImages() {
        return images;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.annonce.getDescription(), this.annonce.getLoginOwner(),
                this.annonce.getDateCreation()));
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

        if (object instanceof AnnonceAffichageDTO) {
            AnnonceAffichageDTO other = (AnnonceAffichageDTO) object;
            return Objects.equals(this.getAnnonce().getDescription(), other.getAnnonce().getDescription())
                    && Objects.equals(this.getAnnonce().getLoginOwner(), other.getAnnonce().getLoginOwner())
                    && Objects.equals(this.getAnnonce().getDateCreation(), other.getAnnonce().getDateCreation());
        }
        return false;
    }
}