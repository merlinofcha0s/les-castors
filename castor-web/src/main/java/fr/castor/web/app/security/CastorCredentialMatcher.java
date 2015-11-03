package fr.castor.web.app.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.castor.core.security.HashHelper;

/**
 * Classe d'implémentation de la sécurité avec shiro. Décrit la maniere
 * permettant de savoir si les infos d'authentification match avec le contenu de
 * la BDD
 * 
 * @author Casaucau Cyril
 * 
 */
public class CastorCredentialMatcher implements CredentialsMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastorCredentialMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        // Les infos qui viennent du form
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        char[] passwordForm = (char[]) usernamePasswordToken.getCredentials();

        // Les infos qui viennent de la base de données
        SimpleAuthenticationInfo usernamePasswordDB = (SimpleAuthenticationInfo) info;
        String passwordDB = String.valueOf(usernamePasswordDB.getCredentials());

        // Vérification du password avec le hash qui se trouve dans la bdd
        boolean passwordMatch = HashHelper.check(new String(passwordForm), passwordDB);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Verification du password : " + passwordMatch);
        }

        return passwordMatch;
    }
}
