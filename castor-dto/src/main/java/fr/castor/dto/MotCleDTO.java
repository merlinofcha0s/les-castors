package fr.castor.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Casaucau on 26/08/2015.
 */
public class MotCleDTO extends AbstractDTO {

    @NotNull
    private String motCle;

    @Valid
    private List<CategorieMetierDTO> categoriesMetier = new ArrayList<>();

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public List<CategorieMetierDTO> getCategoriesMetier() {
        return categoriesMetier;
    }

    public void setCategoriesMetier(List<CategorieMetierDTO> categoriesMetier) {
        this.categoriesMetier = categoriesMetier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotCleDTO motCleDTO = (MotCleDTO) o;
        return Objects.equals(motCle, motCleDTO.motCle) &&
                Objects.equals(categoriesMetier, motCleDTO.categoriesMetier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motCle, categoriesMetier);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MotCleDTO{");
        sb.append("motCle='").append(motCle).append('\'');
        sb.append(", categories=").append(categoriesMetier);
        sb.append('}');
        return sb.toString();
    }
}
