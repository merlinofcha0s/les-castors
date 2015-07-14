package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.CategorieMetierDTO;

import java.util.Date;
import java.util.Objects;

/**
 * DTO permettant de rechercher des annonces
 *
 * @author Casaucau Cyril
 */
public class SearchAnnonceDTO extends AbstractDTO {

    private CategorieMetierDTO categorieMetierDTO;

    private Date aPartirdu;

    private int departement;

    private String loginDemandeur;

    public CategorieMetierDTO getCategorieMetierDTO() {
        return categorieMetierDTO;
    }

    public void setCategorieMetierDTO(CategorieMetierDTO categorieMetierDTO) {
        this.categorieMetierDTO = categorieMetierDTO;
    }

    public Date getaPartirdu() {
        return aPartirdu;
    }

    public void setaPartirdu(Date aPartirdu) {
        this.aPartirdu = aPartirdu;
    }

    public int getDepartement() {
        return departement;
    }

    public void setDepartement(int departement) {
        this.departement = departement;
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
        SearchAnnonceDTO that = (SearchAnnonceDTO) o;
        return Objects.equals(departement, that.departement) &&
                Objects.equals(categorieMetierDTO, that.categorieMetierDTO) &&
                Objects.equals(aPartirdu, that.aPartirdu) &&
                Objects.equals(loginDemandeur, that.loginDemandeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categorieMetierDTO, aPartirdu, departement, loginDemandeur);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchAnnonceDTO{");
        sb.append("categorieMetierDTO=").append(categorieMetierDTO);
        sb.append(", aPartirdu=").append(aPartirdu);
        sb.append(", departement=").append(departement);
        sb.append(", loginDemandeur='").append(loginDemandeur).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
