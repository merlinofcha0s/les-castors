package fr.castor.web.app.security;

import fr.castor.ws.client.service.UtilisateurServiceREST;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Named;

@Named
public class CastorRealm extends AuthorizingRealm {

    private UtilisateurServiceREST utilisateurServiceREST;

    public CastorRealm() {
        //get reference to BeanManager
        BeanManager bm = CDI.current().getBeanManager();
        Bean<UtilisateurServiceREST> bean = (Bean<UtilisateurServiceREST>) bm.getBeans(UtilisateurServiceREST.class).iterator().next();
        CreationalContext<UtilisateurServiceREST> ctx = bm.createCreationalContext(bean);

        //get reference to your CDI managed bean
        utilisateurServiceREST = (UtilisateurServiceREST) bm.getReference(bean, UtilisateurServiceREST.class, ctx);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String roles = utilisateurServiceREST.getRolesByLogin(principalCollection.getPrimaryPrincipal().toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (String role : roles.split(", ")) {
            info.addRole(role);
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authcToken;
        String usernameForm = usernamePasswordToken.getUsername();

        String hash = utilisateurServiceREST.getHashByLogin(usernameForm);

        if (!hash.isEmpty()) {
            info = new SimpleAuthenticationInfo(usernameForm, hash, getName());
        }

        return info;
    }

}
