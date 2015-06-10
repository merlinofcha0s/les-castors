package fr.batimen.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.NOTATION_MAX_COMMENTAIRE;
import static fr.batimen.dto.constant.ValidatorConstant.NOTATION_MIN_COMMENTAIRE;

public class NotationDTO extends AbstractDTO {

    private static final long serialVersionUID = 2210249579834795935L;

    @NotNull
    private Double score;
    @NotNull
    @Size(min = NOTATION_MIN_COMMENTAIRE, max = NOTATION_MAX_COMMENTAIRE)
    private String commentaire;
    @NotNull
    private String nomEntreprise;
    @NotNull
    private String nomPrenomOrLoginClient;
    private Date dateNotation;

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

    public String getNomPrenomOrLoginClient() {
        return nomPrenomOrLoginClient;
    }

    public void setNomPrenomOrLoginClient(String nomPrenomOrLoginClient) {
        this.nomPrenomOrLoginClient = nomPrenomOrLoginClient;
    }

    public Date getDateNotation() {
        return dateNotation;
    }

    public void setDateNotation(Date dateNotation) {
        this.dateNotation = dateNotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotationDTO that = (NotationDTO) o;
        return Objects.equals(score, that.score) &&
                Objects.equals(commentaire, that.commentaire) &&
                Objects.equals(nomEntreprise, that.nomEntreprise) &&
                Objects.equals(nomPrenomOrLoginClient, that.nomPrenomOrLoginClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, commentaire, nomEntreprise, nomPrenomOrLoginClient);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotationDTO{");
        sb.append("score=").append(score);
        sb.append(", commentaire='").append(commentaire).append('\'');
        sb.append(", nomEntreprise='").append(nomEntreprise).append('\'');
        sb.append(", nomPrenomOrLoginClient='").append(nomPrenomOrLoginClient).append('\'');
        sb.append('}');
        return sb.toString();
    }
}