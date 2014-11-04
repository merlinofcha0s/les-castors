package fr.batimen.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CategorieMetierDTO implements Serializable {

    private static final long serialVersionUID = -4989158878771801257L;

    @NotNull
    private String name;

    @NotNull
    private Short codeCategorieMetier;

    @Valid
    private List<SousCategorieMetierDTO> sousCategories = new ArrayList<SousCategorieMetierDTO>();

    public CategorieMetierDTO() {
    }

    public CategorieMetierDTO(String name, short codeCategorieMetier) {
        this.name = name;
        this.codeCategorieMetier = codeCategorieMetier;
    }

    public void addSousCategorie(SousCategorieMetierDTO sousCategorie) {
        sousCategories.add(sousCategorie);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sousCategories
     */
    public List<SousCategorieMetierDTO> getSousCategories() {
        return sousCategories;
    }

    /**
     * @return the codeCategorieMetier
     */
    public Short getCodeCategorieMetier() {
        return codeCategorieMetier;
    }

    /**
     * @param codeCategorieMetier
     *            the codeCategorieMetier to set
     */
    public void setCodeCategorieMetier(Short codeCategorieMetier) {
        this.codeCategorieMetier = codeCategorieMetier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.name, this.codeCategorieMetier));
    }

    /**
     * @param sousCategories
     *            the sousCategories to set
     */
    public void setSousCategories(List<SousCategorieMetierDTO> sousCategories) {
        this.sousCategories = sousCategories;
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

        if (object instanceof CategorieMetierDTO) {
            CategorieMetierDTO other = (CategorieMetierDTO) object;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.codeCategorieMetier, other.codeCategorieMetier);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

}
