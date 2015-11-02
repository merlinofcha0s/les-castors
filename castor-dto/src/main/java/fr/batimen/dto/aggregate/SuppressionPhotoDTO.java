package fr.batimen.dto.aggregate;

import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.ImageDTO;

import java.util.Objects;

/**
 * DTO permettant de specifier la suppression d'une photo dans le webservice et dans le cloud.
 *
 * @author Casaucau Cyril
 */
public class SuppressionPhotoDTO extends DemandeAnnonceDTO {

    private ImageDTO imageASupprimer;

    public ImageDTO getImageASupprimer() {
        return imageASupprimer;
    }

    public void setImageASupprimer(ImageDTO imageASupprimer) {
        this.imageASupprimer = imageASupprimer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SuppressionPhotoDTO that = (SuppressionPhotoDTO) o;
        return Objects.equals(imageASupprimer, that.imageASupprimer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imageASupprimer);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuppressionPhotoDTO{");
        sb.append("imageASupprimer=").append(imageASupprimer);
        sb.append('}');
        return sb.toString();
    }
}