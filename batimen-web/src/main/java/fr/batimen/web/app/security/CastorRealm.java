package fr.batimen.web.app.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import fr.batimen.ws.client.service.UtilisateurServiceREST;

@Named
public class CastorRealm extends AuthorizingRealm {

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

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
