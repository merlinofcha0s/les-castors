package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class MesAnnoncesPageDTO {

    private List<NotificationDTO> notifications = new ArrayList<NotificationDTO>();
    private List<AnnonceDTO> annonces = new ArrayList<AnnonceDTO>();

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

        if (object instanceof MesAnnoncesPageDTO) {
            MesAnnoncesPageDTO other = (MesAnnoncesPageDTO) object;
            return Objects.equals(this.notifications, other.notifications)
                    && Objects.equals(this.annonces, other.annonces);
        }
        return false;
    }

    public static MesAnnoncesPageDTO deserializeMesDevisDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, MesAnnoncesPageDTO.class);
    }

    public static List<MesAnnoncesPageDTO> deserializeMesDevisDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<MesAnnoncesPageDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
