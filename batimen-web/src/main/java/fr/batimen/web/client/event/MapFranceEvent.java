package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class MapFranceEvent extends AbstractEvent {

    private String departement;

    public MapFranceEvent(AjaxRequestTarget target) {
        super(target);
    }

    /**
     * @return the departement
     */
    public String getDepartement() {
        return departement;
    }

    /**
     * @param departement
     *            the departement to set
     */
    public void setDepartement(String departement) {
        this.departement = departement;
    }

}
