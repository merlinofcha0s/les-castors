package fr.castor.dto.aggregate;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.AdresseDTO;
import fr.castor.dto.AnnonceDTO;
import fr.castor.dto.constant.ValidatorConstant;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Objet d'echange permettant la modification d'une annonce.
 *
 * Created by Casaucau on 22/04/2015.
 *
 * @author Casaucau Cyril
 */
public class ModificationAnnonceDTO extends AbstractDTO {

    @NotNull
    @Valid
    private AnnonceDTO annonce;
    @NotNull
    @Valid
    private AdresseDTO adresse;
    @NotNull
    @Size(min = ValidatorConstant.CLIENT_LOGIN_RANGE_MIN, max = ValidatorConstant.CLIENT_LOGIN_RANGE_MAX)
    private String loginDemandeur;

    public AnnonceDTO getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceDTO annonce) {
        this.annonce = annonce;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    public String getLoginDemandeur() {
        return loginDemandeur;
    }

    public void setLoginDemandeur(String loginDemandeur) {
        this.loginDemandeur = loginDemandeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModificationAnnonceDTO that = (ModificationAnnonceDTO) o;
        return Objects.equals(annonce, that.annonce) &&
                Objects.equals(adresse, that.adresse) &&
                Objects.equals(loginDemandeur, that.loginDemandeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annonce, adresse, loginDemandeur);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModificationAnnonceDTO{");
        sb.append("annonce=").append(annonce);
        sb.append(", adresse=").append(adresse);
        sb.append(", loginDemandeur='").append(loginDemandeur).append('\'');
        sb.append('}');
        return sb.toString();
    }
}