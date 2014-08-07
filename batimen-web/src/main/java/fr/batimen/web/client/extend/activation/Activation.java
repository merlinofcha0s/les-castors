package fr.batimen.web.client.extend.activation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;

import fr.batimen.core.constant.Constant;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ClientService;

public class Activation extends MasterPage {

    private static final long serialVersionUID = 99813244363266423L;

    private String cleActivation;
    private final Label messageActivationCompte;
    private final Label messageActivationCompte2;
    private final Image confirmationImg;
    private final ContextRelativeResource pathImgOk = new ContextRelativeResource("img/thumbsUp.png");
    private final ContextRelativeResource pathImgKo = new ContextRelativeResource("img/error2.png");;

    public Activation() {
        super("Activation d'un compte lescastors.fr", "activation compte lescastors.fr", "Activation de votre compte",
                false, "");
        this.setActiveMenu(NONE);
        this.setVersioned(false);
        messageActivationCompte = new Label("messageActivationCompte", new Model<String>());
        messageActivationCompte2 = new Label("messageActivationCompte2", new Model<String>());
        confirmationImg = new Image("confirmationImg", pathImgOk);

        this.add(confirmationImg);
        this.add(messageActivationCompte);
        this.add(messageActivationCompte2);
    }

    public Activation(PageParameters params) {
        this();
        cleActivation = params.get("key").toString();
        if (!cleActivation.isEmpty()) {

            Integer codeRetourService = activatingAccount(cleActivation);

            if (codeRetourService.equals(Constant.CODE_SERVICE_RETOUR_OK)) {
                messageActivationCompte.setDefaultModelObject("Votre compte est activé !!");
                messageActivationCompte2
                        .setDefaultModelObject("Vous pouvez maintenant vous connecter à notre espace membre");
                confirmationImg.setImageResource(pathImgOk);
            } else if (codeRetourService.equals(Constant.CODE_SERVICE_RETOUR_KO)) {
                messageActivationCompte.setDefaultModelObject("Problème d'accès au service.");
                messageActivationCompte2.setDefaultModelObject("Si le probleme persiste contactez nous.");
                confirmationImg.setImageResource(pathImgKo);
            } else if (codeRetourService.equals(Constant.CODE_SERVICE_ANNONCE_RETOUR_DEJA_ACTIF)) {
                messageActivationCompte.setDefaultModelObject("Votre compte est déjà actif");
                messageActivationCompte2.setDefaultModelObject("");
                confirmationImg.setImageResource(pathImgKo);
            } else if (codeRetourService.equals(Constant.CODE_SERVICE_ANNONCE_RETOUR_COMPTE_INEXISTANT)) {
                getMessageIncorrectKey();
            }
        } else {
            getMessageIncorrectKey();
        }

    }

    private void getMessageIncorrectKey() {
        messageActivationCompte.setDefaultModelObject("Clé d'activation incorrecte");
        messageActivationCompte2
                .setDefaultModelObject("Veuillez suivre le lien contenu dans le mail de confirmation d'inscription");
        confirmationImg.setImageResource(pathImgKo);
    }

    private int activatingAccount(String cleActivation) {
        return ClientService.activateAccount(cleActivation);
    }

}
