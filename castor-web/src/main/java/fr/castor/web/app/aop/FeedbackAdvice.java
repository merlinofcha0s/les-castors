package fr.castor.web.app.aop;

import fr.castor.web.client.extend.nouveau.communs.JSCommun;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by Casaucau on 02/12/2015.
 */
@Aspect
public class FeedbackAdvice {

    @Before(value = "execution(* org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink.onError(org.apache.wicket.ajax.AjaxRequestTarget, org.apache.wicket.markup.html.form.Form)) && args(target, form)")
    public void goToTop(AjaxRequestTarget target, Form form){
        JSCommun.scrollToTop(target);
    }
}