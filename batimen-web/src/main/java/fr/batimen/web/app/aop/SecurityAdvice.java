package fr.batimen.web.app.aop;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.enums.TypeCompte;

@Aspect
public class SecurityAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAdvice.class);

    @Before(value = "execution(fr.batimen.web.client.extend.account.client.*.new(..))")
    public void checkClientRole() {
        try {
            SecurityUtils.getSubject().checkRole(TypeCompte.CLIENT.getRole());
        } catch (AuthorizationException ae) {

        }

    }
}