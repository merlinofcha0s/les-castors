package fr.castor.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * DTO pour le transfert des images
 * 
 * @author Casaucau Cyril
 * 
 */
public class ImageDTO extends AbstractDTO {

    private static final long serialVersionUID = 9211085039373280654L;

    @NotNull
    private String url;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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

        if (object instanceof ImageDTO) {
            ImageDTO other = (ImageDTO) object;
            return Objects.equals(this.url, other.getUrl());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImageDTO{");
        sb.append("url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}