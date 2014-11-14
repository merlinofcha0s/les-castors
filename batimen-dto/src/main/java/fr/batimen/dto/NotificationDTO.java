package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class NotificationDTO extends AbstractDTO {

    private static final long serialVersionUID = 5544266173747817936L;

    @NotNull
    private TypeNotification typeNotification;
    @Past
    @NotNull
    private Date dateNotification;
    @NotNull
    private StatutNotification statutNotification;
    @NotNull
    private TypeCompte pourQuiNotification;

    /**
     * @return the typeNotification
     */
    public TypeNotification getTypeNotification() {
        return typeNotification;
    }

    /**
     * @param typeNotification
     *            the typeNotification to set
     */
    public void setTypeNotification(TypeNotification typeNotification) {
        this.typeNotification = typeNotification;
    }

    /**
     * @return the dateNotification
     */
    public Date getDateNotification() {
        return dateNotification;
    }

    /**
     * @param dateNotification
     *            the dateNotification to set
     */
    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }

    /**
     * @return the statutNotification
     */
    public StatutNotification getStatutNotification() {
        return statutNotification;
    }

    /**
     * @return the pourQuiNotification
     */
    public TypeCompte getPourQuiNotification() {
        return pourQuiNotification;
    }

    /**
     * @param statutNotification
     *            the statutNotification to set
     */
    public void setStatutNotification(StatutNotification statutNotification) {
        this.statutNotification = statutNotification;
    }

    /**
     * @param pourQuiNotification
     *            the pourQuiNotification to set
     */
    public void setPourQuiNotification(TypeCompte pourQuiNotification) {
        this.pourQuiNotification = pourQuiNotification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.typeNotification, this.dateNotification, this.statutNotification,
                this.pourQuiNotification));
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
            return Objects.equals(this.typeNotification, other.typeNotification)
                    && Objects.equals(this.dateNotification, other.dateNotification)
                    && Objects.equals(this.statutNotification, other.statutNotification)
                    && Objects.equals(this.pourQuiNotification, other.pourQuiNotification);
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
