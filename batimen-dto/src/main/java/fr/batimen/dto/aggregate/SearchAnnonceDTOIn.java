package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.CategorieMetierDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.*;

/**
 * DTO permettant de rechercher des annonces
 *
 * @author Casaucau Cyril
 */
public class SearchAnnonceDTOIn extends AbstractDTO {

    @Valid
    private List<CategorieMetierDTO> categoriesMetierDTO = new LinkedList<>();

    @NotNull
    @Past
    private Date aPartirdu;

    @NotNull
    @Min(value = DEPARTEMENT_MIN)
    @Max(value = DEPARTEMENT_MAX)
    private Integer departement;

    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String loginDemandeur;

    @NotNull
    private Integer rangeDebut;
    @NotNull
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

    public void clear(){
        aPartirdu = new Date();
        categoriesMetierDTO.clear();
        departement = 0;
        rangeDebut = 0;
        rangeFin = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchAnnonceDTOIn that = (SearchAnnonceDTOIn) o;
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
        final StringBuilder sb = new StringBuilder("SearchAnnonceDTOIn{");
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