package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.NotationDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class MonProfilDTO extends AbstractDTO {

    private static final long serialVersionUID = -3221651801848840575L;

    private String nomPrenomLogin;
    private Long nbAnnonce;
    private List<NotationDTO> notations = new ArrayList<NotationDTO>();

    /**
     * @return the nomPrenomLogin
     */
    public String getNomPrenomLogin() {
        return nomPrenomLogin;
    }

    /**
     * @return the nbAnnonce
     */
    public Long getNbAnnonce() {
        return nbAnnonce;
    }

    /**
     * @return the notations
     */
    public List<NotationDTO> getNotations() {
        return notations;
    }

    /**
     * @param nomPrenomLogin
     *            the nomPrenomLogin to set
     */
    public void setNomPrenomLogin(String nomPrenomLogin) {
        this.nomPrenomLogin = nomPrenomLogin;
    }

    /**
     * @param nbAnnonce
     *            the nbAnnonce to set
     */
    public void setNbAnnonce(Long nbAnnonce) {
        this.nbAnnonce = nbAnnonce;
    }

    /**
     * @param notations
     *            the notations to set
     */
    public void setNotations(List<NotationDTO> notations) {
        this.notations = notations;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.nomPrenomLogin, this.nbAnnonce));
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

        if (object instanceof MonProfilDTO) {
            MonProfilDTO other = (MonProfilDTO) object;
            return Objects.equals(this.nomPrenomLogin, other.nomPrenomLogin)
                    && Objects.equals(this.nbAnnonce, other.nbAnnonce);
        }
        return false;
    }

    public static MonProfilDTO deserializeMonProfilDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, MonProfilDTO.class);
    }

    public static List<MonProfilDTO> deserializeMonProfilDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<NotificationDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
