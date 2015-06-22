package fr.batimen.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Casaucau on 21/06/2015.
 */
public class LocalisationDTO implements Serializable{

    private String codePostal;

    private String departement;

    private String commune;

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalisationDTO that = (LocalisationDTO) o;
        return Objects.equals(codePostal, that.codePostal) &&
                Objects.equals(departement, that.departement) &&
                Objects.equals(commune, that.commune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codePostal, departement, commune);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalisationDTO{");
        sb.append("codePostal='").append(codePostal).append('\'');
        sb.append(", departement='").append(departement).append('\'');
        sb.append(", commune='").append(commune).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
