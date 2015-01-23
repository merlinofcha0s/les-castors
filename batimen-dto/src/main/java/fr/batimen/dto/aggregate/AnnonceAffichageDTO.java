package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

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

    public static AnnonceAffichageDTO deserializeAnnonceAffichageDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, AnnonceAffichageDTO.class);
    }

    public static List<AnnonceAffichageDTO> deserializeAnnonceAffichageDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<AnnonceAffichageDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
