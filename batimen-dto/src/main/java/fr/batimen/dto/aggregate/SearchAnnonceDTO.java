package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.CategorieMetierDTO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * DTO permettant de rechercher des annonces
 *
 * @author Casaucau Cyril
 */
public class SearchAnnonceDTO extends AbstractDTO {

    private List<CategorieMetierDTO> categoriesMetierDTO = new LinkedList<>();

    private Date aPartirdu;

    private Integer departement;

    private String loginDemandeur;

    private Integer rangeDebut;
    private Integer rangeFin;

    public List<CategorieMetierDTO> getCategoriesMetierDTO() {
        return categoriesMetierDTO;
    }

    public void setCategoriesMetierDTO(List<CategorieMetierDTO> categoriesMetierDTO) {
        this.categoriesMetierDTO = categoriesMetierDTO;
    }

    public Date getaPartirdu() {
        return aPartirdu;
    }

    public void setaPartirdu(Date aPartirdu) {
        this.aPartirdu = aPartirdu;
    }

    public Integer getDepartement() {
        return departement;
    }

    public void setDepartement(Integer departement) {
        this.departement = departement;
    }

    public String getLoginDemandeur() {
        return loginDemandeur;
    }

    public void setLoginDemandeur(String loginDemandeur) {
        this.loginDemandeur = loginDemandeur;
    }

    public Integer getRangeDebut() {
        return rangeDebut;
    }

    public void setRangeDebut(Integer rangeDebut) {
        this.rangeDebut = rangeDebut;
    }

    public Integer getRangeFin() {
        return rangeFin;
    }

    public void setRangeFin(Integer rangeFin) {
        this.rangeFin = rangeFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchAnnonceDTO that = (SearchAnnonceDTO) o;
        return Objects.equals(categoriesMetierDTO, that.categoriesMetierDTO) &&
                Objects.equals(aPartirdu, that.aPartirdu) &&
                Objects.equals(departement, that.departement) &&
                Objects.equals(loginDemandeur, that.loginDemandeur) &&
                Objects.equals(rangeDebut, that.rangeDebut) &&
                Objects.equals(rangeFin, that.rangeFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriesMetierDTO, aPartirdu, departement, loginDemandeur, rangeDebut, rangeFin);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchAnnonceDTO{");
        sb.append("categoriesMetierDTO=").append(categoriesMetierDTO);
        sb.append(", aPartirdu=").append(aPartirdu);
        sb.append(", departement=").append(departement);
        sb.append(", loginDemandeur='").append(loginDemandeur).append('\'');
        sb.append(", rangeDebut=").append(rangeDebut);
        sb.append(", rangeFin=").append(rangeFin);
        sb.append('}');
        return sb.toString();
    }
}
