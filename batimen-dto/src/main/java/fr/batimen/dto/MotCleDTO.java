package fr.batimen.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Casaucau on 26/08/2015.
 */
public class MotCleDTO extends AbstractDTO {

    private String motCle;

    private List<Integer> categories = new ArrayList<>();

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotCleDTO motCleDTO = (MotCleDTO) o;
        return Objects.equals(motCle, motCleDTO.motCle) &&
                Objects.equals(categories, motCleDTO.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motCle, categories);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MotCleDTO{");
        sb.append("motCle='").append(motCle).append('\'');
        sb.append(", categories=").append(categories);
        sb.append('}');
        return sb.toString();
    }
}
