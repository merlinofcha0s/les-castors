package fr.batimen.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class CategorieMetierDTO implements Serializable {

    private static final long serialVersionUID = -4989158878771801257L;

    @NotNull
    private Short categorieMetier;


    public CategorieMetierDTO() {
        super();
    }

    public CategorieMetierDTO(String name, short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    public CategorieMetierDTO(short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    public Short getCategorieMetier() {
        return categorieMetier;
    }

    public void setCategorieMetier(Short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorieMetierDTO that = (CategorieMetierDTO) o;
        return Objects.equals(categorieMetier, that.categorieMetier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categorieMetier);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategorieMetierDTO{");
        sb.append("categorieMetier=").append(categorieMetier);
        sb.append('}');
        return sb.toString();
    }
}
