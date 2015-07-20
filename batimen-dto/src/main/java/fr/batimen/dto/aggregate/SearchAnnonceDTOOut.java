package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AnnonceDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Casaucau on 20/07/2015.
 *
 * @author Casaucau Cyril
 */
public class SearchAnnonceDTOOut extends AbstractDTO {

    private List<AnnonceDTO> annonceDTOList = new ArrayList<>();

    private int nbTotalResultat;

    public List<AnnonceDTO> getAnnonceDTOList() {
        return annonceDTOList;
    }

    public void setAnnonceDTOList(List<AnnonceDTO> annonceDTOList) {
        this.annonceDTOList = annonceDTOList;
    }

    public int getNbTotalResultat() {
        return nbTotalResultat;
    }

    public void setNbTotalResultat(int nbTotalResultat) {
        this.nbTotalResultat = nbTotalResultat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchAnnonceDTOOut that = (SearchAnnonceDTOOut) o;
        return Objects.equals(nbTotalResultat, that.nbTotalResultat) &&
                Objects.equals(annonceDTOList, that.annonceDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annonceDTOList, nbTotalResultat);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchAnnonceDTOOut{");
        sb.append("annonceDTOList=").append(annonceDTOList);
        sb.append(", nbTotalResultat=").append(nbTotalResultat);
        sb.append('}');
        return sb.toString();
    }
}
