package fr.castor.dto.aggregate;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.NotificationDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MesAnnoncesNotificationDTO extends AbstractDTO {

    @Valid
    private List<NotificationDTO> notifications = new ArrayList<NotificationDTO>();

    @NotNull
    private Long nbTotalNotifications;

    /**
     * @return the notifications
     */
    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public Long getNbTotalNotifications() {
        return nbTotalNotifications;
    }

    public void setNbTotalNotifications(Long nbTotalNotifications) {
        this.nbTotalNotifications = nbTotalNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MesAnnoncesNotificationDTO that = (MesAnnoncesNotificationDTO) o;
        return Objects.equals(notifications, that.notifications) &&
                Objects.equals(nbTotalNotifications, that.nbTotalNotifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notifications, nbTotalNotifications);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MesAnnoncesNotificationDTO{");
        sb.append("notifications=").append(notifications);
        sb.append(", nbTotalNotifications=").append(nbTotalNotifications);
        sb.append('}');
        return sb.toString();
    }
}