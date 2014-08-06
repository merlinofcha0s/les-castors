package fr.batimen.web.client.extend.activation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.core.constant.Constant;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ClientService;

public class Activation extends MasterPage {

    private static final long serialVersionUID = 99813244363266423L;

    private String cleActivation;
    private final Label messageActivationCompte;
    private final Label messageActivationCompte2;

    public Activation() {
        super("Activation d'un compte lescastors.fr", "activation compte lescastors.fr", "Activation de votre compte",
                false, "");
        this.setActiveMenu(NONE);

        messageActivationCompte = new Label("messageActivationCompte", new Model<String>());
        messageActivationCompte2 = new Label("messageActivationCompte2", new Model<String>());
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
            } else if (codeRetourService.equals(Constant.CODE_SERVICE_RETOUR_KO)) {
                messageActivationCompte.setDefaultModelObject("Problème d'accès au service.");
                messageActivationCompte2.setDefaultModelObject("Si le probleme persiste contacté nous.");
            }
        }

    }

    private int activatingAccount(String cleActivation) {
        return ClientService.activateAccount(cleActivation);
    }

}
