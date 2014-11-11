package fr.batimen.test.ws.facade;

import java.util.List;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import fr.batimen.core.exception.BackendException;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.MesAnnoncesPageDTO;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientsService;

public class GestionClientFacadeTest extends AbstractBatimenWsTest {

    /**
     * Test de récuperation des roles pour un artisan
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetInfoForMesAnnoncesPage() {
        MesAnnoncesPageDTO mesAnnoncesPage = ClientsService.getMesInfosAnnoncePage("pebronne");

        List<NotificationDTO> notifications = mesAnnoncesPage.getNotifications();
        List<AnnonceDTO> annonces = mesAnnoncesPage.getAnnonces();

        Assert.assertEquals(1, notifications.size());
        Assert.assertEquals(1, annonces.size());
    }
}
