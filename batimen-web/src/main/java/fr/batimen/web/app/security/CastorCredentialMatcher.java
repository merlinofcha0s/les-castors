package fr.batimen.web.app.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.security.HashHelper;

public class CastorCredentialMatcher implements CredentialsMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastorCredentialMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        // Les infos qui viennent du form
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String usernameForm = usernamePasswordToken.getUsername();
        char[] passwordForm = (char[]) usernamePasswordToken.getCredentials();

        // Les infos qui viennent de la base de données
        SimpleAuthenticationInfo usernamePasswordDB = (SimpleAuthenticationInfo) info;
        String usernameDB = String.valueOf(usernamePasswordDB.getPrincipals().getPrimaryPrincipal());
        String passwordDB = String.valueOf(usernamePasswordDB.getCredentials());

        // Vérification du password avec le hash qui se trouve dans la bdd
        boolean usernameMatch = usernameForm.equals(usernameDB);
        boolean passwordMatch = HashHelper.check(new String(passwordForm), passwordDB);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Verification de l'username : " + usernameMatch);
            LOGGER.debug("Verification du password : " + passwordMatch);
        }

        return usernameMatch && passwordMatch;
    }
}
