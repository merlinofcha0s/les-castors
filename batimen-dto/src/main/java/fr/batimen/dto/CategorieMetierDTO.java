package fr.batimen.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategorieMetierDTO implements Serializable {

    private static final long serialVersionUID = -4989158878771801257L;

    @NotNull
    private String name;

    @NotNull
    private Short categorieMetier;

    @Valid
    private List<SousCategorieMetierDTO> sousCategories = new ArrayList<SousCategorieMetierDTO>();

    public CategorieMetierDTO() {
    }

    public CategorieMetierDTO(String name, short categorieMetier) {
        this.name = name;
        this.categorieMetier = categorieMetier;
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
     * @param sousCategories
     *            the sousCategories to set
     */
    public void setSousCategories(List<SousCategorieMetierDTO> sousCategories) {
        this.sousCategories = sousCategories;
    }

    /**
     * @return the codeCategorieMetier
     */
    public Short getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @param categorieMetier
     *            the codeCategorieMetier to set
     */
    public void setCategorieMetier(Short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.name, this.categorieMetier));
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
                    && Objects.equals(this.categorieMetier, other.categorieMetier);
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
