package fr.batimen.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class SousCategorieMetierDTO implements Serializable {

    private static final long serialVersionUID = -4392232811368773379L;

    @NotNull
    private String name;

    public SousCategorieMetierDTO(String name) {
        this.name = name;
    }

    public SousCategorieMetierDTO() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.name));
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

        if (object instanceof SousCategorieMetierDTO) {
            SousCategorieMetierDTO other = (SousCategorieMetierDTO) object;
            return Objects.equals(this.name, other.name);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

}
