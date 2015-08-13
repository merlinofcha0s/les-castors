package fr.batimen.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Objet permettant la persistence et le transfert des données de categorie métier pour une annonce
 *
 * @author Casaucau Cyril
 */
public class CategorieDTO extends AbstractDTO {

    private String motCle;

    private List<Integer> categories = new ArrayList<>();

    public CategorieDTO(String motCle, List<Integer> categories) {
        this.motCle = motCle;
        this.categories = categories;
    }

    public CategorieDTO() {
    }

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
        CategorieDTO that = (CategorieDTO) o;
        return Objects.equals(motCle, that.motCle) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motCle, categories);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategorieDTO{");
        sb.append("motCle='").append(motCle).append('\'');
        sb.append(", categories=").append(categories);
        sb.append('}');
        return sb.toString();
    }
}