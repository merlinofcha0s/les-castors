package fr.batimen.web.app.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import fr.batimen.ws.client.service.ClientService;

public class CastorRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authcToken;
        String usernameForm = usernamePasswordToken.getUsername();

        String hash = ClientService.getHashByLogin(usernameForm);

        if (!hash.isEmpty()) {
            info = new SimpleAuthenticationInfo(usernameForm, hash, getName());
        }

        return info;
    }

}
