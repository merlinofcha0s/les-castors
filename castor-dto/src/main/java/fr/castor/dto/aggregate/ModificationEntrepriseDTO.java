package fr.castor.dto.aggregate;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.AdresseDTO;
import fr.castor.dto.EntrepriseDTO;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Objet de transport servant a modifier les informations d'une entreprise.
 *
 * @author Casaucau Cyril
 */
public class ModificationEntrepriseDTO extends AbstractDTO {

    @Valid
    private EntrepriseDTO entreprise = new EntrepriseDTO();

    @Valid
    private AdresseDTO adresse = new AdresseDTO();

    public EntrepriseDTO getEntreprise() {
        return entreprise;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setEntreprise(EntrepriseDTO entreprise) {
        this.entreprise = entreprise;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModificationEntrepriseDTO that = (ModificationEntrepriseDTO) o;
        return Objects.equals(entreprise, that.entreprise) &&
                Objects.equals(adresse, that.adresse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entreprise, adresse);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModificationEntrepriseDTO{");
        sb.append("entreprise=").append(entreprise);
        sb.append(", adresse=").append(adresse);
        sb.append('}');
        return sb.toString();
    }
}