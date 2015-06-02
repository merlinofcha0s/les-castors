package fr.batimen.web.app.security;

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Named;

import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.ws.client.service.ArtisanServiceREST;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.ws.client.service.UtilisateurServiceREST;

@Named
public class Authentication implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Authentication.class);

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    @Inject
    private RolesUtils rolesUtils;

    private static final String CLIENT_KEY = "client";
    private static final String ENTREPRISE_KEY = "entreprise";

    public Boolean authenticate(String username, String password) {
        Boolean isOk = null;

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);

        try {
            SecurityUtils.getSubject().login(token);
            isOk = Boolean.TRUE;
        } catch (UnknownAccountException uae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Compte inconnu", uae);
            }
            isOk = Boolean.FALSE;
        } catch (IncorrectCredentialsException ice) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Credential incorrect", ice);
            }
            isOk = Boolean.FALSE;
        } catch (LockedAccountException lae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Compte bloqué", lae);
            }
            isOk = Boolean.FALSE;
        } catch (ExcessiveAttemptsException eae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Trop d'essai de connexion", eae);
            }
            isOk = Boolean.FALSE;
        } catch (AuthenticationException ae) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Probleme avec l'authentification", ae);
            }
            isOk = Boolean.FALSE;
        }

        if (isOk) {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setLogin(username);
            AuthenticatedWebSession.get().authenticate(username, "");
            ClientDTO client = utilisateurServiceREST.login(loginDTO);
            SecurityUtils.getSubject().getSession(true).setAttribute(CLIENT_KEY, client);

            if(rolesUtils.checkRoles(TypeCompte.ARTISAN)){
                EntrepriseDTO entrepriseDTO = artisanServiceREST.getEntrepriseInformationByArtisanLogin(client.getLogin());
                SecurityUtils.getSubject().getSession().setAttribute(ENTREPRISE_KEY, entrepriseDTO);
            }
        }
        return isOk;
    }

    public ClientDTO getCurrentUserInfo() {
        return (ClientDTO) SecurityUtils.getSubject().getSession().getAttribute(CLIENT_KEY);
    }

    public EntrepriseDTO getEntrepriseUserInfo(){
        return (EntrepriseDTO) SecurityUtils.getSubject().getSession().getAttribute(ENTREPRISE_KEY);
    }

    public void setEntrepriseUserInfo(EntrepriseDTO entrepriseDTO) {
        SecurityUtils.getSubject().getSession().setAttribute(ENTREPRISE_KEY, entrepriseDTO);
    }

    public void setCurrentUserInfo(ClientDTO clientDTO) {
        SecurityUtils.getSubject().getSession().setAttribute(CLIENT_KEY, clientDTO);
    }

    public TypeCompte getCurrentUserRolePrincipal() {
        return getCurrentUserInfo().getPermissions().get(0).getTypeCompte();
    }

    public boolean isAuthenticated(){
        return SecurityUtils.getSubject().isAuthenticated();
    }

}
