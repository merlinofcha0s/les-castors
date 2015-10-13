package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MesAnnoncesDTO extends AbstractDTO {

    private static final long serialVersionUID = -8241631062405568121L;
    @Valid
    private List<NotificationDTO> notifications = new ArrayList<NotificationDTO>();
    @Valid
    private List<AnnonceDTO> annonces = new ArrayList<AnnonceDTO>();

    @NotNull
    private Long nbTotalAnnonces;

    public Long getNbTotalAnnonces() {
        return nbTotalAnnonces;
    }

    public void setNbTotalAnnonces(Long nbTotalAnnonces) {
        this.nbTotalAnnonces = nbTotalAnnonces;
    }

    /**
     * @return the notifications
     */
    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications
     *            the notifications to set
     */
    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the annonces
     */
    public List<AnnonceDTO> getAnnonces() {
        return annonces;
    }

    /**
     * @param annonces
     *            the annonces to set
     */
    public void setAnnonces(List<AnnonceDTO> annonces) {
        this.annonces = annonces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.annonces, this.notifications));
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

        if (object instanceof MesAnnoncesDTO) {
            MesAnnoncesDTO other = (MesAnnoncesDTO) object;
            return Objects.equals(this.notifications, other.notifications)
                    && Objects.equals(this.annonces, other.annonces);
        }
        return false;
    }
}