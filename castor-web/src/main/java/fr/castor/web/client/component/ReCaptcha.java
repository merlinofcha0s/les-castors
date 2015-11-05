package fr.castor.web.client.component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import fr.castor.dto.CaptchaDTO;
import fr.castor.dto.helper.DeserializeJsonHelper;
import fr.castor.web.app.enums.PropertiesFileWeb;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by Casaucau on 11/09/2015.
 */
public class ReCaptcha extends Panel {

    private final String secretKey = "6LfRUAwTAAAAAGpwEyadz8Ku5-tWmzRoNzQvSjEK";

    public ReCaptcha(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forUrl("https://www.google.com/recaptcha/api.js"));
    }

    public CaptchaDTO verifyCaptcha() {
        if (Boolean.valueOf(PropertiesFileWeb.APP.getProperties().getProperty("app.activate.captcha"))) {
            final WebRequest request = (WebRequest) RequestCycle.get().getRequest();
            final String userToken = request.getRequestParameters().getParameterValue("g-recaptcha-response").toString();

            MultivaluedMap<String, String> captchaVerificationForm = new MultivaluedMapImpl();
            captchaVerificationForm.add("secret", secretKey);
            captchaVerificationForm.add("response", userToken);

            ClientConfig clientConfig = new DefaultClientConfig();
            Client client = Client.create(clientConfig);
            client.setFollowRedirects(true);
            WebResource call = client.resource("https://www.google.com/recaptcha/api/siteverify");
            String response =
                    call.type(MediaType.
                            APPLICATION_FORM_URLENCODED_TYPE)
                            .post(String.class, captchaVerificationForm);

            return DeserializeJsonHelper.deserializeDTO(response, CaptchaDTO.class);
        } else {
            return new CaptchaDTO();
        }
    }
}