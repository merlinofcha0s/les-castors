package fr.batimen.ws.entity;

import fr.batimen.core.constant.QueryJPQL;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entité Image, est utilisée pour symboliser une image d'une annonce
 *
 * @author Casaucau Cyril
 */
@Entity
@Table(name = "Image")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.IMAGE_BY_HASH_ID_AND_LOGIN_CLIENT,
                query = "SELECT i FROM Image AS i WHERE i.annonce.hashID = :hashID AND i.annonce.demandeur.login = :login"),
        @NamedQuery(name = QueryJPQL.IMAGE_BY_HASH_ID,
        query = "SELECT i FROM Image AS i WHERE i.annonce.hashID = :hashID")})
public class Image extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 8005599117537733385L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_annonce")
    private Annonce annonce;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the annonce
     */
    public Annonce getAnnonce() {
        return annonce;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param annonce the annonce to set
     */
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.url, this.url));
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

        if (object instanceof Image) {
            Image other = (Image) object;
            return Objects.equals(this.url, other.url);
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Image{");
        sb.append("id=").append(id);
        sb.append(", url='").append(url).append('\'');
        sb.append(", annonce=").append(annonce);
        sb.append('}');
        return sb.toString();
    }
}
