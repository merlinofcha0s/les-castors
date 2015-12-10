package fr.castor.web.client.component.breadcrumb;

import fr.castor.web.client.component.LinkLabel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Objet symbolisant une étape dans le breadcrumb
 *
 * @author Casaucau Cyril
 */
public class BreadCrumbLink implements Serializable {

    private LinkLabel link;

    private boolean isActive;

    public BreadCrumbLink(LinkLabel link, boolean isActive) {
        this.link = link;
        this.isActive = isActive;
    }

    public LinkLabel getLink() {
        return link;
    }

    public void setLink(LinkLabel link) {
        this.link = link;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreadCrumbLink that = (BreadCrumbLink) o;
        return isActive == that.isActive &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, isActive);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BreadCrumbLink{");
        sb.append("link=").append(link);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
