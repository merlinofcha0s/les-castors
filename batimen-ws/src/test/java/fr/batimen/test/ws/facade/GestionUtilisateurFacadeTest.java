package fr.batimen.test.ws.facade;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.UtilisateurService;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.entity.Permission;

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

    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetClientForLogin() {

        // L'objet que l'on doit recevoir du frontend quand l'utilisateur
        // tentera de s'authentifier
        LoginDTO toLogin = new LoginDTO();
        toLogin.setLogin("pebronne");
        toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

        // Appel du service qui check le login
        ClientDTO user = UtilisateurService.login(toLogin);

        // Verification des infos
        assertTrue(user.getLogin().equals("pebronne"));
        assertTrue("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=".equals(user
                .getPassword()));
        assertTrue(user.getEmail().equals("lol@lol.com"));
        assertTrue(user.getNumeroTel().equals("0615125645"));
        assertTrue(user.getPrenom().equals("Pebron"));
        assertTrue(user.getNom().equals("De la Pebronne"));
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
        ClientDTO user = UtilisateurService.login(toLogin);

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
        ClientDTO user = UtilisateurService.login(toLogin);

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

        ClientDTO client = UtilisateurService.getUtilisateurByEmail("lol@lol.com");

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
        ClientDTO artisan = UtilisateurService.getUtilisateurByEmail("lolPebronne@lol.com");

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
        int codeRetour = UtilisateurService
                .activateAccount("NTNkN2RmYzVkNWU2MDZkZjZlYTVjZGQ2ZGE0ZjljY2JhNGJjZWY5MmIxNmNiOWJmMjk2ZDVhNDY3OTEzMTIyZA==");

        // On charge le client pour vérifié les infos d'activation.
        ClientDTO client = UtilisateurService.getUtilisateurByEmail("mdr@lol.com");

        Assert.assertFalse(client.getCleActivation().isEmpty());
        Assert.assertTrue(client.getIsActive().equals(Boolean.TRUE));
        Assert.assertTrue(codeRetour == Constant.CODE_SERVICE_RETOUR_OK);

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
        int codeRetour = UtilisateurService
                .activateAccount("NTNkN2RmYzVkNWU2MDZkZjZlYTVjZGQ2ZGE0ZjljY2JhNGJjZWY5MmIxNmNiOWJmMjk2ZDVhNDY3OTEzMTIyZA=0");

        // On charge le client pour vérifié les infos d'activation.
        ClientDTO client = UtilisateurService.getUtilisateurByEmail("lolPebronne@lol.com");

        Assert.assertFalse(client.getCleActivation().isEmpty());
        Assert.assertTrue(client.getIsActive().equals(Boolean.TRUE));
        Assert.assertTrue(codeRetour == Constant.CODE_SERVICE_RETOUR_OK);
    }

    /**
     * Test de récuperation d'un hash pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetHashClient() throws BackendException {
        String hash = UtilisateurService.getHashByLogin("pebronne");
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
    public void testGetHashArtisan() throws BackendException {
        String hash = UtilisateurService.getHashByLogin("pebronneArtisanne");
        Assert.assertTrue(!hash.isEmpty());
        Assert.assertEquals("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", hash);
    }

    /**
     * Test de récuperation des roles pour un client
     * 
     * @throws BackendException
     */
    @Test
    @UsingDataSet("datasets/in/clients.yml")
    public void testGetRolesClient() throws BackendException {
        String roles = UtilisateurService.getRolesByLogin("pebronne");
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
        String roles = UtilisateurService.getRolesByLogin("pebronneArtisanne");
        Assert.assertTrue(!roles.isEmpty());
        Assert.assertEquals(TypeCompte.ARTISAN.getRole(), roles);
    }
}
