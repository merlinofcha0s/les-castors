package fr.batimen.test.ws.facade;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotationDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.MesAnnoncesDTO;
import fr.batimen.dto.aggregate.MonProfilDTO;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientsServiceREST;

public class GestionClientFacadeTest extends AbstractBatimenWsTest {

    @Inject
    private ClientsServiceREST clientsServiceREST;

    /**
     * Cas de test : Le client se rend sur la page "mes annonces" <br/>
     * Ce test verifie que les données remontent de maniere correctes
     * 
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetInfoForMesAnnonces() {
        MesAnnoncesDTO mesAnnonces = clientsServiceREST.getMesInfosAnnonce("pebronne");

        List<NotificationDTO> notifications = mesAnnonces.getNotifications();
        List<AnnonceDTO> annonces = mesAnnonces.getAnnonces();

        Assert.assertEquals(1, notifications.size());
        Assert.assertEquals(1, annonces.size());

        // Check de la notification.
        Boolean notificationPresent = Boolean.FALSE;

        for (NotificationDTO notification : notifications) {
            if (notification.getTypeNotification().equals(TypeNotification.INSCRIT_A_ANNONCE)
                    && notification.getPourQuiNotification().equals(TypeCompte.CLIENT)
                    && notification.getStatutNotification().equals(StatutNotification.VU)
                    && notification.getArtisanLogin().equals("pebronneArtisanne")
                    && notification.getClientLogin().equals("pebronne")
                    && notification.getNomEntreprise().equals("Pebronne enterprise")
                    && notification
                            .getHashIDAnnonce()
                            .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")) {
                notificationPresent = Boolean.TRUE;
            }
        }

        Assert.assertTrue(notificationPresent);

        // Check de l'annonce.
        Boolean rightDescription = Boolean.FALSE;

        for (AnnonceDTO annonce : annonces) {
            if (annonce.getDescription().equals("Peinture d'un mur")) {
                rightDescription = Boolean.TRUE;
            }
        }

        Assert.assertTrue(rightDescription);
        Assert.assertEquals(1, annonces.size());
    }

    /**
     * Cas de test : Le client se rend sur la page "mon profil" <br/>
     * Ce test verifie que les données remontent de maniere correctes
     * 
     */
    @Test
    @UsingDataSet("datasets/in/mon_profil.yml")
    public void testGetInfoForMonProfil() {
        MonProfilDTO monProfilDTO = clientsServiceREST.getMesInfosForMonProfil("pebronne");

        Assert.assertEquals(Long.valueOf("2"), monProfilDTO.getNbAnnonce());

        Boolean isDataCorrectForNotation1 = Boolean.FALSE;
        Boolean isDataCorrectForNotation2 = Boolean.FALSE;

        Assert.assertEquals(2, monProfilDTO.getNotations().size());

        for (NotationDTO notation : monProfilDTO.getNotations()) {
            if (notation.getScore().equals(Double.valueOf("3")) && notation.getCommentaire().equals("Bon Travail")
                    && notation.getNomEntreprise().equals("Pebronne enterprise")) {
                isDataCorrectForNotation1 = Boolean.TRUE;
            }
            if (notation.getScore().equals(Double.valueOf("5")) && notation.getCommentaire().equals("Excellent")
                    && notation.getNomEntreprise().equals("Pebronne enterprise")) {
                isDataCorrectForNotation2 = Boolean.TRUE;
            }
        }

        Assert.assertTrue(isDataCorrectForNotation1);
        Assert.assertTrue(isDataCorrectForNotation2);
    }
}
