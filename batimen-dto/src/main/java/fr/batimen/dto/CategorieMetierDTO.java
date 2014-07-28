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

    @Valid
    private List<SousCategorieMetierDTO> sousCategories = new ArrayList<SousCategorieMetierDTO>();

    public CategorieMetierDTO() {
    }

    public CategorieMetierDTO(String name) {
        this.name = name;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.name));
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
            return Objects.equals(this.name, other.name);
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
