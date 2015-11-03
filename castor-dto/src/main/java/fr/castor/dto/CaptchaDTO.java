package fr.castor.dto;

import java.util.Objects;

/**
 * Created by Casaucau on 11/09/2015.
 */
public class CaptchaDTO extends AbstractDTO {

    private String success;

    private String errorCodes;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaptchaDTO that = (CaptchaDTO) o;
        return Objects.equals(success, that.success) &&
                Objects.equals(errorCodes, that.errorCodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, errorCodes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CaptchaDTO{");
        sb.append("success='").append(success).append('\'');
        sb.append(", errorCodes='").append(errorCodes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
