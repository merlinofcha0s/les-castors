package fr.castor.dto.aggregate;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.AnnonceDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MesAnnoncesAnnonceDTO extends AbstractDTO {

    @Valid
    private List<AnnonceDTO> annonces = new ArrayList<AnnonceDTO>();

    @NotNull
    private Long nbTotalAnnonces;

    public Long getNbTotalAnnonces() {
        return nbTotalAnnonces;
    }

    public void setNbTotalAnnonces(Long nbTotalAnnonces) {
        this.nbTotalAnnonces = nbTotalAnnonces;
    }

    /**
     * @return the annonces
     */
    public List<AnnonceDTO> getAnnonces() {
        return annonces;
    }

    /**
     * @param annonces
     *            the annonces to set
     */
    public void setAnnonces(List<AnnonceDTO> annonces) {
        this.annonces = annonces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MesAnnoncesAnnonceDTO that = (MesAnnoncesAnnonceDTO) o;
        return Objects.equals(annonces, that.annonces) &&
                Objects.equals(nbTotalAnnonces, that.nbTotalAnnonces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annonces, nbTotalAnnonces);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MesAnnoncesAnnonceDTO{");
        sb.append("annonces=").append(annonces);
        sb.append(", nbTotalAnnonces=").append(nbTotalAnnonces);
        sb.append('}');
        return sb.toString();
    }
}