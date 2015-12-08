package fr.castor.web.app.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Advice qui gére les accés au page où il faut être connecté <br/>
 * Il verifie le rôle de l'utilisateur avant d'instancier la page.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
@Aspect
public class SecurityAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAdvice.class);



    /**
     * Verifie que l'utilisateur qui se connecte a cette page a bien le rôle
     * CLIENT
     * 
     * @param joinPoint
     *            Permet de controler l'appel à la methode qu'on a hijack.
     *            Contient également les parametres, etc
     * @throws Throwable
     */
    // @Around(value =
    // "execution(fr.batimen.web.client.extend.member.client.*.new())")
    /*public void checkClientRole(ProceedingJoinPoint joinPoint) throws Throwable {
        // On recupère l'objet de la classe qui va être instancié
        WebPage page = (WebPage) joinPoint.getThis();
        Subject utilisateur = SecurityUtils.getSubject();
        try {
            // On check le role de l'utilisateur
            utilisateur.checkRole(TypeCompte.CLIENT.getRole());
            // On laisse passer vu qu'aucune exception n'a été levé
            joinPoint.proceed();
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Accés interdit detecté ", ae);
                LOGGER.warn("Accés ok pour : " + TypeCompte.CLIENT.getRole());
                LOGGER.warn("Login du compte demandeur : " + utilisateur.getPrincipal().toString());
            }
            page.setResponsePage(AccesInterdit.class);
        }
    }*/
}