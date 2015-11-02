package fr.batimen.web.app.security;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;

public class ShiroCastorListener extends EnvironmentLoaderListener {

    @Inject
    private CastorRealm castorRealm;

    @Override
    protected WebEnvironment createEnvironment(ServletContext pServletContext) {
        WebEnvironment environment = super.createEnvironment(pServletContext);

        RealmSecurityManager rsm = (RealmSecurityManager) environment.getSecurityManager();
        rsm.setRealm(castorRealm);

        castorRealm.setCredentialsMatcher(new CastorCredentialMatcher());
        castorRealm.setAuthorizationCachingEnabled(true);
        castorRealm.setAuthenticationCachingEnabled(true);

        ((DefaultWebEnvironment) environment).setSecurityManager(rsm);
        return environment;
    }

}
