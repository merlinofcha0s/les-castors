package fr.batimen.test.ws.facade;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.dto.*;
import fr.batimen.dto.aggregate.MesAnnoncesAnnonceDTO;
import fr.batimen.dto.aggregate.MesAnnoncesNotificationDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientServiceREST;
import fr.batimen.ws.client.service.UtilisateurServiceREST;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.entity.Permission;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class GestionUtilisateurFacadeTest extends AbstractBatimenWsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionUtilisateurFacadeTest.class);

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private AnnonceDAO annonceDAO;

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

    @Inject
    private ClientServiceREST clientsServiceREST;

    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetClientForLogin() {

        // L'objet que l'on doit recevoir du frontend quand l'utilisateur
        // tentera de s'authentifier
        LoginDTO toLogin = new LoginDTO();
        toLogin.setLogin("pebronne");
        toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

        // Appel du service qui check le login
        ClientDTO user = utilisateurServiceREST.login(toLogin);

        // Verification des infos
        assertTrue(user.getLogin().equals("pebronne"));
        assertTrue("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=".equals(user
                .getPassword()));
        assertTrue(user.getEmail().equals("lol@lol.com"));
        assertTrue(user.getNumeroTel().equals("0615125645"));
        assertTrue(user.getPrenom().equals("Pebron"));
        assertTrue(user.getNom().equals("De la Pebronne"));
        Assert.assertEquals(user.getPermissions().get(0).getTypeCompte(), TypeCompte.CLIENT);
    }

    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetArtisanForLogin() {

        // L'objet que l'on doit recevoir du frontend quand l'utilisateur
        // tentera de s'authentifier
        LoginDTO toLogin = new LoginDTO();
        toLogin.setLogin("pebronneArtisanne");
        toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

        // Appel du service qui check le login
        ClientDTO user = utilisateurServiceREST.login(toLogin);

        // Verification des infos
        assertTrue(user.getLogin().equals("pebronneArtisanne"));
        assertTrue("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=".equals(user
                .getPassword()));
        assertTrue(user.getEmail().equals("lolPebronne@lol.com"));
        assertTrue(user.getNumeroTel().equals("0615125645"));
        assertTrue(user.getPrenom().equals("Pebron"));
        assertTrue(user.getNom().equals("De la Pebronne"));
    }

    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetClientForLoginFail() throws Exception {

        // L'objet que l'on doit recevoir du frontend quand l'utilisateur
        // tentera de s'authentifier
        LoginDTO toLogin = new LoginDTO();
        toLogin.setLogin("pebronmdr");
        toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

        // Appel du service qui check le login
        ClientDTO user = utilisateurServiceREST.login(toLogin);

        // Verification que rien n'est renvoyer ce qui veut dire que la
        // combinaison login / mdp n'est pas bonne ou que l'utilisateur n'existe
        // pas
        assertTrue(user.getLogin().equals(""));
        assertTrue(user.getPassword().equals(""));
        assertTrue(user.getEmail().equals(""));
    }

    /**
     * On vérifie que le DAO renvoi bien le bon client par rapport a son email.
     * 
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetClientByEmail() {

        ClientDTO client = utilisateurServiceREST.getUtilisateurByEmail("lol@lol.com");

        // On vérifie les differentes infos du client
        assertTrue(client.getLogin().equals("pebronne"));
        assertTrue(client.getPassword().equals(
                "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY="));
        assertTrue(client.getEmail().equals("lol@lol.com"));
    }

    /**
     * On vérifie que le DAO renvoi bien le bon client par rapport a son email.
     * 
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetArtisanByEmail() {
        ClientDTO artisan = utilisateurServiceREST.getUtilisateurByEmail("lolPebronne@lol.com");

        // On vérifie les differentes infos du client
        assertTrue(artisan.getLogin().equals("pebronneArtisanne"));
        assertTrue(artisan.getPassword().equals(
                "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY="));
        assertTrue(artisan.getEmail().equals("lolPebronne@lol.com"));
    }

    /**
     * On vérifie que le DAO n'enregistre pas le client si il y a duplication.
     * 
     * @throws BackendException
     */
    @Test(expected = DuplicateEntityException.class)
    @UsingDataSet("datasets/in/clients.yml")
    public void testSaveDuplilcateClient() throws BackendException, DuplicateEntityException {

        Client clientDuplicate = new Client();
        clientDuplicate.setEmail("lol@lol.com");
        clientDuplicate.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");
        clientDuplicate.setLogin("pebronne");
        clientDuplicate.setNom("De la Pebronne");
        clientDuplicate.setNumeroTel("0615125645");
        clientDuplicate.setPrenom("Pebron");

        Permission permission = new Permission();
        permission.setTypeCompte(TypeCompte.ARTISAN);

        clientDuplicate.getPermissions().add(permission);

        Calendar calClient = Calendar.getInstance(Locale.FRANCE);
        calClient.set(2014, 01, 10, 00, 00, 00);
        clientDuplicate.setDateInscription(calClient.getTime());

        clientDAO.saveNewClient(clientDuplicate);
    }

    /**
     * On test le service d'activation de compte
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testActivationClient() throws BackendException {
        int codeRetour = utilisateurServiceREST
                .activateAccount("NTNkN2RmYzVkNWU2MDZkZjZlYTVjZGQ2ZGE0ZjljY2JhNGJjZWY5MmIxNmNiOWJmMjk2ZDVhNDY3OTEzMTIyZA==");

        // On charge le client pour vérifié les infos d'activation.
        ClientDTO client = utilisateurServiceREST.getUtilisateurByEmail("mdr@lol.com");

        Assert.assertFalse(client.getCleActivation().isEmpty());
        Assert.assertTrue(client.getIsActive().equals(Boolean.TRUE));
        Assert.assertTrue(codeRetour == CodeRetourService.RETOUR_OK);

        List<Annonce> annonces = annonceDAO.getAnnoncesByLogin(client.getLogin());
        Annonce annonce = annonces.get(0);
        Assert.assertEquals(EtatAnnonce.ACTIVE, annonce.getEtatAnnonce());
    }

    /**
     * On test le service d'activation de compte
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testActivationArtisan() throws BackendException {
        int codeRetour = utilisateurServiceREST
                .activateAccount("NTNkN2RmYzVkNWU2MDZkZjZlYTVjZGQ2ZGE0ZjljY2JhNGJjZWY5MmIxNmNiOWJmMjk2ZDVhNDY3OTEzMTIyZA=0");

        // On charge le client pour vérifié les infos d'activation.
        ClientDTO client = utilisateurServiceREST.getUtilisateurByEmail("lolPebronne@lol.com");

        Assert.assertFalse(client.getCleActivation().isEmpty());
        Assert.assertTrue(client.getIsActive().equals(Boolean.TRUE));
        Assert.assertTrue(codeRetour == CodeRetourService.RETOUR_OK);
    }

    /**
     * Test de récuperation d'un hash pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetHashClientActivated() throws BackendException {
        String hash = utilisateurServiceREST.getHashByLogin("pebronne");
        Assert.assertTrue(!hash.isEmpty());
        Assert.assertEquals("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", hash);
    }

    /**
     * Test de récuperation d'un hash pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetHashClientNotActivated() throws BackendException {
        String hash = utilisateurServiceREST.getHashByLogin("xavier");
        Assert.assertTrue(hash.isEmpty());
    }

    /**
     * Test de récuperation d'un hash pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetHashArtisanActivated() throws BackendException {
        String hash = utilisateurServiceREST.getHashByLogin("moiArtisanne");
        Assert.assertTrue(!hash.isEmpty());
        Assert.assertEquals("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", hash);
    }

    /**
     * Test de récuperation d'un hash pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetHashArtisanNotActivated() throws BackendException {
        String hash = utilisateurServiceREST.getHashByLogin("pebronneArtisanne");
        Assert.assertTrue(hash.isEmpty());
    }

    /**
     * Test de récuperation des roles pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetRolesClient() throws BackendException {
        String roles = utilisateurServiceREST.getRolesByLogin("pebronne");
        Assert.assertTrue(!roles.isEmpty());
        Assert.assertEquals(TypeCompte.CLIENT.getRole(), roles);
    }

    /**
     * Test de récuperation des roles pour un artisan
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetRolesArtisan() throws BackendException {
        String roles = utilisateurServiceREST.getRolesByLogin("pebronneArtisanne");
        Assert.assertTrue(!roles.isEmpty());
        Assert.assertEquals(TypeCompte.ARTISAN.getRole(), roles);
    }

    /**
     * Test de récuperation des roles pour un artisan
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/update_utilisateur.yml")
    @ShouldMatchDataSet(value = "datasets/out/update_utilisateur.yml", excludeColumns = { "id", "datemaj" })
    public void testUpdateUtilisateurInfos() throws BackendException {

        // On charge le client de la bdd
        ClientDTO client = utilisateurServiceREST.getUtilisateurByEmail("lol@lol.com");
        Assert.assertNotNull(client);

        // On change quelques données
        client.setNom("Du pébron");
        client.setNumeroTel("0512458596");

        ModifClientDTO modifClientDTO = new ModifClientDTO();

        ModelMapper mapper = new ModelMapper();
        mapper.map(client, modifClientDTO);

        modifClientDTO.setOldEmail(client.getEmail());

        // On appel le ws
        Integer codeRetour = utilisateurServiceREST.updateUtilisateurInfos(modifClientDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);
    }

    /**
     * Cas de test : Le client recupere ses notifications <br/>
     * Ce test verifie que les données de notifications remontent de manière correctes
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetNotificationMesAnnoncesParUnClient() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronne");
        demandeMesAnnoncesDTO.setRangeNotificationsDebut(0);
        demandeMesAnnoncesDTO.setRangeNotificationsFin(3);

        MesAnnoncesNotificationDTO mesAnnoncesNotificationDTO = utilisateurServiceREST.getNotificationForMesAnnonces(demandeMesAnnoncesDTO);

        List<NotificationDTO> notifications = mesAnnoncesNotificationDTO.getNotifications();

        Assert.assertEquals(1, notifications.size());

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
                    .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")
                    && notification.getSiret().equals("43394298400017")) {
                notificationPresent = Boolean.TRUE;
            }
        }

        Assert.assertTrue(notificationPresent);
    }

    /**
     * Cas de test : Le client se rend sur la page "mes annonces" <br/>
     * Ce test verifie que les données d'annonces remontent de maniere correctes
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetAnnonceForMesAnnoncesParUnClient() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronne");
        demandeMesAnnoncesDTO.setRangeAnnoncesDebut(0);
        demandeMesAnnoncesDTO.setRangeAnnonceFin(3);

        MesAnnoncesAnnonceDTO mesAnnonces = utilisateurServiceREST.getAnnonceForMesAnnonces(demandeMesAnnoncesDTO);

        // Check de l'annonce.
        Boolean rightDescription = Boolean.FALSE;
        Boolean rightHashID = Boolean.FALSE;

        for (AnnonceDTO annonce : mesAnnonces.getAnnonces()) {
            if (annonce.getDescription().equals("Peinture d'un mur")) {
                rightDescription = Boolean.TRUE;
            }
            if (annonce
                    .getHashID()
                    .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")) {
                rightHashID = Boolean.TRUE;
            }
        }

        Assert.assertTrue(rightDescription);
        Assert.assertTrue(rightHashID);
        Assert.assertEquals(2, mesAnnonces.getAnnonces().size());
        Assert.assertEquals(Long.valueOf(2), mesAnnonces.getNbTotalAnnonces());
    }

    /**
     * Cas de test : Un artisan se rend sur la page "mes annonces" <br/>
     * Ce test verifie que les données d'annonces remontent de maniere correctes
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetAnnonceForMesAnnoncesParUnArtisan() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronneArtisanne");
        demandeMesAnnoncesDTO.setRangeAnnoncesDebut(0);
        demandeMesAnnoncesDTO.setRangeAnnonceFin(3);

        getAnnoncesMesAnnoncesPourAdminOuArtisan(demandeMesAnnoncesDTO);
    }

    /**
     * Cas de test : Un artisan recupère ses notifications <br/>
     * Ce test verifie que les données de notifications remontent de maniere correctes
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetNotificationForMesAnnoncesParUnArtisan() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronneArtisanne");
        demandeMesAnnoncesDTO.setRangeNotificationsDebut(0);
        demandeMesAnnoncesDTO.setRangeNotificationsFin(3);

        getInfoNotificationForMesAnnoncesPourAdminOuArtisan(demandeMesAnnoncesDTO);
    }

    /**
     * Cas de test : Un administrateur utilise le service pour récuperer les informations de notification
     * d'un client / artisan
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetNotificationsForMesAnnoncesParUnAdmin() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("admin");
        demandeMesAnnoncesDTO.setRangeNotificationsDebut(0);
        demandeMesAnnoncesDTO.setRangeNotificationsFin(3);

        getInfoNotificationForMesAnnoncesPourAdminOuArtisan(demandeMesAnnoncesDTO);
    }

    /**
     * Cas de test : Un administrateur utilise le service pour récuperer les annonces d'un client / artisan
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetAnnoncesMesAnnoncesParUnAdmin() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("admin");
        demandeMesAnnoncesDTO.setRangeAnnoncesDebut(0);
        demandeMesAnnoncesDTO.setRangeAnnonceFin(3);

        getAnnoncesMesAnnoncesPourAdminOuArtisan(demandeMesAnnoncesDTO);
    }

    /**
     * Cas de test : Un client essaye de recuperer des annonces qu'il ne possede pas.
     * Le webservice ne lui renvoi rien.
     *
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetAnnoncesForMesAnnoncesParUnClientPasLesDroits() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronne");
        demandeMesAnnoncesDTO.setRangeAnnoncesDebut(0);
        demandeMesAnnoncesDTO.setRangeAnnonceFin(3);

        MesAnnoncesAnnonceDTO mesAnnonces = utilisateurServiceREST.getAnnonceForMesAnnonces(demandeMesAnnoncesDTO);

        Assert.assertTrue(mesAnnonces.getAnnonces().isEmpty());
    }

    /**
     * Cas de test : Un client essaye de recuperer des notifications qu'il ne possede pas.
     * Le webservice ne lui renvoi rien.
     */
    @Test
    @UsingDataSet("datasets/in/client_notification_annonce.yml")
    public void testGetNotificationsForMesAnnoncesParUnClientPasLesDroits() {
        DemandeMesAnnoncesDTO demandeMesAnnoncesDTO = new DemandeMesAnnoncesDTO();
        demandeMesAnnoncesDTO.setLogin("pebronneArtisanne");
        demandeMesAnnoncesDTO.setLoginDemandeur("pebronne");
        demandeMesAnnoncesDTO.setRangeNotificationsDebut(0);
        demandeMesAnnoncesDTO.setRangeNotificationsFin(3);

        MesAnnoncesNotificationDTO mesAnnoncesNotificationDTO = utilisateurServiceREST.getNotificationForMesAnnonces(demandeMesAnnoncesDTO);

        Assert.assertTrue(mesAnnoncesNotificationDTO.getNotifications().isEmpty());
    }

    private void getAnnoncesMesAnnoncesPourAdminOuArtisan(DemandeMesAnnoncesDTO demandeMesAnnoncesDTO) {
        MesAnnoncesAnnonceDTO mesAnnoncesAnnonceDTO = utilisateurServiceREST.getAnnonceForMesAnnonces(demandeMesAnnoncesDTO);
        List<AnnonceDTO> annonces = mesAnnoncesAnnonceDTO.getAnnonces();

        // Check de l'annonce.
        Boolean rightDescription = Boolean.FALSE;
        Boolean rightHashID = Boolean.FALSE;

        for (AnnonceDTO annonce : annonces) {
            if (annonce.getDescription().equals("Peinture d'un mur")) {
                rightDescription = Boolean.TRUE;
            }
            if (annonce
                    .getHashID()
                    .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")) {
                rightHashID = Boolean.TRUE;
            }
        }

        Assert.assertTrue(rightDescription);
        Assert.assertTrue(rightHashID);
        Assert.assertEquals(1, annonces.size());
    }

    private void getInfoNotificationForMesAnnoncesPourAdminOuArtisan(DemandeMesAnnoncesDTO demandeMesAnnoncesDTO) {
        MesAnnoncesNotificationDTO mesAnnoncesNotificationDTO = utilisateurServiceREST.getNotificationForMesAnnonces(demandeMesAnnoncesDTO);
        List<NotificationDTO> notifications = mesAnnoncesNotificationDTO.getNotifications();

        Assert.assertEquals(1, notifications.size());

        // Check de la notification.
        Boolean notificationPresent = Boolean.FALSE;

        for (NotificationDTO notification : notifications) {
            if (notification.getTypeNotification().equals(TypeNotification.A_CHOISI_ENTREPRISE)
                    && notification.getPourQuiNotification().equals(TypeCompte.ARTISAN)
                    && notification.getStatutNotification().equals(StatutNotification.VU)
                    && notification.getArtisanLogin().equals("pebronneArtisanne")
                    && notification.getClientLogin().equals("pebronne")
                    && notification.getNomEntreprise().equals("Pebronne enterprise")
                    && notification
                    .getHashIDAnnonce()
                    .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")
                    && notification.getSiret().equals("43394298400017")) {
                notificationPresent = Boolean.TRUE;
            }
        }

        Assert.assertTrue(notificationPresent);
    }
}
