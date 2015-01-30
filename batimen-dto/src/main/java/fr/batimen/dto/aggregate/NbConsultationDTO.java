package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class NbConsultationDTO extends AbstractDTO {

    private static final long serialVersionUID = 835331376231650298L;

    @NotNull
    private String hashID;
    @NotNull
    private Integer nbConsultation;

    /**
     * @return the hashID
     */
    public String getHashID() {
        return hashID;
    }

    /**
     * @return the nbConsultation
     */
    public Integer getNbConsultation() {
        return nbConsultation;
    }

    /**
     * @param hashID
     *            the hashID to set
     */
    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    /**
     * @param nbConsultation
     *            the nbConsultation to set
     */
    public void setNbConsultation(Integer nbConsultation) {
        this.nbConsultation = nbConsultation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.nbConsultation, this.hashID));
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

        if (object instanceof NbConsultationDTO) {
            NbConsultationDTO other = (NbConsultationDTO) object;
            return Objects.equals(this.hashID, other.hashID)
                    && Objects.equals(this.nbConsultation, other.nbConsultation);
        }
        return false;
    }

    public static NbConsultationDTO deserializeNbConsultationDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, NbConsultationDTO.class);
    }

    public static List<NbConsultationDTO> deserializeNbConsultationDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<NbConsultationDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
