package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class CastorWizardEvent extends AbstractEvent {

    private String stepNumber;

    public CastorWizardEvent(AjaxRequestTarget target) {
        super(target);
    }

    /**
     * @return the stepNumber
     */
    public String getStepNumber() {
        return stepNumber;
    }

    /**
     * @param stepNumber
     *            the stepNumber to set
     */
    public void setStepNumber(String stepNumber) {
        this.stepNumber = stepNumber;
    }

}
