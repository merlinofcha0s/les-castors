package fr.batimen.dto.aggregate;

import fr.batimen.dto.DemandeAnnonceDTO;

import java.io.File;
import java.util.*;

/**
 * Objet d'échance servant à ajouter une image
 *
 *
 * @author Casaucau Cyril
 */
public class AjoutPhotoDTO extends DemandeAnnonceDTO {

    private List<File> images = new ArrayList<>();

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AjoutPhotoDTO that = (AjoutPhotoDTO) o;
        return Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), images);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AjoutPhotoDTO{");
        sb.append("hashid=").append(id);
        sb.append("loginDemandeur=").append(loginDemandeur);
        sb.append('}');
        return sb.toString();
    }
}