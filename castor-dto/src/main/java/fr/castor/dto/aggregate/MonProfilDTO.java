package fr.castor.dto.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.castor.dto.AbstractDTO;
import fr.castor.dto.AvisDTO;

/**
 * Objet de transfert a destination de la page de mon profil (coté client)
 * 
 * @author Casaucau Cyril
 * 
 */
public class MonProfilDTO extends AbstractDTO {

    private static final long serialVersionUID = -3221651801848840575L;

    private String nomPrenomLogin;
    private Long nbAnnonce;
    private List<AvisDTO> notations = new ArrayList<AvisDTO>();

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
    public List<AvisDTO> getNotations() {
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
    public void setNotations(List<AvisDTO> notations) {
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
}