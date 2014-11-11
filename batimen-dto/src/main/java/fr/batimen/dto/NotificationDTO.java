package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class NotificationDTO extends AbstractDTO {

    private static final long serialVersionUID = 5544266173747817936L;

    @NotNull
    private TypeNotification notification;
    @Past
    @NotNull
    private Date dateNotification;

    /**
     * @return the notification
     */
    public TypeNotification getNotification() {
        return notification;
    }

    /**
     * @return the dateNotification
     */
    public Date getDateNotification() {
        return dateNotification;
    }

    /**
     * @param notification
     *            the notification to set
     */
    public void setNotification(TypeNotification notification) {
        this.notification = notification;
    }

    /**
     * @param dateNotification
     *            the dateNotification to set
     */
    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.notification, this.dateNotification));
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

        if (object instanceof NotificationDTO) {
            NotificationDTO other = (NotificationDTO) object;
            return Objects.equals(this.notification, other.notification)
                    && Objects.equals(this.dateNotification, other.dateNotification);
        }
        return false;
    }

    public static NotificationDTO deserializeLoginDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, NotificationDTO.class);
    }

    public static List<NotificationDTO> deserializeLoginDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<NotificationDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
