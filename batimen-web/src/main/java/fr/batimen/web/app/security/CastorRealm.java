package fr.batimen.web.app.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.client.service.ClientService;

public class CastorRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authcToken;
        String usernameForm = usernamePasswordToken.getUsername();

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(usernameForm);

        ClientDTO clientDTO = ClientService.login(loginDTO);

        if (!"".equals(clientDTO.getLogin())) {
            info = new SimpleAuthenticationInfo(clientDTO.getLogin(), clientDTO.getPassword(), getName());
        }

        return info;
    }

}
