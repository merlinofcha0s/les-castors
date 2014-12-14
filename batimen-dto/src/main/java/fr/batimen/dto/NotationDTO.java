package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.NOTATION_MAX_COMMENTAIRE;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.helper.DeserializeJsonHelper;

public class NotationDTO extends AbstractDTO {

    private static final long serialVersionUID = 2210249579834795935L;
    @NotNull
    private Double score;
    @NotNull
    @Size(max = NOTATION_MAX_COMMENTAIRE)
    private String commentaire;
    @NotNull
    private String nomEntreprise;

    /**
     * @return the score
     */
    public Double getScore() {
        return score;
    }

    /**
     * @return the commentaire
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * @param score
     *            the score to set
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * @param commentaire
     *            the commentaire to set
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * @return the nomEntreprise
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * @param nomEntreprise
     *            the nomEntreprise to set
     */
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.score, this.commentaire, this.nomEntreprise));
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

        if (object instanceof NotationDTO) {
            NotationDTO other = (NotationDTO) object;
            return Objects.equals(this.score, other.score) && Objects.equals(this.commentaire, other.commentaire)
                    && Objects.equals(this.nomEntreprise, other.nomEntreprise);
        }
        return false;
    }

    public static NotationDTO deserializeNotationDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, NotationDTO.class);
    }

    public static List<NotationDTO> deserializeNotationDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<NotationDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
