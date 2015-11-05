package fr.castor.web.client.modal;

import javax.inject.Inject;

import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.client.component.ModalCastor;
import fr.castor.web.client.event.SuppressionOpenEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.dto.ClientDTO;
import fr.castor.dto.DemandeAnnonceDTO;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.client.event.SuppressionCloseEvent;
import fr.castor.web.client.extend.Accueil;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.ws.client.service.AnnonceServiceREST;

/**
 * Panel qui sert a demander une confirmation à l'utilisateur lorsque celui ci
 * veut supprimer son annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class SuppressionModal extends ModalCastor {

    private static final long serialVersionUID = -2013052713793815773L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionModal.class);

    @Inject
    private AnnonceServiceREST annonceService;

    @Inject
    private Authentication authentication;

    private AjaxFallbackLink<Void> yes;
    private AjaxFallbackLink<Void> no;

    private final RolesUtils rolesUtils;

    private final String hashID;

    public SuppressionModal(String id, String hashID, String title, String size) {
        super(id, title, size);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation de la popup de suppression");
        }
        this.hashID = hashID;
        rolesUtils = new RolesUtils();
        initLinkPopup();
    }

    public final void initLinkPopup() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation des liens de la popup de suppression");
        }

        yes = new AjaxFallbackLink<Void>("yes") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                MesAnnonces mesAnnonces = null;
                ClientDTO clientConnected = authentication.getCurrentUserInfo();

                DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
                demandeAnnonceDTO.setId(hashID);
                demandeAnnonceDTO.setLoginDemandeur(clientConnected.getLogin());
                demandeAnnonceDTO.setTypeCompteDemandeur(authentication.getCurrentUserRolePrincipal());

                Integer codeRetour = annonceService.suppressionAnnonce(demandeAnnonceDTO);

                // Suivant le role de l'utilisateur on ne redirige pas au meme
                // endroit
                if (rolesUtils.checkRoles(TypeCompte.CLIENT)) {
                    if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                        mesAnnonces = new MesAnnonces("Votre annonce a bien été supprimée",
                                FeedbackMessageLevel.SUCCESS);
                        this.setResponsePage(mesAnnonces);
                    } else {
                        mesAnnonces = new MesAnnonces(
                                "Problème technique, impossible de supprimer votre annonce :( Veuillez reessayer plus tard",
                                FeedbackMessageLevel.ERROR);
                        this.setResponsePage(mesAnnonces);
                    }
                } else if (rolesUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
                    // TODO Faire la meme chose que pour les clients mais
                    // redirigé vers la page principal admin
                    this.setResponsePage(Accueil.class);
                }
                close(target);
            }
        };
        yes.setOutputMarkupId(true);
        yes.setMarkupId("yes");

        no = new AjaxFallbackLink<Void>("no") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                close(target);
            }
        };
        no.setOutputMarkupId(true);
        no.setMarkupId("no");

        add(yes, no);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof SuppressionOpenEvent) {
            SuppressionOpenEvent suppressionOpenEvent = (SuppressionOpenEvent) event.getPayload();
            open(suppressionOpenEvent.getTarget());
        }

        if (event.getPayload() instanceof SuppressionCloseEvent) {
            SuppressionCloseEvent suppressionCloseEvent = (SuppressionCloseEvent) event.getPayload();
            close(suppressionCloseEvent.getTarget());
        }
    }
}