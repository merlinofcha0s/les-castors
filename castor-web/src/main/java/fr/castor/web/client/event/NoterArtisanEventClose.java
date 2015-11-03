package fr.castor.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Event de fermeture de la popup permettant de noter l'artisan
 * 
 * @author Casaucau Cyril
 * 
 */
public class NoterArtisanEventClose extends AbstractEvent {

    private Double nbEtoiles;
    private String commentaireNotation;

    public NoterArtisanEventClose(AjaxRequestTarget target) {
        super(target);
    }

    public NoterArtisanEventClose(AjaxRequestTarget target, Double nbEtoiles, String commentaireNotation) {
        this(target);

        this.nbEtoiles = nbEtoiles;
        this.commentaireNotation = commentaireNotation;
    }

    /**
     * @return the nbEtoiles
     */
    public Double getNbEtoiles() {
        return nbEtoiles;
    }

    /**
     * @return the commentaireNotation
     */
    public String getCommentaireNotation() {
        return commentaireNotation;
    }

    /**
     * @param nbEtoiles
     *            the nbEtoiles to set
     */
    public void setNbEtoiles(Double nbEtoiles) {
        this.nbEtoiles = nbEtoiles;
    }

    /**
     * @param commentaireNotation
     *            the commentaireNotation to set
     */
    public void setCommentaireNotation(String commentaireNotation) {
        this.commentaireNotation = commentaireNotation;
    }

}
